package com.example.poten.service;

import com.example.poten.domain.Club;
import com.example.poten.domain.FileEntity;
import com.example.poten.domain.Poster;
import com.example.poten.domain.User;
import com.example.poten.dto.request.PosterForm;
import com.example.poten.dto.response.ClubResponse;
import com.example.poten.dto.response.PosterResponse;
import com.example.poten.exception.ClubException;
import com.example.poten.exception.PosterException;
import com.example.poten.exception.UserException;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.PosterRepository;
import com.example.poten.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class PosterService {
    private final PosterRepository posterRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final FileService fileService;

    public PosterService(PosterRepository posterRepository, UserRepository userRepository, ClubRepository clubRepository, FileService fileService){
        this.posterRepository = posterRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
        this.fileService = fileService;
    }

    /**
     * 공고 업로드
     */
    public Poster savePoster(User loginUser, Club userClub, PosterForm form) throws Exception {
        /**
         * 검증 0 : 해당 유저가 있는지 확인
         * 검증 1 : 해당 유저가 동아리 대표가 맞는지 확인
         */
        User findUserFromRepo = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        Club findClub = clubRepository.findById(userClub.getId()).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (loginUser.getId()!=findClub.getManager().getId())  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");
        
        List<FileEntity> picsToFileEnity = fileService.parseFileInfo(form.getPosterImg());    // FileEntity로 변환

        Poster savedPoster =form.toPoster(findUserFromRepo, findClub, picsToFileEnity);
        savedPoster.setPosterImg(picsToFileEnity);
        posterRepository.save(savedPoster);
        return savedPoster;
    }

    /**
     * 공고 조회
     */
    // 공고 하나 조회 (by 공고id)
    public Poster findPosterById(Long posterId){
        Poster findPoster = posterRepository.findById(posterId).orElseThrow(() -> new PosterException("공고 조회 오류"));
        return findPoster;
    }

    // 동아리의 공고 모두 조회 (by 동아리id)
    public List<Poster> findPosterByClubId(Club club){
        List<Poster> findPostersFromRepo = posterRepository.findAllByClub(club);
        return findPostersFromRepo == null ? Collections.emptyList() : findPostersFromRepo;
    }

    public List<Poster> findPosterAll(){
        List<Poster> findPostersFromRepo = posterRepository.findAll();
        return findPostersFromRepo == null ? Collections.emptyList() : findPostersFromRepo;
    }

    // 검색 - 공고
    public List<PosterResponse> searchPoster(String keyword){
        List<Poster> posterList=new ArrayList<>();
        List<PosterResponse> result=new ArrayList<>();

        List<Club> searchClubList = clubRepository.findAllByNameContaining(keyword);

        for(Club club : searchClubList) {
            posterList.addAll(posterRepository.findAllByClub(club));
        }
        for(Poster poster : posterList) {
            result.add(poster.toResponse());
        }

        return result;
    }

    /**
     * 공고 수정
     */
    public Poster updatePoster(User loginUser, Long posterId, PosterForm form) throws Exception {
        User findUserFromRepo = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUserFromRepo.getId();
        Poster findPosterFromRepo = posterRepository.findById(posterId).orElseThrow(() -> new PosterException("존재하지 않는 공고입니다."));

        // ! 피드 수정과 달리, 공고 수정/삭제는 동아리의 관리자만 가능하다. !
        Club posterClub = findPosterFromRepo.getClub();

        if (findUserId!=posterClub.getManager().getId()) throw new PosterException("해당 유저는 동아리 관리자가 아닙니다.");

        List<FileEntity> picsToFileEnity = fileService.parseFileInfo(form.getPosterImg());    // FileEntity로 변환
        findPosterFromRepo.update(form, picsToFileEnity);
        return findPosterFromRepo;
    }

    /**
     * 공고 삭제
     */
    public boolean deletePoster(User loginUser, Long posterId){
        User findUserFromRepo = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUserFromRepo.getId();
        Poster findPosterFromRepo = posterRepository.findById(posterId).orElseThrow(() -> new PosterException("존재하지 않는 공고입니다."));

        // ! 피드 수정과 달리, 공고 수정/삭제는 동아리의 관리자만 가능하다. !
        Club posterClub = findPosterFromRepo.getClub();

        if (findUserId!=posterClub.getManager().getId()) throw new PosterException("해당 유저는 동아리 관리자가 아닙니다.");

        posterRepository.deleteById(posterId);
        return true;
    }

}
