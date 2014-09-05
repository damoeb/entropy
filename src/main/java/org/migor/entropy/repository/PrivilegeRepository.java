package org.migor.entropy.repository;

import org.migor.entropy.domain.Privilege;
import org.migor.entropy.domain.PrivilegeName;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Privilege entity.
 */
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(PrivilegeName privilegeName);
}
