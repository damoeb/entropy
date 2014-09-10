package org.migor.entropy.service.impl;

import org.migor.entropy.domain.Comment;
import org.migor.entropy.service.ReputationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReputationServiceImpl implements ReputationService {

    private final Logger log = LoggerFactory.getLogger(ReputationServiceImpl.class);

    @Override
    public void judge(Comment comment) {

        if (comment == null) {
            throw new IllegalArgumentException("comment is null");
        }

        int reputation = 0;

        // todo implement
//        User author = comment.getAuthor();
//        author.getReputation();
//
////        comment.getThread().getHarassmentRules();
//
//        comment.getText();
//        comment.getTitle();

        comment.setReputation(reputation);
    }
}
