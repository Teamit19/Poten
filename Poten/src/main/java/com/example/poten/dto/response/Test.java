package com.example.poten.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class Test {
    //private MultipartFile pic;
    private String content;
    private String content2;
}
