package com.example.demo.repository;

import com.example.demo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Basic queries
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Time-based queries
    List<User> findByCreatedAtAfter(LocalDateTime date);
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Number-based queries
    Optional<User> findByUniqueNumber(Long uniqueNumber);
    boolean existsByUniqueNumber(Long uniqueNumber);

    // Custom JPQL queries
    @Query("SELECT u FROM User u WHERE u.uniqueNumber > :number ORDER BY u.createdAt DESC")
    List<User> findUsersWithNumberGreaterThan(@Param("number") Long number);



    // Native SQL queries
    @Query(value = "SELECT * FROM users WHERE username LIKE %:pattern%", nativeQuery = true)
    List<User> findByUsernamePattern(@Param("pattern") String pattern);

    @Query(value = "SELECT * FROM users WHERE LOWER(email) LIKE LOWER(CONCAT('%', :domain, '%'))",
            nativeQuery = true)
    List<User> findByEmailDomain(@Param("domain") String domain);

    // Pagination queries

    Page<User> findByCreatedAtAfter(LocalDateTime date, Pageable pageable);

    // Count queries

    Optional<User> findTopByOrderByUniqueNumberDesc();
    // Bulk update query


}