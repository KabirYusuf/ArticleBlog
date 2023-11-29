package dev.levelupschool.backend.service.implementation;

import com.cloudinary.Cloudinary;
import dev.levelupschool.backend.exception.UserException;
import dev.levelupschool.backend.service.interfaces.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class CloudinaryFileStorageService implements FileStorageService {
    @Value("${CLOUDINARY_CLOUD_NAME}")
    private String cloudinaryCloudName;
    @Value("${CLOUDINARY_API_KEY}")
    private String cloudinaryApiKey;
    @Value("${CLOUDINARY_API_SECRET}")
    private String cloudinaryApiSecret;
    @Override
    @Async
    public CompletableFuture<String> saveFile(MultipartFile file, String folderName) {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", cloudinaryCloudName);
        config.put("api_key", cloudinaryApiKey);
        config.put("api_secret", cloudinaryApiSecret);

        Cloudinary cloudinary = new Cloudinary(config);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("folder", folderName);

        try {
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(), parameters);
            return CompletableFuture.completedFuture((String) uploadedFile.get("url"));
        } catch (IOException e) {
            throw new UserException(e.getMessage());
        }

    }
}
