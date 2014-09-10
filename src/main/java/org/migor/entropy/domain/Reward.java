package org.migor.entropy.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A Reward.
 */
@Entity
@Table(name = "T_REWARD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Reward extends ReputationManiplation {

}
