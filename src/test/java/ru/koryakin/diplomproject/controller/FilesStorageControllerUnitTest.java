package ru.koryakin.diplomproject.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.controller.DTO.request.EditRequest;
import ru.koryakin.diplomproject.controller.DTO.response.FileResponse;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.service.FilesStorageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;

@SpringJUnitConfig({
        FilesStorageController.class
})
class FilesStorageControllerUnitTest {

    @Autowired
    FilesStorageController filesStorageController;

    @MockBean
    FilesStorageService filesStorageService;

    @MockBean
    Principal principal;

    @BeforeEach
    void requestPreparator() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    void getAllFilesTest() {
        //arrange
        String limit = "3";
        FileResponse file1 = new FileResponse("Filename1", 10);
        FileResponse file2 = new FileResponse("Filename2", 20);
        List<Object> files = List.of(file1, file2);
        Mockito.when(filesStorageService.getAllFiles(limit, principal)).thenReturn(files);
        //act
        List<?> response = filesStorageController.getAllFiles(limit, principal);
        //assert
        assertEquals(file1, response.get(0));
    }

    @Test
    void uploadFile() {
        //arrange
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 10;
            }

            @Override
            public byte[] getBytes() {
                return new byte[10];
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IllegalStateException {

            }
        };

        //act
        ResponseEntity<?> responseEntity = filesStorageController.uploadFile("Filename1", file, principal);
        //assert
        Mockito.verify(filesStorageService, only()).uploadFile(any(FileStorage.class), eq(principal));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void deleteFile() {
        //arrange
        String filename = "Filename1";
        //act
        ResponseEntity<?> responseEntity = filesStorageController.deleteFile(filename, principal);
        //assert
        Mockito.verify(filesStorageService, only()).deleteFile(eq("Filename1"), eq(principal));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void editFilename() {
        //arrange
        String filename = "oldFilename";
        EditRequest editRequest = new EditRequest("newFilename");
        //act
        ResponseEntity<?> responseEntity = filesStorageController.editFilename(filename, editRequest, principal);
        //assert
        Mockito.verify(filesStorageService, only()).editFileName(eq("oldFilename"), eq("newFilename"), eq(principal));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void getFileByFilename() {
        //arrange
        String filename = "Filename";
        ByteArrayResource byteArrayResource = new ByteArrayResource(new byte[1000]);
        Mockito.when(filesStorageService.getFileByFilename(filename, principal)).thenReturn(byteArrayResource);
        //act
        ByteArrayResource responseByte = filesStorageController.getFileByFilename(filename, principal);
        //assert
        assertEquals(1000, responseByte.getByteArray().length);
    }
}