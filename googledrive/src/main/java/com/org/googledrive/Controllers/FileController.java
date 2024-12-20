package com.org.googledrive.Controllers;

import com.org.googledrive.DTO.FileDTO;
import com.org.googledrive.Service.FileService;
import com.org.googledrive.models.File;
import com.org.googledrive.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String,String>> uploadFile(@RequestParam("file") MultipartFile file)  {

        try{

            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userEmail = user.getEmail();

            fileService.saveFile(file,userEmail);
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded successfully!");
            return ResponseEntity.ok(response);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error","File upload failed"));
        }
    }
    @GetMapping("/list")
    public ResponseEntity<?> getAllFiles(){

        try{
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userEmail = user.getEmail();

            List<FileDTO> files = fileService.getFilesForUser(userEmail);
            return ResponseEntity.ok(files);

        }catch (Exception e){
            return ResponseEntity.status(500).body("Error: "+e.getMessage());
        }

    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Long fileId){

        try{

            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String userEmail = user.getEmail();
            // fetch the file from service
            File fileDTO = fileService.getFileForUser(fileId, userEmail);

            // Read file content
            Path filePath = Paths.get(fileDTO.getFilePath());
            byte[] fileData = Files.readAllBytes(filePath);

            // Prepare response
            return ResponseEntity.ok().header("Content-Disposition","attachment;" +
                    "fileName=/" + fileDTO.getFileName() + "/").body(fileData);

        }
        catch (Exception e){
             return ResponseEntity.status(500).body("Error : " + e.getMessage());
        }
    }
}
