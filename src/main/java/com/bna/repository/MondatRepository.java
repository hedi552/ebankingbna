package com.bna.repository;

import com.bna.domain.Mondat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA repository for the Mondat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MondatRepository extends JpaRepository<Mondat, Long> {
     @Query("""
        select o
        from Mondat o
        join o.compte c
        join c.user u
        where u.login = ?#{authentication.name}
    """)
    List<Mondat> findAllByCurrentUser();
}