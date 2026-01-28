package com.abdelaziz26.metriplate.services.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String upload(MultipartFile file) throws IOException;
}
