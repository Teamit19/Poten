package com.example.poten.domain;

import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.request.PosterForm;
import com.example.poten.dto.response.PosterResponse;
import com.sun.istack.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Getter
@Table(name="poster")
public class Poster extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name="clubId")
    private Club club;

    @ManyToOne
    @NotNull
    @JoinColumn(name="userId")
    private User user;

    private String title;
    private String content;
    private LocalDateTime deadlineDate;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    private List<FileEntity> posterImg;

    @Builder
    public Poster(Long id, Club club, User user, String title, String content,
        LocalDateTime deadlineDate, List<FileEntity> posterImg) {
        this.club = club;
        this.user = user;
        this.title = title;
        this.content = content;
        this.deadlineDate = deadlineDate;
        this.posterImg = posterImg;
    }

    public PosterResponse toResponse() {
        return PosterResponse.builder()
            .posterId(id)
            .club(club.toResponse())
            .writer(user.toResponse())
            .title(title)
            .content(content)
            .deadlineDate(deadlineDate.toString())
            .createdTime(getCreatedTime().toString())
            .modifiedTime(getModifiedTime().toString())
            .build();
    }

    // 공고 글 수정하기
    public void update(PosterForm form, List<FileEntity> pics) {
        this.content = form.getContent();
        this.posterImg = pics;
    }
}
