/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.providers;

import java.awt.Image;
import org.lee.echo360.model.Blackboard;

/**
 *
 * @author lee
 */
public interface BlackboardProvider {

    public LectureLocator getLectureLocator();

    public LoginExecuter getLoginExecuter();

    public SubjectLocator getSubjectLocator();

    public String getProviderName();

    public Blackboard createBlackboard();

    public Image getProviderImage();

    public LectureDownloader getLectureDownloader();
}
