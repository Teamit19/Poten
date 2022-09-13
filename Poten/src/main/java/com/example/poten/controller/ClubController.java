package com.example.poten.controller;

import com.example.poten.domain.*;
import com.example.poten.dto.TestDto;
import com.example.poten.dto.request.BoolResponse;
import com.example.poten.dto.request.ClubForm;
import com.example.poten.dto.response.ClubResponse;
import com.example.poten.dto.response.Test;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.service.ClubService;
import com.example.poten.service.FileService;
import com.example.poten.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final UserService userService;
    private final ClubService clubService;

    private final FileService fileService;

    private TestDto test;

    public ClubController(UserService userService, ClubService clubService, FileService fileService) {
        this.userService = userService;
        this.clubService = clubService;
        this.fileService = fileService;
    }

    private void logError(List<FieldError> errors) {
        log.error("Club Errors = {}", errors);
    }


    @PostMapping("/test")
    @ResponseBody
    public Test sendTest(@ModelAttribute HashMap<String, String> param){
        Test t = new Test("test");
        System.out.println("test 완료" + param);
        return t;
    }

    @ApiOperation(value = "안드로이드테스트")
    @PostMapping("/test2")
    public ResponseEntity postTest(@ModelAttribute @Valid TestDto testDto) throws Exception {
        System.out.println("test 2 시작" + testDto.getPics() + testDto.getContent());
        List<FileEntity> picsToFileEnity = fileService.parseFileInfo(testDto.getPics());    // FileEntity로 변환
        test = testDto;
        if(! picsToFileEnity.isEmpty()) {
            System.out.println("test 2 시작1" + picsToFileEnity.get(0).getOrgFileName());
            test.addFile(picsToFileEnity);
        }
        return ResponseEntity.ok(testDto);
    }

    @ApiOperation(value = "안드로이드테스트")
    @GetMapping("/test2")
    public ResponseEntity getTest(){

        return ResponseEntity.ok(test);
    }

    @ApiOperation(value = "동아리 생성")
    @PostMapping("")
    public ResponseEntity<?> createClub(HttpServletRequest request, @Valid @RequestBody ClubForm clubForm, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Club saveClub = clubService.saveClub(loginUser, clubForm);


        return ResponseEntity.ok(saveClub.toResponse());
    }

    @ApiOperation(value = "동아리 멤버 추가")
    @PostMapping("/{clubId}/{userId}")
    public ResponseEntity addClubMember(HttpServletRequest request,  @PathVariable Long clubId, @PathVariable Long userId) throws Exception {

        User loginUser = userService.getLoginUser(request);
        UserResponse addUser = clubService.addMember(loginUser, userId, clubId);


        return ResponseEntity.ok(addUser);
    }

    @ApiOperation(value = "동아리 가입 신청")
    @PostMapping("/{clubId}/join")
    public ResponseEntity joinClub(HttpServletRequest request,  @PathVariable Long clubId) throws LoginException {

        User loginUser = userService.getLoginUser(request);
        Boolean joinResult = clubService.joinClub(loginUser, clubId);

        return ResponseEntity.ok(new BoolResponse(joinResult));
    }

    @ApiOperation(value = "동아리 검색")
    @PostMapping("/search")
    public ResponseEntity searchClub(@RequestBody Map<String, String> keywordMap){
        String keyword = keywordMap.get("keyword");
        List<ClubResponse> searchResult = clubService.searchClub(keyword);

        return ResponseEntity.ok(searchResult);
    }

    @ApiOperation(value = "동아리 추천")
    @GetMapping("/recommend")
    public ResponseEntity getInterestclub(HttpServletRequest request) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        List<Club> interestResult = clubService.getInterestClub(loginUser);

        return ResponseEntity.ok(interestResult);
    }
    

    @ApiOperation(value = "동아리 조회")
    @GetMapping("/{clubId}")
    public ResponseEntity<?> getClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        Club club = clubService.findByClubId(clubId);

        return ResponseEntity.ok(club.toResponse());
    }

    @ApiOperation(value = "동아리 멤버 조회")
    @GetMapping("/{clubId}/member")
    public ResponseEntity<?> getClubMembers(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        List<UserResponse> clubMembers = clubService.findMembersByClub(clubId);

        return ResponseEntity.ok(clubMembers);
    }

    @ApiOperation(value = "동아리 팔로워 목록 조회")
    @GetMapping("/{clubId}/follower")
    public ResponseEntity<?> getClubFollowers(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        List<UserResponse> followers = clubService.findFollowersByClub(clubId);

        return ResponseEntity.ok(followers);
    }


    @ApiOperation(value = "동아리 정보 수정")
    @PutMapping("/{clubId}")
    public ResponseEntity<?> updateClub(HttpServletRequest request,  @PathVariable Long clubId, @Valid @RequestBody ClubForm clubForm, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            return new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Club updateClub = clubService.updateClub(loginUser, clubId, clubForm);
        return ResponseEntity.ok(updateClub.toResponse());
    }

    @ApiOperation(value = "동아리 삭제")
    @DeleteMapping("/{clubId}")
    public ResponseEntity deleteClub(HttpServletRequest request,  @PathVariable Long clubId) throws LoginException {

        User loginUser = userService.getLoginUser(request);
        boolean deleteResult =  clubService.deleteClub(loginUser, clubId);
        return ResponseEntity.ok(new BoolResponse(deleteResult));
    }

    @ApiOperation(value = "동아리 멤버 삭제")
    @DeleteMapping("/{clubId}/{userId}")
    public ResponseEntity deleteClubMember(HttpServletRequest request,  @PathVariable Long clubId, @PathVariable Long userId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        boolean deleteResult =  clubService.deleteMember(loginUser, userId, clubId);
        return ResponseEntity.ok(new BoolResponse(deleteResult));
    }

    @ApiOperation(value = "동아리 부장 변경")
    @PostMapping("/{clubId}/manager/{userId}")
    public ResponseEntity changeManager(HttpServletRequest request,  @PathVariable Long clubId, @PathVariable Long userId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        boolean changeResult =  clubService.changeManager(loginUser, userId, clubId);
        return ResponseEntity.ok(new BoolResponse(changeResult));
    }

    @ApiOperation(value = "동아리 좋아요")
    @PostMapping("/{clubId}/heart")
    public ResponseEntity heartClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        HeartClub heartClub =  clubService.heartClub(loginUser, clubId);
        return ResponseEntity.ok(heartClub.toResponse());
    }

    @ApiOperation(value = "동아리 좋아요 해제")
    @PostMapping("/{clubId}/unheart")
    public ResponseEntity unHeartBoard(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        boolean unHeartResult =  clubService.unHeartClub(loginUser, clubId);
        return ResponseEntity.ok(new BoolResponse(unHeartResult));
    }

    @ApiOperation(value = "동아리 팔로우")
    @PostMapping("/{clubId}/follow")
    public ResponseEntity followClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        ClubResponse followClub =  clubService.followClub(loginUser, clubId);
        return ResponseEntity.ok(followClub);
    }

    @ApiOperation(value = "동아리 팔로우 해제")
    @PostMapping("/{clubId}/unfollow")
    public ResponseEntity unfollowClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);

        boolean unfollowResult =  clubService.unfollowClub(loginUser, clubId);
        return ResponseEntity.ok(new BoolResponse(unfollowResult));
    }





}
