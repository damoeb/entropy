package org.migor.entropy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Privilege describes, that is reputation is required to execute a method.
 *
 * @link http://stackoverflow.com/help/privileges?tab=all
 */
@Entity
@Table(name = "T_PRIVILEGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Privilege implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PrivilegeName name;

    @Column(name = "reputation", nullable = false)
    private int reputation = 0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PrivilegeName getName() {
        return name;
    }

    public void setName(PrivilegeName name) {
        this.name = name;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}
