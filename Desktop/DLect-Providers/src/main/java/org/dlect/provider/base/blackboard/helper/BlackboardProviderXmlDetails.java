/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.helper;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "s")
public class BlackboardProviderXmlDetails {

    @XmlElement(name = "b2_url")
    private String b2_url;

    @XmlElement(name = "http_auth")
    private String httpAuth;

    @XmlElement(name = "can_has_ssl_login")
    private String sslLogin;

    public String getB2Url() {
        return b2_url;
    }

    public boolean isHttpAuth() {
        return toBoolean(httpAuth);
    }

    public boolean isSslLogin() {
        return toBoolean(sslLogin);
    }

    protected static boolean toBoolean(String s) {
        return "true".equalsIgnoreCase(s) || "1".equals(s);
    }

}
