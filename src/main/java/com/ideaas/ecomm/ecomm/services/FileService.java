package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.exception.FileStorageException;
import com.ideaas.ecomm.ecomm.exception.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class FileService {


    private final Path fileStorageLocation;

    @Autowired
    public FileService(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public String storeFile(MultipartFile file, String folder) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Path path = Paths.get(this.fileStorageLocation.toString().concat(File.separator).concat(folder));
            try {
                Files.createDirectories(path);
            } catch (Exception ex) {
                throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
            }
            Path targetLocation = path.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }


    public List<Image> readFiles(String url) {
        final List<Image> images = new ArrayList<>();
        try {
            Files.walkFileTree(Paths.get(fileStorageLocation.toString() + File.separator + url), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs){
                    try {
                        images.add(new Image(file.getFileName().toString(), false));
                    }
                    finally {
                        return FileVisitResult.CONTINUE;
                    }
                }
            });
        } catch (NoSuchFileException ex) {
           return Collections.EMPTY_LIST;
        } catch (IOException ex) {
        }

        Collections.sort(images, Comparator.comparing(Image::getUrl));

        return images;
    }


    public Resource loadFileAsResource(String folder, String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(folder.concat("/").concat(fileName)).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException | MyFileNotFoundException ex) {
            return null;
        }
    }
}
