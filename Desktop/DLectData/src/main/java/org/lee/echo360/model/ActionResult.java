/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

/**
 *
 * @author lee
 */
public enum ActionResult {

    SUCCEDED,
    NOT_LOGGED_IN,
    FAILED,
    FATAL,
    INVALID_CREDENTIALS,
    NOT_CONNECTED;

    public boolean isSuccess() {
        return this == SUCCEDED;
    }

    public boolean isFatal() {
        return this == FATAL;
    }
}
