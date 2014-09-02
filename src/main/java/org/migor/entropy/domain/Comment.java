package org.migor.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A Comment.
 */
@Entity
@Table(name = "T_COMMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "thread_id", updatable = false, insertable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private Thread thread;

    @NotNull
    @Column(name = "thread_id")
    private Long threadId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id", updatable = false, insertable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private Comment parent;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "level")
    private Integer level;

    @CreatedDate
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date")
    private DateTime createdDate = DateTime.now();

    @LastModifiedDate
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "last_modified_date")
    private DateTime lastModifiedDate = DateTime.now();

    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "text")
    private String text;

    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "title")
    private String title;

    @Size(min = 1, max = 128)
    @Column(name = "display_name")
    private String displayName;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private User author;

    @NotNull
    @Column(name = "author_id")
    private String authorId;

    @Column(name = "likes")
    private Integer likes = 0;

    @Column(name = "dislikes")
    private Integer dislikes = 0;

    @JsonIgnore
    @Column(name = "report_stage")
    private Integer reportStage = 0;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    /**
     * Submissions score: likes - dislikes via reddit
     */
    @Column(name = "score")
    private int score;

    //    todo 'voters'
//    @ManyToM/*any
//    @JoinTable(name = "comments2voters")
//    private */Set<User> voters;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public Long getThreadId() {
        return threadId;
    }

    public void setThreadId(Long threadId) {
        this.threadId = threadId;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislikes() {
        return dislikes;
    }

    public void setDislikes(Integer dislikes) {
        this.dislikes = dislikes;
    }

    public Integer getReportStage() {
        return reportStage;
    }

    public void setReportStage(Integer reportStage) {
        this.reportStage = reportStage;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Comment comment = (Comment) o;

        if (id != comment.id) {
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
        return "Comment{" +
                "id=" + id +
                ", threadId=" + threadId +
                ", parentId=" + parentId +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", text='" + text + '\'' +
                ", authorId='" + authorId + '\'' +
                '}';
    }
}
