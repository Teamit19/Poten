package com.example.poten.domain;

import com.example.poten.dto.response.HeartClubResponse;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class HeartClub {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @NotNull
    @JoinColumn(name="clubId")
    private Club club;

    @Builder
    public HeartClub(Long id, User user, Club club) {
        this.id = id;
        this.user = user;
        this.club = club;
    }

    public HeartClubResponse toResponse() {
        return HeartClubResponse.builder()
            .heartId(id)
            .lover(user.toResponse())
            .club(club.toResponse())
            .build();
    }
}
