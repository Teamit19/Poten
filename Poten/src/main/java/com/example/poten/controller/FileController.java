package com.example.poten.controller;

import com.example.poten.service.FileService;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {
    private  FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    private void logError(List<FieldError> errors) {
        log.error("Board Errors = {}", errors);
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public ResponseEntity<byte[]> getFile( @PathVariable String filename){
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        String fileDirPath = bundle.getString("basefilePath");
        log.error("getFile 들어옴");

        File file = new File(fileDirPath, filename);
        ResponseEntity<byte[]> result = null;
        log.error("파일 정보 " + file.toString());
        try{
            HttpHeaders header = new HttpHeaders();

            header.add("Content-type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),header, HttpStatus.OK);
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable String filename)
        throws MalformedURLException {
        Resource resource = fileService.loadAsResource(filename);
        String resourceName = resource.getFilename();

        String resourceOrgName = resourceName.substring(resourceName.indexOf("_") + 1);

        HttpHeaders headers = new HttpHeaders();
        try{
            headers.add("Content-Disposition", "attachment; filename=" +
                new String(resourceOrgName.getBytes(StandardCharsets.UTF_8),"ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
    }
}
