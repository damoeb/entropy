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
@Table(name = "T_REPORT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
// todo unique constraint commentId, clientId, threadId
// todo extends AbstractAuditingEntity
public class Report implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    /**
     * browser signature or login
     */
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

    // todo add constraint

    @NotNull
    @Column(name = "comment_id")
    private Long commentId;

    @NotNull
    @Column(name = "thread_id")
    private Long threadId;

    @NotNull
    @Column(name = "level")
    private Integer level;

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
