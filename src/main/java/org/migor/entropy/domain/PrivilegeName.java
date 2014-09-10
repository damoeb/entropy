package org.migor.entropy.domain;

/**
 * Defines privileges that are watched by DoormanAspect.
 * <p/>
 * Created by damoeb on 9/3/14.
 */
public enum PrivilegeName {
    SAVE_THREAD,
    DELETE_THREAD,

    SAVE_COMMENT,
    REPLY,
    DELETE_COMMENT,

    SAVE_BAN,
    DELETE_BAN,

    SAVE_PRIVILEGE,
    DELETE_PRIVILEGE,

    SAVE_REPORT,
    FINALIZE_REPORT,

    SAVE_VOTE,

    DELETE_VOTE,
    DELETE_LOCK

}
