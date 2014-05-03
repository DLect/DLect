/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.subject;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.dlect.exception.DLectException;
import org.dlect.exception.DLectExceptionCause;
import org.dlect.immutable.model.ImmutableLecture;
import org.dlect.immutable.model.ImmutableSemester;
import org.dlect.immutable.model.ImmutableStream;
import org.dlect.immutable.model.ImmutableSubject;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.SubjectProvider;
import org.dlect.provider.base.blackboard.BlackboardSubjectCustomiser;
import org.dlect.provider.base.blackboard.helper.BlackboardXmlParser;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubject;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectListing;

import static org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient.DLECT_SHORT_IDENTIFIER;

/**
 *
 * @author lee
 */
public class BlackboardSubjectProvider implements SubjectProvider {

    private static final ImmutableList<ImmutableLecture> EMPTY_LECTURE_LIST = ImmutableList.of();
    private static final ImmutableList<ImmutableStream> EMPTY_STREAM_LIST = ImmutableList.of();

    private static final String ENROLLMENTS_LOCATION = "enrollments?course_type=ALL&include_grades=false&language=en_GB&v=1&ver=" + DLECT_SHORT_IDENTIFIER;

    private final URI baseUrl;
    private final BlackboardHttpClient httpClient;
    private final BlackboardSubjectCustomiser subjectCustomiser;
    private final BlackboardXmlParser xmlParser;

    public BlackboardSubjectProvider(URI baseUrl,
                                     BlackboardSubjectCustomiser subjectCustomiser,
                                     BlackboardHttpClient httpClient,
                                     BlackboardXmlParser xmlParser) {
        this.baseUrl = baseUrl;
        this.subjectCustomiser = subjectCustomiser;
        this.httpClient = httpClient;
        this.xmlParser = xmlParser;
    }

    @Override
    public Multimap<ImmutableSemester, ImmutableSubject> getSubjects() throws DLectException {
        try (InputStream listingStream = httpClient.doGet(new URL(baseUrl.toURL(), ENROLLMENTS_LOCATION).toURI())) {
            BlackboardSubjectListing listing = xmlParser.parseSubjectListing(listingStream);

            Multimap<ImmutableSemester, ImmutableSubject> loading = HashMultimap.create();

            for (BlackboardSubject bs : listing.getAllSubjects()) {
                Optional<String> opName = subjectCustomiser.getSubjectName(bs.getName(), bs.getCourseId(), bs.getBbid());

                if (opName.isPresent()) {
                    String name = opName.get();

                    ImmutableSemester semester = subjectCustomiser.getSemesterFor(bs.getName(), bs.getCourseId(), bs.getBbid(), bs.getEnrollmentDate());

                    ImmutableSubject s = new ImmutableSubject(bs.getBbid(), name, EMPTY_LECTURE_LIST, EMPTY_STREAM_LIST);

                    loading.put(semester, s);
                }
            }
            return loading;
        } catch (IOException ex) {
            ProviderLogger.LOGGER.error("Failed to load subjects", ex);
            throw new DLectException(DLectExceptionCause.NO_CONNECTION, ex);
        } catch (URISyntaxException ex) {
            ProviderLogger.LOGGER.error("Failed to create the URI", ex);
            throw new DLectException(DLectExceptionCause.BAD_CREDENTIALS, ex);
        }
    }

}
