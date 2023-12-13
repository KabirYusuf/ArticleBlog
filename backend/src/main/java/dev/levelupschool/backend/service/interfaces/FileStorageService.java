package dev.levelupschool.backend.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface FileStorageService {
    String saveFile(MultipartFile file, String folderName);
}
