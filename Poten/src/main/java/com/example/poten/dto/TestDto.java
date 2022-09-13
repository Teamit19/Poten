package com.example.poten.dto;

import com.example.poten.domain.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class TestDto {

    private List<MultipartFile> pics;
    private String content;
    private List<FileEntity> files;

    public void addFile(List<FileEntity> files) {
        this.files = files;
    }
}
