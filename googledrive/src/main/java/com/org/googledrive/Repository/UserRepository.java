package com.org.googledrive.Repository;

import com.org.googledrive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmailAndPassword(String email,String Password);

    Optional<User> findByEmail(String email);
}
