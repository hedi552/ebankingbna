package com.bna.repository;

import com.bna.domain.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
/**
 * Spring Data JPA repository for the Virement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VirementRepository extends JpaRepository<Virement, Long> {
     @Query("""
        select o
        from Virement o
        join o.compte c
        join c.user u
        where u.login = ?#{authentication.name}
    """)
    List<Virement> findAllByCurrentUser();
}