package org.migor.entropy.service;

import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.DoormanException;
import org.migor.entropy.domain.Report;
import org.migor.entropy.domain.Thread;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.ReportRepository;
import org.migor.entropy.repository.ThreadRepository;
import org.migor.entropy.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ThreadService {

    private final Logger log = LoggerFactory.getLogger(ThreadService.class);

    @Inject
    private ThreadRepository threadRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentService commentService;

    @Inject
    private ReportRepository reportRepository;

    public void create(Thread thread) throws DoormanException {

        if (thread == null) {
            throw new IllegalArgumentException("thread is null");
        }

        thread.setStatus(Thread.Status.OPEN);

        thread = threadRepository.save(thread);

        Comment first = new Comment();
        first.setThreadId(thread.getId());
        first.setTitle(thread.getTitle());
        first.setText(thread.getDescription());
        first.setAuthorId(SecurityUtils.getCurrentLogin());
        first.setDisplayName("Anonymous");
        first.setStatus(Comment.Status.APPROVED);

        commentService.create(first);
    }

    public List<Thread> getAll() {
        return threadRepository.findAll();
    }

    public void delete(Long id) {
        threadRepository.delete(id);
    }

    public Map<String, Object> getDetailed(Long id) throws DoormanException {

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Map<String, Object> responseObj = new HashMap<>();

        Thread thread = threadRepository.findOne(id);
        if (thread == null) {
            throw new DoormanException(Thread.class, ErrorCode.RESOURCE_NOT_FOUND);
        }
        responseObj.put("thread", thread);
        responseObj.put("approved", commentRepository.findByThreadIdAndStatus(id, Comment.Status.APPROVED));
        responseObj.put("pendingCount", commentRepository.getCountForThreadIdAndStatus(id, Comment.Status.PENDING));
        responseObj.put("reportCount", commentRepository.getReportCountForThreadIdAndReportStatus(id, Report.Status.PENDING));
        return responseObj;
    }

    public Map<String, Object> getReports(@PathVariable Long id) throws DoormanException {

        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }

        Map<String, Object> responseObj = new HashMap<>();

        Thread thread = threadRepository.findOne(id);
        if (thread == null) {
            throw new DoormanException(Thread.class, ErrorCode.RESOURCE_NOT_FOUND);
        }
        responseObj.put("thread", thread);
        responseObj.put("reports", reportRepository.findByThreadIdAndStatus(id, Report.Status.PENDING));
        responseObj.put("comments", commentRepository.findByThreadIdAndReportStatus(id, Report.Status.PENDING));

        return responseObj;
    }


}
