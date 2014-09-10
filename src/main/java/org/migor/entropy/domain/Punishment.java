package org.migor.entropy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A Punishment.
 */
@Entity
@Table(name = "T_PUNISHMENT")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Punishment extends ReputationManiplation {

}
