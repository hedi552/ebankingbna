package com.bna.repository;

import com.bna.domain.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    /**
     * Find all operations where the compte belongs to the currently authenticated user.
     */
    @Query("""
        select o
        from Operation o
        join o.compte c
        join c.user u
        where u.login = ?#{authentication.name}
    """)
    List<Operation> findAllByCurrentUser();
}