package dev.sijun.imagehost;


import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final FileStorageService fileStorageService;

    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String filename = fileStorageService.saveFile(file);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/images/view/")
                .path(filename)
                .toUriString();
        return ResponseEntity.ok(url);
    }

    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> viewImage(@PathVariable String filename) throws MalformedURLException {
        Resource file = fileStorageService.loadFile(filename);
        String contentType = "image/jpeg"; // 필요시 content-type 자동 판별 가능
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}