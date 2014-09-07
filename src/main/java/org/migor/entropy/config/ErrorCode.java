package org.migor.entropy.config;

/**
 * Created by damoeb on 9/5/14.
 */
public enum ErrorCode {

    BANNED(1, "you are banned"),
    REPUTATION(2, "reputation is too low"),
    FREQUENCY(3, "use is time limited (temporary lock)"),
    RESOURCE_NOT_FOUND(4, "does not exist"),
    INVALID_STATUS(5, "status is invalid"),
    ALREADY_EXISTS(6, "already exists"),
    INVALID_DATA(7, "invalid data");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
