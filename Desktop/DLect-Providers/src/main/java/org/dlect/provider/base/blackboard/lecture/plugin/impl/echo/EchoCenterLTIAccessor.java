/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.lecture.plugin.impl.echo;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dlect.logging.ProviderLogger;
import org.dlect.provider.base.blackboard.helper.httpclient.BlackboardHttpClient;
import org.dlect.provider.base.blackboard.helper.httpclient.HttpResponceStream;
import org.dlect.provider.base.blackboard.helper.xml.BlackboardSubjectMapItem;

import static org.dlect.provider.base.blackboard.lecture.plugin.impl.echo.EchoCenterHelper.*;

/**
 *
 * @author lee
 */
public class EchoCenterLTIAccessor {

    private static final Pattern FORM_REGEXP = Pattern.compile("<form.*?</form>", Pattern.DOTALL);
    private static final Pattern FORM_ACTION_REGEXP = Pattern.compile("<form[^<]*?action=['\"](.*?)['\"]", Pattern.DOTALL);
    private static final Pattern FORM_METHOD_REGEXP = Pattern.compile("<form[^<]*?method=['\"](.*?)['\"]", Pattern.DOTALL);
    private static final Pattern FORM_TARGET_REGEXP = Pattern.compile("<form[^<]*?target=['\"](.*?)['\"]", Pattern.DOTALL);

    private static final Pattern INPUT_REGEXP = Pattern.compile("<input.*?>", Pattern.DOTALL);
    private static final Pattern INPUT_NAME_REGEXP = Pattern.compile("<input[^<]*?name=['\"](.*?)['\"]", Pattern.DOTALL);
    private static final Pattern INPUT_VALUE_REGEXP = Pattern.compile("<input[^<]*?value=['\"](.*?)['\"]", Pattern.DOTALL);

    private static final Pattern IFRAME_HREF_REGEXP = Pattern.compile("<iframe[^<]*?src=['\"](.*?)['\"]", Pattern.DOTALL);
    private static final Pattern ECHO_CENTER_SECTION_ID_REGEXP = Pattern.compile("sectionId=([^&]+)", Pattern.DOTALL);
    
    private static final Object ECHO_CENTER_LOCK = new Object();

    private final BlackboardHttpClient httpClient;

    public EchoCenterLTIAccessor(BlackboardHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Nullable
    @CheckForNull
    public EchoCenterJsonParser checkEchoCenter(URI baseUri, BlackboardSubjectMapItem bsmi) {
        ProviderLogger.LOGGER.error("URI: {}", bsmi);
        try {
            URI request = toUri(baseUri, bsmi.getViewUrl());

            try (HttpResponceStream r = httpClient.doGet(request)) {
                return parseNewWindowPage(r);
            }
        } catch (IOException | URISyntaxException ex) {
            ProviderLogger.LOGGER.error("Error", ex);
        }
        return null;
    }

    protected EchoCenterJsonParser parseNewWindowPage(final HttpResponceStream r) throws URISyntaxException, IOException {
        URI location = r.getLocation();

        String page = getStringStream(r);
        String form = matchOrNull(page, EchoCenterLTIAccessor.FORM_REGEXP);
        if (form != null) {
            String action = matchOrNull(form, 1, EchoCenterLTIAccessor.FORM_ACTION_REGEXP);
            String method = matchOrNull(form, 1, EchoCenterLTIAccessor.FORM_METHOD_REGEXP);
            String target = matchOrNull(form, 1, EchoCenterLTIAccessor.FORM_TARGET_REGEXP);

            if ("post".equalsIgnoreCase(method) && "lectur".equalsIgnoreCase(target)) {
                URI u = toUriFromHtml(location, action);

                synchronized (ECHO_CENTER_LOCK) {
                    try (HttpResponceStream ltiPage = httpClient.doPost(u, ImmutableMap.<String, String>of())) {
                        return parseLtiRedirectPage(ltiPage);
                    }
                }
            }
        }
        return null;
    }

    protected EchoCenterJsonParser parseLtiRedirectPage(final HttpResponceStream r) throws URISyntaxException, IOException {
        String page = getStringStream(r);
        String form = matchOrNull(page, EchoCenterLTIAccessor.FORM_REGEXP);
        if (form != null) {
            String action = matchOrNull(form, 1, EchoCenterLTIAccessor.FORM_ACTION_REGEXP);
            String method = matchOrNull(form, 1, EchoCenterLTIAccessor.FORM_METHOD_REGEXP);
            if ("post".equalsIgnoreCase(method)) {
                Map<String, String> formElements = parseFormInputs(form);

                URI u = toUriFromHtml(r.getLocation(), action);

                try (HttpResponceStream echoCenter = httpClient.doPost(u, formElements)) {
                    return loadEchoCenter(echoCenter);
                }
            }
        }
        return null;
    }

    protected EchoCenterJsonParser loadEchoCenter(final HttpResponceStream echoCenter) throws URISyntaxException, IOException, MalformedURLException {
        String page;
        page = getStringStream(echoCenter);
        String iframeHref = matchOrNull(page, 1, EchoCenterLTIAccessor.IFRAME_HREF_REGEXP);
        URI iframe = toUriFromHtml(echoCenter.getLocation(), iframeHref);
        try (HttpResponceStream iFrame = httpClient.doGet(iframe)) {
            page = getStringStream(iFrame);
            iframeHref = matchOrNull(page, 1, EchoCenterLTIAccessor.IFRAME_HREF_REGEXP);

            iframe = toUriFromHtml(iFrame.getLocation(), iframeHref);

            try (HttpResponceStream iFrame2 = httpClient.doGet(iframe)) {
                URI echoCenterURI = iFrame2.getLocation();

                String sectionId = matchOrNull(echoCenterURI.toString(), 1, EchoCenterLTIAccessor.ECHO_CENTER_SECTION_ID_REGEXP);

                return new EchoCenterJsonParser(httpClient, echoCenterURI, sectionId);
            }
        }
    }

    private Map<String, String> parseFormInputs(String form) {
        Matcher m = INPUT_REGEXP.matcher(form);
        Map<String, String> formInputs = Maps.newHashMap();
        while (m.find()) {
            String inputElement = m.group();

            String name = matchOrNull(inputElement, 1, EchoCenterLTIAccessor.INPUT_NAME_REGEXP);
            String value = Strings.nullToEmpty(matchOrNull(inputElement, 1, EchoCenterLTIAccessor.INPUT_VALUE_REGEXP));

            value = StringEscapeUtils.unescapeHtml4(value);

            formInputs.put(name, value);
        }
        return formInputs;
    }

}
