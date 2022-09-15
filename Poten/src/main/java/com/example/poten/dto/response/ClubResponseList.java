package com.example.poten.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ClubResponseList {
    private List<ClubResponse> clubResponseList;

}
