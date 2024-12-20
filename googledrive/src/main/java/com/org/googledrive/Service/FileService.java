package com.org.googledrive.Service;

import com.org.googledrive.DTO.FileDTO;
import com.org.googledrive.Repository.FileRepository;
import com.org.googledrive.Repository.UserRepository;
import com.org.googledrive.models.File;
import com.org.googledrive.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileService {

    private final Path BASE_UPLOAD_DIR = Paths.get("src/main/resources/uploads");

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    public List<FileDTO> getFilesForUser(String email){

        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found!");

        }
        User user = userOptional.get();
        List<File> files = fileRepository.findByUser(user);
        // convert file entities to fileDTOs
        List<FileDTO> fileDTOS = new ArrayList<>();
        for(File file:files){
            fileDTOS.add(new FileDTO(
                    file.getId(),file.getFileName(),file.getFileType(),
                    file.getFileSize(),file.getUploadedDate()));

        }
        return fileDTOS;

    }

    public File getFileForUser(Long fileId, String userEmail){

        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found!");
        }
        User user = userOptional.get();

        // Fetch the file by ID
        Optional<File> fileOptional = fileRepository.findById(fileId);
        if(fileOptional.isEmpty()){
            throw new RuntimeException("File not found!");
        }
        File fileDTO = fileOptional.get();

        if(fileDTO.getUser().getId()!=user.getId()){
            throw new RuntimeException("Access denied: File does not belong to the user!");

        }
        return fileDTO;
    }

    public File saveFile(MultipartFile multipartFile, String userEmail) throws IOException{


        // Find the user by email
       Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found!");
        }
        User user = userOptional.get();

        // create a folder for the user if it does not exist
        Path userUploadDir = BASE_UPLOAD_DIR.resolve(user.getId().toString());
        if(!Files.exists(userUploadDir)){
            Files.createDirectories(userUploadDir);
        }

        // save file to local storage
        String fileName = Objects.requireNonNull(multipartFile.getOriginalFilename());
        Path filePath = userUploadDir.resolve(fileName);
        Files.copy(multipartFile.getInputStream(),filePath);

        // save file metadata to the database
        File file = new File();
        file.setFileName(fileName);
        file.setFileType(multipartFile.getContentType());
        file.setFilePath(filePath.toString());
        file.setFileSize(multipartFile.getSize());
        file.setUser(user);
        file.setUploadedDate(new Date());

        return fileRepository.save(file);
    }
}
