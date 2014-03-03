/*
 *  Copyright (C) 2013 lee
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.dlect.update.old;

/**
 *
 * @author lee
 */
public final class VersionInformation {

    public static final String APPLICATION_VERSION = "2.1-3";
    public static final String JAVA_VERSION = System.getProperty("java.version", "Unknown");
    public static final String OS_COMPLETE_NAME = System.getProperty("os.name", "Name") + " " + System.getProperty("os.version", "Version") + " (" + System.getProperty("os.arch", "Arch") + ")";
    public static final String OS_TYPE_PARAMETERS = ""
//            + "os-name=" + encode(System.getProperty("os.name", "Name"))
//            + "&os-version=" + encode(System.getProperty("os.version", "Version"))
//            + "&os-arch=" + encode(System.getProperty("os.arch", "Arch"))
//            + "&os=" + encode(OS_COMPLETE_NAME)
    +"";
    public static final String BASE_URL = "http://uqlectures.sourceforge.net";
    /**
     *The URL to check for updates.
     */
    public static final String UPDATE_CHECKING_URL = BASE_URL + "/update.php?thisver="
//                                                     + encode(APPLICATION_VERSION)
     + "";
    /**
     * The URL to register the application's startup.
     * This URL should be formatted using the UUID for this run; The BBID from
     * the Blackboard instance and the time from `main()` to first window.
     */
    private static final String RUN_REGISTRATION_URL = BASE_URL + "/regRun.php?thisver=" 
//                                                       + encode(APPLICATION_VERSION)
          //  + "&javaver=" + encode(JAVA_VERSION)
            + "&uuid=%d&bbid=%d&startup=%d&";

    public static String getRunRegistrationURL(long uuid, long bbid, long startupTime) {
        System.out.println(String.format(RUN_REGISTRATION_URL, uuid, bbid, startupTime) + OS_TYPE_PARAMETERS);
        return String.format(RUN_REGISTRATION_URL, uuid, bbid, startupTime) + OS_TYPE_PARAMETERS;
    }

    public static String getMobileProviderCourseNameErrorURL(String errorName, Class errorClass, String errorLocation) {
        System.out.println(String.format(RUN_REGISTRATION_URL, errorName, errorClass, errorLocation) + OS_TYPE_PARAMETERS);
        return String.format(RUN_REGISTRATION_URL, errorName, errorClass, errorLocation) + OS_TYPE_PARAMETERS;
    }

    private VersionInformation() {
    }
}
