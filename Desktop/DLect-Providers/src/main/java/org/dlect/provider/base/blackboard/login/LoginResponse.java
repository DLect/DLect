/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.provider.base.blackboard.login;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lee
 */
@XmlRootElement(name = "mobileresponse")
public class LoginResponse {

    @XmlAttribute(name = "status")
    private String status;

    public LoginResponse() {
    }

    public String getStatus() {
        return status;
    }

}
