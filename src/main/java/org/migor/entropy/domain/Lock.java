package org.migor.entropy.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Lock.
 */
@Entity
@Table(name = "T_LOCK", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"group_id", "author_id"})
})
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @NotNull
    @Column(name = "group_id")
    private String groupId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    private User author;

    @NotNull
    @Column(name = "author_id")
    private String authorId;

    @NotNull
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    @Column(name = "expiration")
    private DateTime expiration = DateTime.now();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public User getAuthor() {
        return author;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public DateTime getExpiration() {
        return expiration;
    }

    public void setExpiration(DateTime expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Lock lock = (Lock) o;

        if (id != lock.id) {
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
        return "Lock{" +
                "id=" + id +
                ", authorId='" + authorId + '\'' +
                ", expiration=" + expiration +
                '}';
    }
}
