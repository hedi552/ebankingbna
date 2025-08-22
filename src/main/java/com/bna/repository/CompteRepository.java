package com.bna.repository;

import com.bna.domain.Compte;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Compte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    @Query("""
    select compte
    from Compte compte
    join compte.user user
    where user.login = ?#{authentication.name}
""")
List<Compte> findByUserIsCurrentUser();
}