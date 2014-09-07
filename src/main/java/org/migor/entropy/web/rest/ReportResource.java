package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.*;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.ReportRepository;
import org.migor.entropy.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * REST controller for managing Report.
 */
@RestController
@RequestMapping("/app")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private CommentRepository commentRepository;

    /**
     * POST  /rest/reports -> Create a new report.
     */
    @RequestMapping(value = "/rest/reports",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Object> create(@RequestBody Report report, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to save Report : {}", report);
        Report existing = reportRepository.findByCommentIdAndClientId(report.getCommentId(), SecurityUtils.getCurrentLogin());
        if (existing != null) {
            // multiple reports of user for same comment
            throw new DoormanException(Report.class, ErrorCode.ALREADY_EXISTS);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        report.setClientId(SecurityUtils.getCurrentLogin());
        report.setStatus(ReportStatus.PENDING);
        report.setThreadId(comment.getThreadId());
        report.setStage(comment.getReportStage());

        reportRepository.save(report);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST  /rest/reports/{id}/approve -> Approve a report.
     */
    @RequestMapping(value = "/rest/reports/{id}/approve",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.FINALIZE_REPORT)
    public ResponseEntity<Report> approve(@PathVariable Long id, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to approve Report : {}", id);

        Report report = reportRepository.findOne(id);
        if (report == null) {
            // does not exist
            throw new DoormanException(Report.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (report.getStatus() != ReportStatus.PENDING) {
            // Invalid status
            throw new DoormanException(Report.class, ErrorCode.INVALID_STATUS);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (comment.getReportStage().equals(report.getStage())) {
//            User author = comment.getAuthor();
//            author.setPunishmentCount(author.getPunishmentCount() + 1);
//            author.getPunishments().add();
            // todo punish comment author
            // todo delete comment
//            comment.setStatus(CommentStatus.DELETED);
        }

        comment.setReportStage(report.getStage() + 1);

        report.setStatus(ReportStatus.APPROVED);

        commentRepository.save(comment);
        reportRepository.save(report);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    /**
     * POST  /rest/reports/{id}/reject -> Reject a report.
     */
    @RequestMapping(value = "/rest/reports/{id}/reject",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.FINALIZE_REPORT)
    public ResponseEntity<Report> reject(@PathVariable Long id, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to reject Report : {}", id);

        Report report = reportRepository.findOne(id);
        if (report == null) {
            // does not exist
            throw new DoormanException(Report.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (report.getStatus() != ReportStatus.PENDING) {
            // Invalid status
            throw new DoormanException(Report.class, ErrorCode.INVALID_STATUS);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (comment.getReportStage().equals(report.getStage()) && report.isAbused()) {
            // todo punish reporter for abuse
        }

        comment.setReportStage(report.getStage() + 1);

        report.setStatus(ReportStatus.REJECTED);

        commentRepository.save(comment);
        reportRepository.save(report);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
