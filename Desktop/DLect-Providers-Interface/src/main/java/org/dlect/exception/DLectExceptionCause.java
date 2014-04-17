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
public enum DLectExceptionCause {

    NO_CONNECTION,
    BAD_CREDENTIALS,
    @Deprecated
    INVALID_DATA_FORMAT,
    ILLEGAL_SERVICE_RESPONCE,
    ILLEGAL_PROVIDER_STATE,
    DISK_ERROR,
    PROVIDER_CONTRACT,
    UNCAUGHT_UNKNOWN_EXCEPTION;
}
