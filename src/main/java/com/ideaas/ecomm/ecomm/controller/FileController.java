package com.ideaas.ecomm.ecomm.controller;

import com.ideaas.ecomm.ecomm.domain.Image;
import com.ideaas.ecomm.ecomm.payload.UploadFileResponse;
import com.ideaas.ecomm.ecomm.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

@RestController
@RequestMapping("file")
public class FileController {

    private FileService fileService;

    @Autowired
    public FileController(final FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("upload")
    public UploadFileResponse uploadFile(@RequestParam("file") final MultipartFile file, final String folder) {
        final String fileName = fileService.storeFile(file, folder);

        final String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/")
                .path(folder)
                .path(File.separator)
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUrl,
                file.getContentType(), file.getSize());
    }

    @RequestMapping("{folder}")
    public ResponseEntity<List<Image>> readFiles(@PathVariable final String folder) {
        final List<Image> imagesOfProduct = fileService.readFiles(folder);
        imagesOfProduct.forEach(image -> {
            String path = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/file/download/")
                            .path(folder)
                            .path(File.separator)
                            .path(image.getUrl())
                            .toUriString();
            image.setLink(path);
        });

        return ResponseEntity.ok(imagesOfProduct);
    }

    @RequestMapping("download/{folder}/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable final String folder,
                                                 @PathVariable final String fileName,
                                                 final HttpServletRequest request) {
        final Resource resource = fileService.loadFileAsResource(folder, fileName);
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (Exception ex) {
        }
        if(contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
