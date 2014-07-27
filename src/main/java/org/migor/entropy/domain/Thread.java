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
import java.util.Set;

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

    @Size(min = 1, max = 128)
    @Column(name = "title")
    private String title;

    @Size(min = 1, max = 512)
    @Column(name = "description")
    private String description;

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

    @Column(name = "comment_count")
    private int commentCount;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "T_THREAD_MODERATOR",
            joinColumns = {@JoinColumn(name = "thread_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "login", referencedColumnName = "login")})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<User> moderators;

    @Column(name = "likes")
    private int likes;

    @Column(name = "dislikes")
    private int dislikes;

    /**
     * Submissions score: likes - dislikes via reddit
     */
    @Column(name = "score")
    private int score;

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

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public Set<User> getModerators() {
        return moderators;
    }

    public void setModerators(Set<User> moderators) {
        this.moderators = moderators;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @PrePersist
    public void prePersist() {
        if (created == null) {
            created = new LocalDate();
        }
        modified = new LocalDate();
    }

    @Override
    public String toString() {
        return "Thread{" +
                "id=" + id +
                ", uri='" + uri + '\'' +
                ", title='" + title + '\'' +
                ", created=" + created +
                ", modified=" + modified +
                ", commentCount=" + commentCount +
                '}';
    }
}
