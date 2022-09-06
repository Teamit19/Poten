package com.example.poten.controller;

import com.example.poten.domain.Club;
import com.example.poten.domain.User;
import com.example.poten.dto.request.BoolResponse;
import com.example.poten.dto.request.ClubForm;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.service.ClubService;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/clubs")
public class ClubController {

    private final UserService userService;
    private final ClubService clubService;

    public ClubController(UserService userService, ClubService clubService) {
        this.userService = userService;
        this.clubService = clubService;
    }

    private void logError(List<FieldError> errors) {
        log.error("Board Errors = {}", errors);
    }

    @ApiOperation(value = "동아리 생성")
    @PostMapping("")
    public ResponseEntity<?> createClub(HttpServletRequest request, @Valid @RequestBody ClubForm clubForm, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        Club saveClub = clubService.saveClub(loginUser, clubForm);


        return ResponseEntity.ok(saveClub.toResponse());
    }

    @ApiOperation(value = "동아리 멤버 추가")
    @PostMapping("/{clubId}/{userId}")
    public ResponseEntity addClubMember(HttpServletRequest request,  @PathVariable Long clubId, @PathVariable Long userId, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
        }

        User loginUser = userService.getLoginUser(request);
        UserResponse addUser = clubService.addMember(loginUser, userId, clubId);


        return ResponseEntity.ok(addUser);
    }

    @ApiOperation(value = "동아리 조회")
    @GetMapping("/{clubId}")
    public ResponseEntity<?> getClub(HttpServletRequest request, @PathVariable Long clubId) throws LoginException {
        User loginUser = userService.getLoginUser(request);
        Club club = clubService.findByClubId(clubId);

        return ResponseEntity.ok(club.toResponse());
    }



    @ApiOperation(value = "동아리 정보 수정")
    @PutMapping("/{clubId}")
    public ResponseEntity<?> updateClub(HttpServletRequest request,  @PathVariable Long clubId, @Valid @RequestBody ClubForm clubForm, BindingResult bindingResult) throws LoginException {
        if (bindingResult.hasErrors()) {
            final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            logError(fieldErrors);
            new ResponseEntity<>(fieldErrors, HttpStatus.BAD_REQUEST);
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

}
