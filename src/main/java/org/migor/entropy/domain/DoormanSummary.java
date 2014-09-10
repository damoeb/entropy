package org.migor.entropy.domain;

/**
 * Wrapper for DoormanException
 * <p/>
 * Created by damoeb on 9/5/14.
 */
public class DoormanSummary {
    private int errorCode;
    private String message;

    public DoormanSummary(DoormanException e) {
        this.errorCode = e.getErrorCode().getCode();
        this.message = e.getMessage();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
