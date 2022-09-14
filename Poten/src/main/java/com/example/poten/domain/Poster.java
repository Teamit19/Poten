package com.example.poten.domain;

import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.request.PosterForm;
import com.example.poten.dto.response.FileResponse;
import com.example.poten.dto.response.PosterResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import java.time.Period;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import lombok.extern.java.Log;
import org.springframework.validation.FieldError;

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
    @JsonIgnore
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
        this.posterImg = new ArrayList<FileEntity>();
    }


    public PosterResponse toResponse() {
        // dday 계산
        LocalDate now = LocalDate.now();
        if (this.deadlineDate != null ) {
            Period period = Period.between(now, deadlineDate.toLocalDate());
        }

        var FileResponse = new FileResponse();
        if (! posterImg.isEmpty()) FileResponse = posterImg.get(0).toResponse();

        return PosterResponse.builder()
                .posterId(id)
                .club(club.toResponse())
//                .writer(user.toResponse())
                .title(title)
                .content(content)
                .pics(FileResponse)
                .deadlineDate(deadlineDate.toString())
//                .dday(period.getDays())
                .dday(1)
                .createdTime(getCreatedTime().toString())
                .modifiedTime(getModifiedTime().toString())
                .build();
    }

    // 공고 글 수정하기
    public void update(PosterForm form, List<FileEntity> pics) {
        this.content = form.getContent();
        this.posterImg = pics;
    }

    public void setPosterImg(List<FileEntity> posterImg) {
        this.posterImg = posterImg;
    }
}