package com.swp.server.repository;

import java.util.List;
import java.util.Optional;

import com.swp.server.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.swp.server.entities.Account;

@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {
	Optional<Account> findFirstByEmail(String email);

	Optional<Account> findFirstByEmailAndOtpCode(String email, String otpCode);

	Optional<Account> findByUsername(String username);

	Optional<Account> findFirstByUsername(String username);

	List<Account> findBy();
	List<Account> findByEnabledTrue();
	List<Account> findByEnabledFalse();
	@Query("SELECT a FROM Account a WHERE a.username LIKE %:txtSearch% ORDER BY a.username ASC")
	List<Account> findAndOrderByUsername(@Param("txtSearch") String txtSearch);

	@Query("SELECT a FROM Account a WHERE a.role = :role ORDER BY a.username")
	List<Account> findByRoleAndOrderByUsername(@Param("role") Role role);

	@Query("SELECT a FROM Account a WHERE a.username LIKE %:txtSearch% AND a.role = :role ORDER BY a.username ASC")
	List<Account> findByUsernameContainingAndRoleAndOrderByUsername(@Param("txtSearch") String txtSearch, @Param("role") Role role);

}
