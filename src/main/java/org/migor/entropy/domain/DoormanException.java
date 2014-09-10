package org.migor.entropy.domain;

import org.migor.entropy.config.ErrorCode;

/**
 * verbose exception used for REST calls.
 * <p/>
 * Created by damoeb on 9/5/14.
 */
public class DoormanException extends Exception {
    private final ErrorCode errorCode;

    private DoormanException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public DoormanException(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public DoormanException(Class<?> resource, ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage() + ": " + resource.getSimpleName());
    }

    public DoormanException(Class<Thread> resource, ErrorCode errorCode, String customMessage) {
        this(errorCode, errorCode.getMessage() + ": " + resource.getSimpleName() + ". " + customMessage);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
