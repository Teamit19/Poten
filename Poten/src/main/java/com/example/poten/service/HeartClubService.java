package com.example.poten.service;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.HeartBoard;
import com.example.poten.domain.HeartClub;
import com.example.poten.domain.User;
import com.example.poten.dto.request.HeartDto;
import com.example.poten.exception.BoardException;
import com.example.poten.exception.ClubException;
import com.example.poten.exception.HeartException;
import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.HeartBoardRepository;
import com.example.poten.repository.HeartClubRepository;
import com.example.poten.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class HeartClubService {
    private final ClubRepository clubRepository;
    private final HeartClubRepository heartClubRepository;

    public HeartClubService(ClubRepository clubRepository,HeartClubRepository heartClubRepository) {
        this.clubRepository = clubRepository;
        this.heartClubRepository = heartClubRepository;
    }
    
    /**
     *  동아리 좋아요 누름
     */
    public void heartClub(User loginUser, Long BoardId, HeartDto heartDto) {
        Club findClubFromRepo = clubRepository.findById(heartDto.getTargetId()).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        // 이미 좋아요 누른 유저인지 확인
        if(heartClubRepository.findHeartClubByUserAndClub(loginUser, findClubFromRepo).isPresent()) throw new HeartException("이미 좋아요 누른 동아리입니다.");

        HeartClub heartClub = HeartClub.builder()
            .club(findClubFromRepo)
            .user(loginUser)
            .build();

        heartClubRepository.save(heartClub);
    }

    /**
     *  피드 좋아요 해제
     */
    public void unHeartClub(User loginUser, Long BoardId, HeartDto heartDto) {
        Club findClubFromRepo = clubRepository.findById(heartDto.getTargetId()).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        HeartClub heartClub = heartClubRepository.findHeartClubByUserAndClub(loginUser, findClubFromRepo).orElseThrow(() -> new HeartException("좋아요 누르지 않은 동아리입니다."));

        heartClubRepository.delete(heartClub);
    }

}
