package com.klef.fsad.studentfeedback.repository;

import com.klef.fsad.studentfeedback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>
{
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(String role);
    List<User> findByRoleAndLastLoginAtIsNotNull(String role);
}
