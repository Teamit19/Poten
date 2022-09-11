package com.example.poten.dto.request;

import com.example.poten.domain.Club;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.Follow;
import com.example.poten.domain.User;
import com.example.poten.dto.response.UserResponse;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

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

    private List<Follow> follows;


    public Club toClub(User manager){
        this.follows = new ArrayList<Follow>();

        return Club.builder()
                .name(this.name)
                .manager(manager)
                .clubDesc(this.clubDesc)
                .region(this.region)
                .field(this.field)
                .activityType(this.activityType)
                .profile(this.profile)
                .background(this.background)
                .following(this.follows)
                .build();
    }

}
