package com.example.poten.dto.request;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
public class ClubForm {

    private Long id;

    @NotEmpty
    private String name;

    private User user;

    @NotEmpty
    private String clubDesc;

    @NotEmpty
    private String region;

    @NotEmpty
    private Integer field;

    @NotEmpty
    private Integer activityType;

    private FileEntity profile;

    private FileEntity background;

    public Club toClub(User manager){
        return Club.builder()
                .name(this.name)
                .manager(manager)
                .clubDesc(this.clubDesc)
                .region(this.region)
                .field(this.field)
                .activityType(this.activityType)
                .profile(this.profile)
                .background(this.background)
                .build();
    }

}
