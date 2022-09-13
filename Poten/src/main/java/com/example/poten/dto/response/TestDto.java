package com.example.poten.dto.response;

import com.example.poten.domain.FileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Data
public class TestDto {

    private List<MultipartFile> pics;
   // private MultipartFile pic;
    private String content;
    private String content2;
    private List<FileEntity> files;


    public void addFile(List<FileEntity> files) {
        this.files = files;
    }
}
