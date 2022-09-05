//package com.example.poten.service;
//
//import com.example.poten.domain.Board;
//import com.example.poten.domain.Club;
//import com.example.poten.domain.User;
//import com.example.poten.dto.request.BoardForm;
//import com.example.poten.exception.BoardException;
//import com.example.poten.repository.BoardRepository;
//import com.example.poten.repository.ClubRepository;
//import com.example.poten.repository.UserRepository;
//import static org.assertj.core.api.Assertions.*;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//
//class BoardServiceTest {
//
//    @Autowired
//    BoardRepository boardRepository;
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    ClubRepository clubRepository;
//
////    @AfterEach
////    public void afterEach() {
////        boardRepository.deleteAll();
////        userRepository.deleteAll();
////        clubRepository.deleteAll();
////    }
//
//    @Test
//    void 피드업로드() {
//        // given
//        User loginUser = User.builder()
//            .email("test@gmail.com")
//            .password("test1234")
//            .name("test")
//            .nickname("tt")
//            .sex(1)
//            .birth("2000.01.01")
//            .phone("010-0000-0000")
//            .school("test고등학교")
//            .build();
//
//        User savedUser = userRepository.save(loginUser);
//
//        Club joinClub = Club.builder()
//            .name("CookingClass")
//            .user(savedUser)
//            .clubDesc("Enjoy Cooking class together ...")
//            .region("서울")
//            .field(1)
//            .activityType(1)
//            .build();
//
//        Club savedClub = clubRepository.save(joinClub);
//
//        // 동아리 회원 가입
//        savedClub.addMember(savedUser);
//
//        // 새 피드
//        BoardForm writtenform = new BoardForm("Day 1 cooking Class ...");
//        Board newBoard = writtenform.toBoard(savedUser,savedClub);
//
//        // when
//        // 피드 업로드
//        boardRepository.save(newBoard);
//
//        // then
//        Board result = boardRepository.findById(newBoard.getId()).orElseThrow(() -> new BoardException("피드 조회 오류"));
//        assertThat(result.getId()).isEqualTo(newBoard.getId());
//    }
//
//    @Test
//    void 피드id로_피드하나조회() {
//        Long clubId = 2L;
//        Board findBoard = boardRepository.findById(clubId).orElseThrow(() -> new BoardException("피드 조회 오류"));
//        System.out.println("findBoardId "+ findBoard.getId()+ findBoard.getContent());
//    }
//
////    @Test
////    void 동아리id로_피드모두조회() {
////        Long clubId = 2L;
////        Board findBoard = boardRepository.findById(clubId).orElseThrow(() -> new BoardException("피드 조회 오류"));
////        System.out.println("findBoardId "+ findBoard.getId()+ findBoard.getContent());
////    }
//
//
//}