/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl.echo;

import com.google.common.collect.Lists;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.dlect.helper.Conditions;
import org.dlect.helpers.FileDebuggingHelper;
import org.dlect.logging.ProviderLogger;
import org.dlect.model.helper.ThreadLocalDateFormat;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.httpclient.HttpResponceStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static org.dlect.provider.base.blackboard.lecture.plugin.impl.echo.EchoCenterHelper.*;

/**
 *
 * @author lee
 */
public class EchoCenterJsonParser {

    private static final ThreadLocalDateFormat START_TIME_DATE_FORMAT = new ThreadLocalDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private final URI baseURI;
    private final String sectionId;
    private final BlackboardHttpClient httpClient;

    public EchoCenterJsonParser(BlackboardHttpClient httpClient, URI baseURI, String sectionId) {
        Conditions.checkNonNull(httpClient, "httpClient");
        Conditions.checkNonNull(baseURI, "baseURI");
        Conditions.checkNonNull(sectionId, "sectionId");
        this.baseURI = baseURI;
        this.sectionId = sectionId;
        this.httpClient = httpClient;
    }

    protected Collection<EchoCenterLecture> getLecturesFromPresentation(JSONObject p) {
        JSONArray arr = p.optJSONArray("pageContents");
        List<EchoCenterLecture> lectures = Lists.newArrayList();
        if (arr == null) {
            return lectures;
        }

        for (int i = 0; i < arr.length(); i++) {
            JSONObject lectureItem = arr.getJSONObject(i);

            String uuid = lectureItem.getString("uuid");
            String title = lectureItem.getString("title");
            String baseUrl = lectureItem.getString("richMedia");
            String startTimeString = lectureItem.getString("startTime");
            try {
                Date startTime = START_TIME_DATE_FORMAT.parse(startTimeString);

                lectures.add(new EchoCenterLecture(uuid, title, startTime, baseUrl));
            } catch (ParseException ex) {
                ProviderLogger.LOGGER.error("Failed to parse the start time: `" + startTimeString + "`", ex);
            }
        }
        return lectures;
    }

    public List<EchoCenterLecture> parseSectionJSON() {

        // Change pageSize to 50 in production. It will handle most course's lectures in a single request.
        int pageSize = 2;
        int pageNo = 1;
        int totalResults;
        int detectedResults;
        List<EchoCenterLecture> lectures = Lists.newArrayList();
        try {
            do {
                JSONObject presentations = getPresentationObject(pageSize, pageNo);

                totalResults = presentations.getInt("totalResults");

                lectures.addAll(getLecturesFromPresentation(presentations));

                detectedResults = pageSize * pageNo;
                pageNo++;
            } while (totalResults >= detectedResults);
        } catch (IOException | URISyntaxException ex) {
            ProviderLogger.LOGGER.error("Failed to load page number " + pageNo + "; or size " + pageSize, ex);
        }
        return lectures;
    }

    protected JSONObject getPresentationObject(int pageSize, int pageNumber) throws IOException, URISyntaxException {

        URI jsonURI = toUri(baseURI, "/ess/client/api/sections/" + sectionId
                                     + "/section-data.json?pageIndex=" + pageNumber + "&pageSize=" + pageSize);

        System.out.println("Request to: " + jsonURI);

        try (HttpResponceStream jsonResponse = httpClient.doGet(jsonURI)) {
            String json = getStringStream(jsonResponse);
            try {
                JSONObject obj = new JSONObject(new JSONTokener(json));

                System.out.println(obj.toString(0));

                JSONObject section = obj.optJSONObject("section");
                if (section == null) {
                    // This condition is met if it is not the first page. There is only the presentations object in the json.
                    return obj.getJSONObject("presentations");
                } else {
                    return section.getJSONObject("presentations");
                }
            } catch (JSONException e) {
                FileDebuggingHelper.debugStringToLogger(json, sectionId, ProviderLogger.LOGGER);
                throw e;
            }
        }
    }
}


