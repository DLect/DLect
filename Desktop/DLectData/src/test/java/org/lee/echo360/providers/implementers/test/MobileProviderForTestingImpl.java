/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers.implementers.test;

import java.awt.Image;
import java.util.Map;
import org.lee.echo360.model.Blackboard;
import org.lee.echo360.providers.BlackboardProvider;
import org.lee.echo360.providers.BlackboardProviderWrapper;
import org.lee.echo360.providers.BlackboardProviders;
import org.lee.echo360.providers.LectureDownloader;
import org.lee.echo360.providers.LectureLocator;
import org.lee.echo360.providers.LoginExecuter;
import org.lee.echo360.providers.SubjectLocator;
import org.lee.echo360.providers.implementers.test.xml.EnrollmentInfo;
import org.lee.echo360.providers.mobile.BlackboardMobileProviderImpl;
import org.lee.echo360.providers.mobile.BlackboardMobileProviderLectureImpl;
import org.lee.echo360.providers.mobile.BlackboardMobileProviderSubjectImpl;
import org.lee.echo360.providers.mobile.MobileXmlProvider;
import org.lee.echo360.test.TestUtilities;

public class MobileProviderForTestingImpl extends MobileProviderLocaliserForTesting implements BlackboardProvider {

    private LectureLocator ll;
    private LoginExecuter le;
    private SubjectLocator sl;
    private BlackboardProviderWrapper bpw;
    private LectureDownloader ld;

    public MobileProviderForTestingImpl(EnrollmentInfo ei, MobileXmlProvider prov) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        super(ei);
        ll = new BlackboardMobileProviderLectureImpl(prov, this);
        le = new BlackboardMobileProviderImpl(prov, this);
        sl = new BlackboardMobileProviderSubjectImpl(prov, this);
        bpw = new BlackboardProviderWrapper(this);
        putThisIntoProviders();
    }

    @Override
    public LectureLocator getLectureLocator() {
        return ll;
    }

    @Override
    public LoginExecuter getLoginExecuter() {
        return le;
    }

    @Override
    public SubjectLocator getSubjectLocator() {
        return sl;
    }

    @Override
    public String getProviderName() {
        return "Test Provider";
    }

    @Override
    public Blackboard createBlackboard() {
        return new Blackboard();
    }

    @Override
    public Image getProviderImage() {
        return null;
    }

    @Override
    public LectureDownloader getLectureDownloader() {
        return ld;
    }

    public void setLectureDownloader(LectureDownloader ld) {
        this.ld = ld;
    }

    public final void putThisIntoProviders() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ((Map) TestUtilities.getStaticField(BlackboardProviders.class, "providers")).put(this.getClass(), bpw);
    }
}
