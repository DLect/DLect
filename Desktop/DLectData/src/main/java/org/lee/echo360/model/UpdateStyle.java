/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

/**
 *
 * @author lee
 */
public enum UpdateStyle {

    COMPLETELY_AUTOMATIC("-a"), USER_NOTIFIED("-n");
    private final String code;

    private UpdateStyle(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
