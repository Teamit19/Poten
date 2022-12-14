package com.example.poten.domain;

import com.example.poten.dto.response.FileResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class FileEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    private String savedFileName;
    private String orgFileName;
    private String fileType;
    private Long fileSize;

    @Builder
    public FileEntity(Long id, String savedFileName, String orgFileName,
                      String fileType, Long fileSize) {
        this.id = id;
        this.savedFileName = savedFileName;
        this.orgFileName = orgFileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
    }

    public FileResponse toResponse() {
        return FileResponse.builder()
            .fileName(savedFileName)
            .build();
    }
}
