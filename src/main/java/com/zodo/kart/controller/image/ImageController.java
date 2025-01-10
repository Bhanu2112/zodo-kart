package com.zodo.kart.controller.image;

import com.zodo.kart.service.amazons3.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Author : Bhanu prasad
 */

@RestController
@RequestMapping("/public/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("imagebelongsto") String imagebelongsto) {
        return ResponseEntity.ok(imageService.uploadFileToS3Bucket(file, imagebelongsto));
    }
}
