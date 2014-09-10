package org.migor.entropy.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang.StringUtils;
import org.migor.entropy.config.Constants;
import org.migor.entropy.config.ErrorCode;
import org.migor.entropy.domain.*;
import org.migor.entropy.repository.CommentRepository;
import org.migor.entropy.repository.PunishmentRepository;
import org.migor.entropy.repository.ReportRepository;
import org.migor.entropy.repository.RewardRepository;
import org.migor.entropy.security.SecurityUtils;
import org.migor.entropy.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private Environment env;

    @Inject
    private ReportRepository reportRepository;

    @Inject
    private CommentRepository commentRepository;

    @Inject
    private CommentService commentService;

    @Inject
    private PunishmentRepository punishmentRepository;

    @Inject
    private RewardRepository rewardRepository;

    /**
     * POST  /rest/reports -> Create a new report.
     */
    @RequestMapping(value = "/rest/reports",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.SAVE_REPORT)
    public ResponseEntity<Object> create(@RequestBody Report report, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to save Report : {}", report);
        Report existing = reportRepository.findByCommentIdAndAuthorId(report.getCommentId(), SecurityUtils.getCurrentLogin());
        if (existing != null) {
            // multiple reports of user for same comment
            throw new DoormanException(Report.class, ErrorCode.ALREADY_EXISTS);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        // block reports of own comment
        if (!env.acceptsProfiles(Constants.SPRING_PROFILE_DEVELOPMENT)) {
            if (StringUtils.equals(comment.getAuthorId(), SecurityUtils.getCurrentLogin())) {
                throw new DoormanException(Report.class, ErrorCode.BLOCKED, "You cannot vote your own comment");
            }
        }

        report.setAuthorId(SecurityUtils.getCurrentLogin());
        report.setStatus(Report.Status.PENDING);
        report.setThreadId(comment.getThreadId());
        report.setStage(comment.getReportStage());

        reportRepository.save(report);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * POST  /rest/reports/approve -> Approve a report.
     */
    @RequestMapping(value = "/rest/reports/approve",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.FINALIZE_REPORT)
    // todo remove id
    public ResponseEntity<Report> approve(@RequestBody Report ref, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to approve Report : {}", ref);

        if (ref == null) {
            // not defined
            throw new DoormanException(Report.class, ErrorCode.INVALID_DATA);
        }

        Report report = reportRepository.findOne(ref.getId());
        if (report == null) {
            // does not exist
            throw new DoormanException(Report.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (report.getStatus() != Report.Status.PENDING) {
            // Invalid status
            throw new DoormanException(Report.class, ErrorCode.INVALID_STATUS);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (comment.getReportStage().equals(report.getStage())) {

            log.info(String.format("Punish comment-author %s", comment.getAuthorId()));

            // punish author of comment
            Punishment punishment = new Punishment();
            punishment.setUserId(comment.getAuthorId());
            punishment.setCommentId(comment.getId());
            punishmentRepository.save(punishment);

            commentService.delete(comment.getId());
        }

        log.info(String.format("Reward report-author %s", report.getAuthorId()));

        // reward author of report
        Reward reward = new Reward();
        reward.setUserId(report.getAuthorId());
        reward.setCommentId(comment.getId());
        rewardRepository.save(reward);

        comment.setReportStage(report.getStage() + 1);

        report.setStatus(Report.Status.APPROVED);

        commentRepository.save(comment);
        reportRepository.save(report);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    /**
     * POST  /rest/reports/reject -> Reject a report.
     */
    @RequestMapping(value = "/rest/reports/reject",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Privileged(PrivilegeName.FINALIZE_REPORT)
    // todo remove id
    public ResponseEntity<Report> reject(@RequestBody Report ref, HttpServletRequest request) throws DoormanException {
        log.debug("REST request to reject Report : {}", ref);

        if (ref == null) {
            // not defined
            throw new DoormanException(Report.class, ErrorCode.INVALID_DATA);
        }

        Report report = reportRepository.findOne(ref.getId());
        if (report == null) {
            // does not exist
            throw new DoormanException(Report.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (report.getStatus() != Report.Status.PENDING) {
            // Invalid status
            throw new DoormanException(Report.class, ErrorCode.INVALID_STATUS);
        }

        Comment comment = commentRepository.findOne(report.getCommentId());

        if (comment == null) {
            throw new DoormanException(Comment.class, ErrorCode.RESOURCE_NOT_FOUND);
        }

        if (ref.isAbused()) {
            // punish author of report
            log.info(String.format("Punish report-author %s", report.getAuthorId()));

            Punishment punishment = new Punishment();
            punishment.setUserId(report.getAuthorId());
            punishment.setCommentId(comment.getId());
            punishment.setReportId(report.getId());
            punishmentRepository.save(punishment);
        }

        comment.setReportStage(report.getStage() + 1);

        report.setStatus(Report.Status.REJECTED);

        commentRepository.save(comment);
        reportRepository.save(report);

        return new ResponseEntity<>(report, HttpStatus.OK);
    }

}
