package org.migor.entropy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A Privilege.
 */
@Entity
@Table(name = "T_PRIVILEGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Privilege implements Serializable {

    @Id
    @Column(name = "name", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private PrivilegeName name;

    @Column(name = "reputation", nullable = false)
    private int reputation = 0;

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
