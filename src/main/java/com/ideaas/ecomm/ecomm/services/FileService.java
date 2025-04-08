package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.domain.Product;
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
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

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
        // String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;


        try {
            // Check if the file's name contains invalid characters
            if (originalFilename.contains("..") || fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalFilename);
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

    public void deleteImage(Product product, String image){
       String path = fileStorageLocation.toString() + File.separator + product.getId() +  File.separator + image;
       System.out.println(path);
       eliminarImagenes(path);
    }

    public static void eliminarImagenes(String rutaCarpeta) {
        File archivo = new File(rutaCarpeta);
        if(archivo.isFile() && esImagen(archivo.getName())){
            archivo.delete();
            logger.info("Se ha eliminado "+ archivo.getName());
        } else {
            logger.error("La ruta especificada no corresponde a un Archivo.");
        }
    }

    // public static boolean esImagen(String nombreArchivo) {
    //     String[] extensiones = {"png", "jpg", "jpeg", "gif", "bmp"};
    //     for (String extension : extensiones) {
    //         if (nombreArchivo.toLowerCase().endsWith("." + extension)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // public static void eliminarImagenes(String rutaCarpeta) {
    //     File carpeta = new File(rutaCarpeta);
    //     if (carpeta.isDirectory()) {
    //         File[] archivos = carpeta.listFiles();
    //         if (archivos != null) {
    //             for (File archivo : archivos) {
    //                 if (archivo.isFile() && esImagen(archivo.getName())) {
    //                     if (archivo.delete()) {
    //                         logger.info("Se ha eliminado " + archivo.getName());
    //                     } else {
    //                         logger.error("No se pudo eliminar " + archivo.getName());
    //                     }
    //                 }
    //             }
    //         }
    //     } else {
    //         logger.error("La ruta especificada no corresponde a una Carpeta.");
    //     }
    // }

    public static boolean esImagen(String nombreArchivo) {
        String[] extensiones = {"png", "jpg", "jpeg", "gif", "bmp"};
        for (String extension : extensiones) {
            if (nombreArchivo.toLowerCase().endsWith("." + extension)) {
                return true;
            }
        }
        return false;
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
