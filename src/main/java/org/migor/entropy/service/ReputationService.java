package org.migor.entropy.service;

import org.migor.entropy.domain.Comment;

public interface ReputationService {

    void judge(Comment comment);
}
