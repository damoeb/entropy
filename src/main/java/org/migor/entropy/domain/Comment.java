package org.migor.entropy.domain;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "thread_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private Thread thread;

    @Column(name = "thread_id", updatable = false, insertable = false)
    private long thread_id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private Comment parent;

    @Column(name = "parent_id", updatable = false, insertable = false)
    private long parent_id;

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

    @Size(min = 1, max = 2048)
    @Column(name = "text")
    private String text;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "author_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private User author;

    @Column(name = "author_id", insertable = false, updatable = false)
    private String authorId;

    @Column(name = "likes")
    private int likes;

    @Column(name = "dislikes")
    private int dislikes;

    @Column(name = "flags")
    private boolean flag;

    @Column(name = "cleared")
    private boolean cleared;

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

    public long getThread_id() {
        return thread_id;
    }

    public void setThread_id(long thread_id) {
        this.thread_id = thread_id;
    }

    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    public long getParent_id() {
        return parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
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
                ", thread_id=" + thread_id +
                ", parent_id=" + parent_id +
                ", created=" + created +
                ", modified=" + modified +
                ", text='" + text + '\'' +
                ", authorId='" + authorId + '\'' +
                '}';
    }
}
