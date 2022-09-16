package com.example.poten.controller;

import com.example.poten.domain.Club;
import com.example.poten.domain.Poster;
import com.example.poten.domain.User;
import com.example.poten.dto.request.BoolResponse;
import com.example.poten.dto.request.PosterForm;
import com.example.poten.dto.response.PosterResponse;
import com.example.poten.dto.response.PosterResponseList;
import com.example.poten.service.ClubService;
import com.example.poten.service.PosterService;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/posters")
public class PosterController {
    private final UserService userService;
    private final ClubService clubService;
    private final PosterService posterService;

    public PosterController(UserService userService, ClubService clubService, PosterService posterService){
        this.clubService = clubService;
        this.userService = userService;
        this.posterService = posterService;
    }

    private void logError(List<FieldError> errors) {
        log.error("Poster Errors = {}", errors);
    }


//        public ResponseEntity<?> savePoster(HttpServletRequest request, @PathVariable Long clubId, PosterForm posterForm, BindingResult bindingResult) throws Exception {

    @ApiOperation(value = "공고 생성")
    @PostMapping("/{clubId}/upload")
        public ResponseEntity<?> savePoster(HttpServletRequest request, @PathVariable Long clubId, @ModelAttribute @Valid PosterForm posterForm, BindingResult bindingResult) throws Exception {
        log.error("들어옴");
//        public ResponseEntity<?> savePoster(HttpServletRequest request, @Valid @RequestBody PosterForm posterForm, @PathVariable Long clubId,BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Club userClub = clubService.findByClubId(clubId);
//        Club userClub = clubService.findByClubId(posterForm.getClubId());
        Poster savedPoster = posterService.savePoster(loginUser, userClub, posterForm);
        log.error("savedPoster!! " + savedPoster);

//        PosterResponse result = savedPoster.toResponse();
//        log.error("result!! " + result);
        return ResponseEntity.ok(savedPoster);
    }

    @ApiOperation(value = "공고 하나 조회")
    @GetMapping("/{posterId}")
    public ResponseEntity<?> getPoster(HttpServletRequest request,  @PathVariable Long posterId) throws Exception {
//        User loginUser = userService.getLoginUser(request);

        Poster findPoster = posterService.findPosterById(posterId);
        PosterResponse posterResponse = findPoster.toResponse();
        log.info("공고조회 return 전 : " + posterResponse.getContent());

        return ResponseEntity.ok(findPoster.toResponse());
    }

    @ApiOperation(value = "공고 모두 조회")
    @GetMapping("/all")
    public ResponseEntity<?> getPosterAll() throws Exception {
        List<Poster> posterEntityList = posterService.findPosterAll();
        // DTO로 변환
        List<PosterResponse> posterResponseList = new ArrayList<>();
        posterEntityList.forEach(b -> posterResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new PosterResponseList(posterResponseList));
    }

    @ApiOperation(value = "동아리 공고 모두 조회")
    @GetMapping("/club/{clubId}")
    public ResponseEntity<?> getPosterByClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        Club club = clubService.findByClubId(clubId);
        List<Poster> posterEntityList = posterService.findPosterByClubId(club);

        // DTO로 변환
        List<PosterResponse> posterResponseList = new ArrayList<>();
        posterEntityList.forEach(b -> posterResponseList.add(b.toResponse()));

        return ResponseEntity.ok(new PosterResponseList(posterResponseList));
    }

    @ApiOperation(value = "검색 결과 - 공고")
    @PostMapping("/search")
    public ResponseEntity searchClub(@RequestBody Map<String, String> keywordMap){
        String keyword = keywordMap.get("keyword");
        List<PosterResponse> searchResult = posterService.searchPoster(keyword);

        return ResponseEntity.ok(new PosterResponseList(searchResult));
    }

    @ApiOperation(value = "공고 수정")
    @PutMapping("/update/{posterId}")
    public ResponseEntity<?> uploadPoster(HttpServletRequest request,  @PathVariable Long posterId, @Valid @RequestBody PosterForm posterForm, BindingResult bindingResult)
        throws Exception {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Poster updatedPoster = posterService.updatePoster(loginUser, posterId, posterForm);
        return ResponseEntity.ok(updatedPoster.toResponse());
    }

    @ApiOperation(value = "공고 삭제")
    @DeleteMapping("/delete/{posterId}")
    public ResponseEntity deleteBoard(HttpServletRequest request, @PathVariable Long posterId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        boolean deleteResult =  posterService.deletePoster(loginUser, posterId);
        return ResponseEntity.ok(new BoolResponse(deleteResult));
    }

}
