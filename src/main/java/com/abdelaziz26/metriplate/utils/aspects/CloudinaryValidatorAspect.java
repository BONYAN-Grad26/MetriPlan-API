package com.abdelaziz26.metriplate.utils.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Aspect
@Component
public class CloudinaryValidatorAspect {

    @Before("@annotation(com.abdelaziz26.metriplate.utils.annotations.FileValidator)")
    public void validateImage(JoinPoint joinPoint) throws IOException {
        Optional<Object> maybeFile = Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof MultipartFile)
                .findFirst();

        if (maybeFile.isEmpty())
            throw new IOException("No file parameter found");

        MultipartFile multipartFile = (MultipartFile) maybeFile.get();

        if(multipartFile.isEmpty())
            throw new IOException("file is empty");

        String fileName = multipartFile.getOriginalFilename();

        if(fileName == null || fileName.isEmpty() || fileName.contains("..") || fileName.contains("/") || fileName.contains("\\"))
            throw new IOException("Invalid file name");

        String exe = fileName.substring(fileName.lastIndexOf("."));

        List<String> allowed = Arrays.asList(".jpg", ".jpeg", ".png", ".gif");

        if (!allowed.contains(exe)) {
            throw new IOException("Invalid Extension - Only JPG, JPEG, PNG, or GIF are allowed");
        }
    }
}
