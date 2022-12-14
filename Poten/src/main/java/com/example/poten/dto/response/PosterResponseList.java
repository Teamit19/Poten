package com.example.poten.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PosterResponseList {
    private List<PosterResponse> posterResponseList;
}
