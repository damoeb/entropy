package org.migor.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Thread.
 */
@Entity
@Table(name = "T_THREAD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Thread implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Size(min = 1, max = 512)
    @Column(name = "uri")
    private String uri;

    @Size(min = 0, max = 128)
    @Column(name = "title")
    private String title;

    @Transient
    private String description;

    @CreatedDate
    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "created_date")
    private DateTime createdDate = DateTime.now();

    @LastModifiedDate
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "last_modified_date")
    private DateTime lastModifiedDate = DateTime.now();

    @Column(name = "comment_count")
    private Integer commentCount = 0;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "creator_id", insertable = false, updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private User creator;

    @Column(name = "creator_id")
    private String creatorId;

    /**
     * Sum of all comment likes plus thread likes
     */
    @Column(name = "likes")
    private Integer likes = 0;

    /**
     * Sum of all comment dislikes plus thread dislikes
     */
    @Column(name = "dislikes")
    private Integer dislikes = 0;

    /**
     * Disable comments
     */
    @Column(name = "read_only")
    private Boolean readOnly = false; // todo implement readonly

    /**
     * Submissions score: likes - dislikes via reddit
     */
//    @Column(name = "score")
//    private Integer score;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public DateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(DateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate) {
        this.createdDate = createdDate;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Thread thread = (Thread) o;

        if (id != thread.id) {
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
        return "Thread{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", title='" + title + '\'' +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", commentCount=" + commentCount +
                '}';
    }

    /**
     * Created by damoeb on 7/28/14.
     */
    public static enum Status {
        OPEN, CLOSED
    }
}
