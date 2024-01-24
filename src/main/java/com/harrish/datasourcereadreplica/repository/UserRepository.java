package com.harrish.datasourcereadreplica.repository;

import com.harrish.datasourcereadreplica.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
