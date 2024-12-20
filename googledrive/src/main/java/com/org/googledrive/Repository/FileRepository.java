package com.org.googledrive.Repository;

import com.org.googledrive.models.File;
import com.org.googledrive.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File,Long> {

    List<File> findByUser(User user);
}
