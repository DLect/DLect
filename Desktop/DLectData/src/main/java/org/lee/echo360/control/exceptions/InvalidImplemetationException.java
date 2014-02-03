/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.control.exceptions;

/**
 *
 * @author Lee Symes
 */
public class InvalidImplemetationException extends Exception {

    public InvalidImplemetationException() {
    }

    public InvalidImplemetationException(String message) {
        super(message);
    }

    public InvalidImplemetationException(Throwable cause) {
        super(cause);
    }

    public InvalidImplemetationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
