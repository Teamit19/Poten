package com.example.poten.service;

import com.example.poten.domain.FileEntity;
import com.example.poten.repository.FileRepository;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    // 파일 저장
    public List<FileEntity> parseFileInfo(List<MultipartFile> files) throws Exception {
        List<FileEntity> fileList = new ArrayList<>();
        System.err.println("여기까지 옴0");
        if (!CollectionUtils.isEmpty(files)) {
            System.err.println("여기까지 옴1");
            ResourceBundle bundle = ResourceBundle.getBundle("application");
            String fileDirPath = bundle.getString("basefilePath");  // parent 폴더

            File uploadPath = new File(fileDirPath);

            if (!uploadPath.exists()) {
                if(!uploadPath.mkdirs())
                {
                    System.err.println("디렉토리 생성 실패");
                }
            }

            for (MultipartFile multipartFile : files) {
                String fileType;
                String contentType = multipartFile.getContentType();

                if(ObjectUtils.isEmpty(contentType)) {
                    System.err.println("ObjectUtils.isEmpty(contentType)");
                    break;
                } else {
                    if(contentType.contains("image/jpeg"))
                        fileType = "image";
                    else if(contentType.contains("image/png"))
                        fileType = "image";
                    else
                        break;
                }

                System.err.println("여기까지 옴2");
                String orgFilename = multipartFile.getOriginalFilename();
                orgFilename = orgFilename.substring(orgFilename.lastIndexOf("\\") + 1);

                UUID uuid = UUID.randomUUID();
                String savedFilename = uuid.toString() + "_" + orgFilename;

                try {
                    InputStream initialStream = multipartFile.getInputStream();

                    File saveFile = new File(fileDirPath, savedFilename) ;
                    multipartFile.transferTo(saveFile);
                    
                    if (checkImageType(saveFile)) {
                        FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + savedFilename));
                        Thumbnailator.createThumbnail(initialStream, thumbnail, 1000, 1000);
                        System.err.println("여기까지 옴3");
                        thumbnail.close();
                    }


                    FileEntity fileEntity = FileEntity.builder()
                        .orgFileName(orgFilename)
                        .savedFileName(savedFilename)
                        .fileSize(multipartFile.getSize())
                        .fileType(fileType)
                        .build();

                    System.err.println("여기까지 옴4");
                    fileList.add(fileEntity);

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return fileList;
    }

    public Resource loadAsResource(String filename) throws MalformedURLException {
        return new UrlResource("file:" + createPath(filename));
    }

    public String createPath(String filename) {
        ResourceBundle bundle = ResourceBundle.getBundle("application");
        String fileDirPath = bundle.getString("basefilePath");

        return fileDirPath + "\\" + filename;
    }

    private Boolean checkImageType(File file){
        try {
            String contentType = Files.probeContentType(file.toPath());
            if(contentType != null){
                return contentType.startsWith("image");
            }
            return false;
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

}

