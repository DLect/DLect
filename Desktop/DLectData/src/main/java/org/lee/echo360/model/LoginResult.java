/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lee.echo360.model;

import org.lee.echo360.util.I18N;

/**
 *
 * @author lee
 */
public class LoginResult {

    public static final int FATAL = 10;
    public static final int SEVERE = 5;
    public static final int NOT_CONNECTED = 3;
    private static final LoginResult success = new LoginResult(ResultType.SUCCESS, null);
    private static final LoginResult badCred = new LoginResult(ResultType.BAD_CREDENTIALS, "BAD_LOGIN");

    public static LoginResult getSuccess() {
        return success;
    }

    public static LoginResult threwError(int type) {
        if (type == NOT_CONNECTED) {
            return new LoginResult(ResultType.UNKNOWN_ERROR, "NO_CONNECTION", type);
        } else if (type == FATAL) {
            return new LoginResult(ResultType.UNKNOWN_ERROR, "FATAL", type);
        } else {
            return new LoginResult(ResultType.UNKNOWN_ERROR, "SEVERE_LOGIN", type);
        }

    }

    public static ActionResult convert(LoginResult r) {
        if (r == null) {
            return ActionResult.FATAL;
        }
        switch (r.getResultType()) {
            case BAD_CREDENTIALS:
                return ActionResult.INVALID_CREDENTIALS;
            case SUCCESS:
                return ActionResult.SUCCEDED;
            case UNKNOWN_ERROR:
                switch (r.getErrorSeverity()) {
                    case FATAL:
                        return ActionResult.FATAL;
                    case NOT_CONNECTED:
                        return ActionResult.NOT_CONNECTED;
                    case SEVERE:
                        return ActionResult.FAILED;
                }
            default:
                return ActionResult.FATAL;
        }
    }

    public static LoginResult incorrectLoginDetails() {
        return badCred;
    }
    private final ResultType result;
    private final String prefix;
    private final int errorSeverity;

    private LoginResult(ResultType result, String description) {
        this(result, description, 0);
    }

    private LoginResult(ResultType result, String description, int errorSeverity) {
        this.result = result;
        this.prefix = description;
        this.errorSeverity = errorSeverity;
    }

    public ResultType getResultType() {
        return result;
    }

    public boolean isSuccess() {
        return result.isSuccess();
    }

    public int getErrorSeverity() {
        return errorSeverity;
    }

    public String getDescription() {
        return I18N.getString(prefix + "_DESC");
    }

    @Override
    public String toString() {
        return "LoginResult{" + "result=" + result + ", prefix=" + prefix + ", errorSeverity=" + errorSeverity + ", desc=" + getDescription() + '}';
    }

    public boolean isFatal() {
        return (!this.isSuccess()) && this.errorSeverity == LoginResult.FATAL;
    }

    public String getDescriptionPrefix() {
        return prefix;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.result != null ? this.result.hashCode() : 0);
        hash = 31 * hash + this.errorSeverity;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoginResult other = (LoginResult) obj;
        if (this.result != other.result) {
            return false;
        }
        if (this.errorSeverity != other.errorSeverity) {
            return false;
        }
        return true;
    }

    public static enum ResultType {

        SUCCESS(true),
        BAD_CREDENTIALS(false),
        UNKNOWN_ERROR(false);
        private final boolean success;

        private ResultType(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }
    }
}
