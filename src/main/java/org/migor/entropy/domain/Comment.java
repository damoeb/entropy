package org.migor.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.joda.deser.LocalDateDeserializer;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.migor.entropy.domain.util.CustomLocalDateSerializer;

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

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "created")
    private LocalDate created;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @Column(name = "modified")
    private LocalDate modified;

    @NotNull
    @Size(min = 1, max = 2048)
    @Column(name = "text")
    private String text;

    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "subject")
    private String subject;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private User author;

    @NotNull
    @Column(name = "author_id")
    private String authorId;

    @Column(name = "likes")
    private int likes;

    @Column(name = "dislikes")
    private int dislikes;

    @Column(name = "complains")
    private int complains;

    @NotNull
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CommentStatus status;

    /**
     * Submissions score: likes - dislikes via reddit
     */
//    @Column(name = "score")
//    private int score;


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

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getComplains() {
        return complains;
    }

    public void setComplains(int complains) {
        this.complains = complains;
    }

    public CommentStatus getStatus() {
        return status;
    }

    public void setStatus(CommentStatus status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = new LocalDate();
        }
        modified = new LocalDate();
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", threadId=" + threadId +
                ", parentId=" + parentId +
                ", created=" + created +
                ", modified=" + modified +
                ", text='" + text + '\'' +
                ", authorId='" + authorId + '\'' +
                '}';
    }
}
