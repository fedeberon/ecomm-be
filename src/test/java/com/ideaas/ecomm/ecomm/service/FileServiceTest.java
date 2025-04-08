package com.ideaas.ecomm.ecomm.service;
import com.ideaas.ecomm.ecomm.exception.FileStorageException;
import com.ideaas.ecomm.ecomm.services.FileService;
import com.ideaas.ecomm.ecomm.services.FileStorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension .class)
public class FileServiceTest {

    private FileService fileService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        FileStorageProperties fileStorageProperties = mock(FileStorageProperties.class);
        when(fileStorageProperties.getUploadDir()).thenReturn(tempDir.toString());
        fileService = new FileService(fileStorageProperties);
    }

    @Test
    void testStoreFile() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "Hello, World!".getBytes()
        );
        String folder = "uploads";

        String fileName = fileService.storeFile(file, folder);
        Path filePath = tempDir.resolve(Paths.get(folder, fileName));
        assertTrue(Files.exists(filePath));
    }

    @Test
    void testStoreFileWithInvalidName() {
        MultipartFile file = new MockMultipartFile("file", "../evil.exe", "text/plain", "Contenido".getBytes());

        FileStorageException exception = assertThrows(FileStorageException.class, () -> {
            fileService.storeFile(file, "uploads");
        });
        assertTrue(exception.getMessage().contains("Filename contains invalid path sequence"));
    }

    @Test
    void testLoadFileAsResource_FileExists() throws IOException {
        String folder = "uploads";
        String fileName = "test.txt";
        Path folderPath = tempDir.resolve(folder);
        Files.createDirectories(folderPath);
        Path filePath = folderPath.resolve(fileName);
        Files.write(filePath, "Hello, World!".getBytes());

        Resource resource = fileService.loadFileAsResource(folder, fileName);
        assertNotNull(resource);
        assertTrue(resource.exists());
    }

    @Test
    void testLoadFileAsResource_FileNotFound() {
        assertNull(fileService.loadFileAsResource("uploads", "nonexistent.txt"));
    }
}
