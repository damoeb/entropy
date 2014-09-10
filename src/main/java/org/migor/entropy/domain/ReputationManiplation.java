package org.migor.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by damoeb on 9/8/14.
 */
@MappedSuperclass
public class ReputationManiplation {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    protected User user;

    @NotNull
    @Column(name = "user_id")
    protected String userId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "comment_id", updatable = false, insertable = false)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    protected Comment comment;

    @NotNull
    @Column(name = "comment_id")
    protected Long commentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "report_id", updatable = false, insertable = false)
    @org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    protected Report report;

    @Column(name = "report_id")
    protected Long reportId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Comment getComment() {
        return comment;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Report getReport() {
        return report;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }
}
