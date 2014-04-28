/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.impl.au.uniQld.rota;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.dlect.exception.DLectException;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClientImpl;

public class UQRotaHelperImpl implements UQRotaHelper {

    private static final ImmutableStream STREAM = new ImmutableStream("Unknown");
    private static final ThreadLocalDateFormat FORMAT = new ThreadLocalDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    private final Table<Integer, String, Integer> offeringMappings = HashBasedTable.create();
    private final LoadingCache<Integer, Set<UQRotaStream>> offeringStreamCache = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<Integer, Set<UQRotaStream>>() {

                @Override
                @Nonnull
                public Set<UQRotaStream> load(@Nonnull Integer offeringId) throws Exception {
                    return offeringParser.getStreamsFor(offeringId);
                }
            });

    private final UQRotaSemesterParser semesterParser;
    private final UQRotaOfferingSearchParser offeringSearchParser;
    private final UQRotaOfferingParser offeringParser;

    public UQRotaHelperImpl() {
        this(new BlackboardHttpClientImpl());
    }

    public UQRotaHelperImpl(BlackboardHttpClient client) {
        this(new UQRotaSemesterParserImpl(client), new UQRotaOfferingSearchParserImpl(client), new UQRotaOfferingParserImpl(client));
    }

    public UQRotaHelperImpl(UQRotaSemesterParser semesterParser,
                            UQRotaOfferingSearchParser offeringSearchParser,
                            UQRotaOfferingParser offeringParser) {
        this.semesterParser = semesterParser;
        this.offeringSearchParser = offeringSearchParser;
        this.offeringParser = offeringParser;
    }

    @Override
    public ImmutableSemester getSemester(int semCode) {
        try {
            Optional<ImmutableSemester> sem = semesterParser.getSemester(semCode);
            if (sem.isPresent()) {
                return sem.get();
            }
        } catch (DLectException ex) {
            ProviderLogger.LOGGER.error("Failed to find semester information for code: " + semCode, ex);
        }
        // Make a best guess about the semester information.
        return new ImmutableSemester(semCode, "Semester " + semCode, Integer.toString(semCode));
    }

    @Override
    public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode) {

        Set<UQRotaStream> rotaStreams = getAllStreamsIn(subjectCode, semCode);
        Set<ImmutableStream> returnStreams = Sets.newHashSet();

        for (UQRotaStream rs : rotaStreams) {
            if ("L".equalsIgnoreCase(rs.getSeriesName())) {
                returnStreams.add(toImmutableStream(rs));
            }
        }

        return returnStreams;
    }

    @Override
    public Set<ImmutableStream> getStreamsFor(String subjectCode, int semCode, Date lectureTime, String lectureBuilding, String lectureRoom) {
        Set<UQRotaStream> rotaStreams = getAllStreamsIn(subjectCode, semCode);
        Set<ImmutableStream> returnStreams = Sets.newHashSet();

        boolean checkRooms = !Strings.isNullOrEmpty(lectureBuilding) && !Strings.isNullOrEmpty(lectureRoom);
        long time = lectureTime.getTime();
        // TODO re-work this loop inside a loop inside a loop ...
        for (UQRotaStream rs : rotaStreams) {
            for (UQRotaStreamSession rss : rs.getSession()) {
                for (Date d : rss.getDates()) {
                    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                    // Reset, then set the days and minutes correctly.
                    c.setTimeInMillis(0);
                    c.add(Calendar.DAY_OF_MONTH, (int) TimeUnit.DAYS.convert(d.getTime(), TimeUnit.MILLISECONDS));
                    c.add(Calendar.MINUTE, rss.getStartMins());
                    if (time == c.getTimeInMillis()) {
                        if (!checkRooms) {
                            returnStreams.add(toImmutableStream(rs));
                        } else {
                            // TODO check lecture location.
                            returnStreams.add(toImmutableStream(rs));
                        }
                    }
                }
            }
        }
        if (returnStreams.size() == 1) {
            return returnStreams;
        }
        // If there is a lecture stream, then assume that that is the correct one and discard the others.
        Set<ImmutableStream> lectureStreams = Sets.newHashSet();
        for (ImmutableStream stream : returnStreams) {
            if (stream.getName().startsWith("L")) {
                lectureStreams.add(stream);
            }
        }
        if (lectureStreams.isEmpty()) {
            return returnStreams;
        } else {
            return lectureStreams;
        }
    }

    @Nonnull
    protected Set<UQRotaStream> getAllStreamsIn(String subjectCode, int semCode) {
        Integer offeringId = getOfferingId(semCode, subjectCode);
        System.out.println("Offering ID: " + offeringId);
        if (offeringId == null) {
            return ImmutableSet.of();
        }
        try {
            return offeringStreamCache.get(offeringId);
        } catch (ExecutionException ex) {
            return ImmutableSet.of();
        }
    }

    @Nullable
    @CheckForNull
    protected Integer getOfferingId(int semCode, String subjectCode) {
        Integer offering = offeringMappings.get(semCode, subjectCode);
        if (offering == null) {
            try {
                offering = offeringSearchParser.getOfferingId(subjectCode, semCode);
                offeringMappings.put(semCode, subjectCode, offering);
            } catch (DLectException ex) {
                return null;
            }
        }
        // offering should not be null here.
        return offering;
    }

    private ImmutableStream toImmutableStream(UQRotaStream rs) {
        String gn = rs.getGroupNumber();
        if (gn.isEmpty()) {
            return new ImmutableStream(rs.getSeriesName());
        } else {
            return new ImmutableStream(rs.getSeriesName() + Integer.parseInt(gn, 10));
        }
    }

}
