/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dlect.exception;

/**
 *
 * @author lee
 */
public class DLectException extends Exception {

    private static final long serialVersionUID = 1L;

    private final DLectExceptionCause causeCode;

    public DLectException(DLectExceptionCause causeCode) {
        this.causeCode = causeCode;
    }

    public DLectException(DLectExceptionCause causeCode, String message) {
        super(message);
        this.causeCode = causeCode;
    }

    public DLectException(DLectExceptionCause causeCode, Throwable cause) {
        super(cause);
        this.causeCode = causeCode;
    }

    public DLectException(DLectExceptionCause causeCode, String message, Throwable cause) {
        super(message, cause);
        this.causeCode = causeCode;
    }

    public DLectException(DLectException cause) {
        this(cause.getCauseCode(), cause.getCause());
    }

    public DLectException(String message, DLectException cause) {
        this(cause.getCauseCode(), message, cause.getCause());
    }

    public DLectExceptionCause getCauseCode() {
        return causeCode;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + " (" + causeCode + "): " + message) : s + " (" + causeCode + ")";
    }

}
