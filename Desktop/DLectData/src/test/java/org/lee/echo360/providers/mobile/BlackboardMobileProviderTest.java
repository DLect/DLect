/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lee.echo360.model.ProviderInformation;
import org.lee.echo360.test.TestUtilities;
import org.mockito.Mockito;

/**
 *
 * @author lee
 */
public class BlackboardMobileProviderTest {

    public static final ProviderInformation UQ_PROVIDER_OBJECT = new ProviderInformation(
            "The University of Queensland",
            "https://learn.uq.edu.au/webapps/Bb-mobile-BBLEARN/",
            921, false, true);

    // BlackboardMobileProvider prov;
    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        Assume.assumeTrue("Not Connected", TestUtilities.isInternetReachable());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testParseProviderFromStreamValid() throws IOException {
        ProviderInformation parse = BlackboardMobileProvider.parseProviderFromStream(ProviderXML.SUPPORTED_PROVIDER.getStream());
        assertNotNull(parse);
        assertEquals("The names are different", "The University of Queensland", parse.getName());
    }

    @Test
    public void testCreateProviderValid() throws IOException {
        URL u = new URL(BlackboardMobileProvider.DATA_REFRESH_PRE_CLIENT_ID + 921 + BlackboardMobileProvider.DATA_REFRESH_POST_CLIENT_ID);
        String s = IOUtils.toString(u.openStream());
        Assume.assumeTrue("The provider has regressed", s.contains("<data"));
        boolean supported = BlackboardMobileProvider.checkClientIdSupported(921);
        assertTrue("Provider is not supported", supported);
    }

    /**
     * Test of checkClientIdSupported method, of class BlackboardMobileProvider.
     */
    @Test
    public void testCheckClientIdSupportedWithoutSupport() {
        int clientId = 0;
        boolean expResult = false;
        boolean result = BlackboardMobileProvider.checkClientIdSupported(clientId);
        assertEquals(expResult, result);
    }

    /**
     * Test of createProvider method, of class BlackboardMobileProvider.
     */
    @Test
    public void testCreateInvalidProvider() {
        MobileProviderLocaliser mpl = Mockito.mock(MobileProviderLocaliser.class);
        int i = -100;
        BlackboardMobileProvider result = BlackboardMobileProvider.createProvider(i, mpl);
        Mockito.verifyZeroInteractions(mpl);
        assertNull(result);
    }

    /**
     * Test of createProvider method, of class BlackboardMobileProvider.
     */
    @Test
    public void testCreateValidProvider() throws IOException {
        URL u = new URL(BlackboardMobileProvider.DATA_REFRESH_PRE_CLIENT_ID + 921 + BlackboardMobileProvider.DATA_REFRESH_POST_CLIENT_ID);
        String s = IOUtils.toString(u.openStream());
        Assume.assumeTrue("The provider has regressed", s.contains("<data"));
        MobileProviderLocaliser mpl = Mockito.mock(MobileProviderLocaliser.class);
        int i = 921;
        BlackboardMobileProvider result = BlackboardMobileProvider.createProvider(i, mpl);
        assertNotNull("Returned null Mobile Provider", result);
        Mockito.verifyZeroInteractions(mpl);
    }

    /**
     * Test of parseProviderFromStream method, of class
     * BlackboardMobileProvider.
     */
    @Test
    public void testParseProviderFromStream() throws Exception {
        InputStream stream = ProviderXML.SUPPORTED_PROVIDER.getStream();
        ProviderInformation result = BlackboardMobileProvider.parseProviderFromStream(stream);
        assertEquals("", UQ_PROVIDER_OBJECT, result);
    }

    /**
     * Test of parseProviderFromStream method, of class
     * BlackboardMobileProvider.
     */
    @Test
    public void testParseNonProviderFromStream() throws Exception {
        InputStream stream = ProviderXML.NOT_SUPPORTED_PROVIDER.getStream();
        ProviderInformation result = BlackboardMobileProvider.parseProviderFromStream(stream);
        assertNull(result);
    }

    /**
     * Test of parseProviderFromStream method, of class
     * BlackboardMobileProvider.
     */
    @Test
    public void testParseInvalidProviderFromStream() throws Exception {
        InputStream stream = ProviderXML.INVALID_PROVIDER.getStream();
        ProviderInformation result = BlackboardMobileProvider.parseProviderFromStream(stream);
        assertNull(result);
    }

    private enum ProviderXML {

        SUPPORTED_PROVIDER("<s>\n"
        + "<name>The University of Queensland</name>\n"
        + "<id>17649</id>\n"
        + "<b2_url>https://learn.uq.edu.au/webapps/Bb-mobile-BBLEARN/</b2_url>\n"
        + "<has_community_system>1</has_community_system>\n"
        + "<username_label>Username</username_label>\n"
        + "<has_mobile_central>0</has_mobile_central>\n"
        + "<http_auth>0</http_auth>\n"
        + "<from_people_soft>1</from_people_soft>\n"
        + "<client_id>921</client_id>\n"
        + "<can_has_ssl_login>true</can_has_ssl_login>\n"
        + "<access>\n"
        + "<wifi>1</wifi>\n"
        + "<carrier>1</carrier>\n"
        + "<sprint_local>0</sprint_local>\n"
        + "</access>\n"
        + "<people_soft_institution_id>149017</people_soft_institution_id>\n"
        + "<euse>help@its.uq.edu.au</euse>\n"
        + "<euse_label>ITS HelpDesk</euse_label>\n"
        + "</s>"),
        NOT_SUPPORTED_PROVIDER("<?xml version=\"1.0\" encoding=\"UTF-8\"?><data>"
        + "</data>"),
        INVALID_PROVIDER("THIS IS INVALID XML. NOT A VALID <hello><world></hello>"
        + "</world>");
        final String xml;

        private ProviderXML(String xml) {
            this.xml = xml;
        }

        public InputStream getStream() {
            return IOUtils.toInputStream(xml);
        }
    }
}
