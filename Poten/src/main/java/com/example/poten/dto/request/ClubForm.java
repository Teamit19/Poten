package com.example.poten.dto.request;

import com.example.poten.domain.Club;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.Follow;
import com.example.poten.domain.User;
import com.example.poten.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
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

    @NotNull
    private Integer field;

    @NotNull
    private Integer activityType;

    private List<MultipartFile> profile;

    private List<MultipartFile> background;

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
                .following(this.follows)
                .build();
    }



}
