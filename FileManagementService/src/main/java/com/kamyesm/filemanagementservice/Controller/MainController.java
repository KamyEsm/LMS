package com.kamyesm.filemanagementservice.Controller;

import com.kamyesm.filemanagementservice.DTO.FileDownloadResponse;
import com.kamyesm.filemanagementservice.Service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final StorageService service;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file , @RequestParam("userId") String userId) throws Exception {
        return ResponseEntity.ok().body(service.upload(file,userId));
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(
            @PathVariable String fileName,
            @RequestParam String userId) throws Exception {

        FileDownloadResponse response = service.download(fileName, userId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + response.getFileName() + "\"")
                .body(response.getResource());
    }

    @DeleteMapping("/delete/{fileName}")
    public ResponseEntity<?> deleteFile(@PathVariable String fileName , @RequestParam String UserId){
        return ResponseEntity.ok().body(service.delete(fileName,UserId));
    }



}
