package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.migor.entropy.domain.Comment;
import org.migor.entropy.domain.Report;
import org.migor.entropy.domain.ReportStatus;
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
import javax.servlet.http.HttpServletResponse;

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

    @Inject
    private CommentResource commentResource;

    /**
     * POST  /rest/reports -> Create a new report.
     */
    @RequestMapping(value = "/rest/reports",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Report report) {
        log.debug("REST request to save Report : {}", report);
        Report existing = reportRepository.findByCommentIdAndClientId(report.getCommentId(), SecurityUtils.getCurrentLogin());
        if (existing != null) {
            // multiple reports of user for same comment
            return;
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new IllegalArgumentException("Comment does not exist");
        }

        report.setClientId(SecurityUtils.getCurrentLogin());
        report.setStatus(ReportStatus.PENDING);
        report.setThreadId(comment.getThreadId());
        report.setStage(comment.getReportStage());

        reportRepository.save(report);
    }

    /**
     * POST  /rest/reports/{id}/approve -> Create a new report.
     */
    @RequestMapping(value = "/rest/reports/{id}/approve",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Report> approve(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to approve Report : {}", id);

        Report report = reportRepository.findOne(id);
        if (report == null) {
            // does not exist
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        if (report.getStatus() != ReportStatus.PENDING) {
            // Invalid status
            return new ResponseEntity<>(report, HttpStatus.EXPECTATION_FAILED);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            return new ResponseEntity<>(report, HttpStatus.EXPECTATION_FAILED);
        }

        if (comment.getReportStage().equals(report.getStage())) {
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
     * POST  /rest/reports/{id}/reject -> Create a new report.
     */
    @RequestMapping(value = "/rest/reports/{id}/reject",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Report> reject(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to reject Report : {}", id);

        Report report = reportRepository.findOne(id);
        if (report == null) {
            // does not exist
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }

        if (report.getStatus() != ReportStatus.PENDING) {
            // Invalid status
            return new ResponseEntity<>(report, HttpStatus.EXPECTATION_FAILED);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            return new ResponseEntity<>(report, HttpStatus.EXPECTATION_FAILED);
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
