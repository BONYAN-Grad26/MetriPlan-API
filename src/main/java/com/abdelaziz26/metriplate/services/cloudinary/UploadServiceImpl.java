package com.abdelaziz26.metriplate.services.cloudinary;

import com.abdelaziz26.metriplate.utils.annotations.FileValidator;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final Cloudinary cloudinary;

    @FileValidator
    @Override
    public String upload(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(),
                Map.of(
                        "folder", "___GamesStore__Api",
                        "resource_type", "auto",
                        "public_id", file.getOriginalFilename(),
                        "overwrite", true
                )
        ).get("url").toString();
    }
}
