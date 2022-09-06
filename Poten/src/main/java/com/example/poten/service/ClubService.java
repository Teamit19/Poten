package com.example.poten.service;

import com.example.poten.domain.Board;
import com.example.poten.domain.Club;
import com.example.poten.domain.User;
import com.example.poten.dto.request.BoardForm;
import com.example.poten.dto.request.ClubForm;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.exception.BoardException;
import com.example.poten.exception.ClubException;
import com.example.poten.exception.UserException;
import com.example.poten.repository.BoardRepository;
import com.example.poten.repository.ClubRepository;
import com.example.poten.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional
public class ClubService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;

    public ClubService(BoardRepository boardRepository, UserRepository userRepository,  ClubRepository clubRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.clubRepository = clubRepository;
    }

    public Club findByClubId(Long clubId){
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("동아리 조회 오류"));
        return findClub;
    }

    public Club saveClub(User user, ClubForm clubForm) {
        Club saveClub = clubRepository.save(clubForm.toClub(user));

        return saveClub;
    }

    public UserResponse addMember(User loginUser, Long userId, Long clubId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (!loginUser.equals(findClub.getUser().getId()))  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        findClub.addMember(findUser);

        return findUser.toResponse();
    }

    public Boolean deleteMember(User loginUser, Long userId, Long clubId) {
        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (!loginUser.equals(findClub.getUser().getId()))  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        findClub.deleteMember(findUser);

        return true;
    }


    public Club updateClub(User user, Long clubId, ClubForm form){
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));
        Long findUserId = findUser.getId();
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (!findUserId.equals(findClub.getUser().getId()))  throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        findClub.update(form);
        return findClub;
    }

    public Boolean deleteClub(User user, Long clubId) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(() -> new UserException("등록된 회원이 없습니다."));

        Long findUserId = findUser.getId();
        Club findClub = clubRepository.findById(clubId).orElseThrow(() -> new ClubException("존재하지 않는 동아리입니다."));

        if (!findUserId.equals(findClub.getUser().getId())) throw new ClubException("해당 유저는 동아리 부장이 아닙니다.");

        clubRepository.deleteById(clubId);

        return true;

    }






}
