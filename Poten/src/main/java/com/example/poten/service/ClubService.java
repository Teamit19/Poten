package com.example.poten.service;

import com.example.poten.domain.*;
import com.example.poten.dto.request.ClubForm;
import com.example.poten.dto.response.ClubResponse;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.exception.ClubException;
import com.example.poten.exception.HeartException;
import com.example.poten.exception.UserException;
import com.example.poten.repository.*;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ClubService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    private final HeartClubRepository heartClubRepository;

    private final FollowRepository followRepository;

    private final FileService fileService;


    public ClubService(UserRepository userRepository, ClubRepository clubRepository, HeartClubRepository heartClubRepository, FollowRepository followRepository, FileService fileService) {
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.heartClubRepository = heartClubRepository;
        this.followRepository = followRepository;
        this.fileService = fileService;
    }

    public Club findByClubId(Long clubId){
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("동아리 조회 오류"));
        return findClub;
    }

    /**
     *  동아리 생성
     */
    public Club saveClub(User user, ClubForm clubForm) {

        Club saveClub = clubForm.toClub(user);
        saveClub.addMember(user);
        clubRepository.save(saveClub);

        return saveClub;
    }

    /**
     *  동아리 멤버 승인(부장만)
     */
    public UserResponse addMember(User loginUser, Long userId, Long clubId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (loginUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        findClub.addMember(findUser);

        return findUser.toResponse();
    }

    /**
     *  동아리 멤버 삭제 (부장만)
     */
    public Boolean deleteMember(User loginUser, Long userId, Long clubId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (loginUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        findClub.deleteMember(findUser);

        return true;
    }

    /**
     *  동아리 가입 신청
     */
    public boolean joinClub(User loginUser, Long clubId) {
        User findUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        findClub.addWaiting(findUser);

        return true;
    }

    /**
     *  동아리 팔로워 목록 조회
     */
    public List<UserResponse> getClulbMembers(Long clubId){
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));
        ClubResponse clubResponse = findClubFromRepo.toResponse();

        return clubResponse.getMembers();
    }

    /**
     *  동아리 검색
     */
    public List<ClubResponse> searchClub(String keyword){
        List<ClubResponse> result=new ArrayList<>();
        List<Club> searchList = clubRepository.findAllByNameContaining(keyword);

        for(Club club : searchList) {
            result.add(club.toResponse());
        }

        return result;
    }


    public Club updateClub(User loginUser, Long clubId, ClubForm form){
        User findUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUser.getId();
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (loginUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        findClub.update(form);
        return findClub;
    }

    public Club updateClubProfile(User loginUser, Long clubId, ClubForm form) throws Exception {
        User findUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUser.getId();
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (loginUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        List<FileEntity> profileToFileEnity = fileService.parseFileInfo(form.getProfile());    // FileEntity로 변환
        findClub.updateProfile(profileToFileEnity);

        return findClub;
    }

    public Club updateClubBg(User loginUser, Long clubId, ClubForm form) throws Exception {
        User findUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUser.getId();
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (loginUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        List<FileEntity> bgToFileEnity = fileService.parseFileInfo(form.getBackground());    // FileEntity로 변환
        findClub.updateBackground(bgToFileEnity);



        return findClub;
    }

    public Boolean deleteClub(User user, Long clubId) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        Long findUserId = findUser.getId();
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (findUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        clubRepository.deleteById(clubId);

        return true;

    }


    /**
     *  동아리 좋아요 누름
     */
    public HeartClub heartClub(User loginUser, Long clubId) {
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        // 이미 좋아요 누른 유저인지 확인
        if(heartClubRepository.findByUserAndClub(loginUser, findClubFromRepo).isPresent()) throw new HeartException("이미 좋아요 누른 동아리입니다.");

        HeartClub heartClub = HeartClub.builder()
                .club(findClubFromRepo)
                .user(loginUser)
                .build();

        heartClubRepository.save(heartClub);
        return heartClub;
    }

    /**
     *  동아리 좋아요 해제
     */
    public boolean unHeartClub(User loginUser, Long clubId) {
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        HeartClub heartClub = heartClubRepository.findByUserAndClub(loginUser, findClubFromRepo).orElseThrow(() -> new HeartException("좋아요 누르지 않은 동아리입니다."));

        heartClubRepository.delete(heartClub);

        return true;
    }

    /**
     *  동아리 팔로우 하기
     */
    public ClubResponse followClub(User loginUser, Long clubId) {
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));
//        findClubFromRepo.addFollower(loginUser);
        Follow follow = new Follow();
        follow.addFollow(findClubFromRepo, loginUser);
        followRepository.save(follow);

        return findClubFromRepo.toResponse();
    }


    /**
     *  동아리 팔로우 해제
     */
    public boolean unfollowClub(User loginUser, Long clubId) {
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));
//        findClubFromRepo.removeFollower(loginUser);
        followRepository.deleteByFollowingIdAndFollowerId(findClubFromRepo.getId(), loginUser.getId());

        return true;

    }

    /**
     *  동아리 회원 목록 조회
     */
    public List<UserResponse> findMembersByClub(Long clubId){
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));
        ClubResponse clubResponse = findClubFromRepo.toResponse();

        return clubResponse.getMembers();
    }

    /**
     *  동아리 팔로워 목록 조회
     */
    public List<UserResponse> findFollowersByClub(Long clubId){
        Club findClubFromRepo = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));
        ClubResponse clubResponse = findClubFromRepo.toResponse();

        return clubResponse.getFollows();
    }

    /**
     *  사용자가 팔로잉하는 동아리 목록 조회
     */
    public List<ClubResponse> findFollowingByUser(User loginUser, Long clubId){
        User findUser = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        List<Follow> followingList = followRepository.findAllByFollower(findUser);

        List<ClubResponse> followingDtoList = new ArrayList<>();
        for(Follow follow : followingList) {
            followingDtoList.add(follow.getFollowing().toResponse());
        }

        return followingDtoList;
    }

    /**
     *  동아리 부장 넘기기
     */
    public boolean changeManager(User manager, Long newManagerId,Long clubId) {
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));
        User newManager = userRepository.findById(newManagerId).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        if (manager.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");


        findClub.changeManager(newManager);

        return true;

    }





}
