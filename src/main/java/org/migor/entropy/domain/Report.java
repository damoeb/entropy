package org.migor.entropy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A Report.
 */
@Entity
@Table(name = "T_REPORT", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"client_id", "comment_id"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// todo extends AbstractAuditingEntity
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    /**
     * browser signature or login
     */
    // todo fk to user
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "client_id")
    private String clientId;

    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "reason")
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ReportStatus status;

    @NotNull
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "abused")
    private boolean abused;

    @NotNull
    @Column(name = "thread_id")
    private Long threadId;

    @NotNull
    @Column(name = "stage")
    private Integer stage;

    @CreatedDate
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date")
    private DateTime createdDate = DateTime.now();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Integer getStage() {
        return stage;
    }

    public void setStage(Integer stage) {
        this.stage = stage;
    }

    public boolean isAbused() {
        return abused;
    }

    public void setAbused(boolean abused) {
        this.abused = abused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Report report = (Report) o;

        if (id != report.id) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Report{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", commentId=" + commentId +
                ", createdDate=" + createdDate +
                '}';
    }
}
