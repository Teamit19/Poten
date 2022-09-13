package com.example.poten.service;

import com.example.poten.config.SessionManager;
import com.example.poten.domain.Interest;
import com.example.poten.domain.User;
import com.example.poten.dto.auth.KakaoUserInfo;
import com.example.poten.dto.request.SignUpForm;
import com.example.poten.dto.response.SessionResponse;
import com.example.poten.dto.response.UserResponse;
import com.example.poten.repository.InterestRepository;
import com.example.poten.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class UserService {

    @Value("AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC")
    String ADMIN_TOKEN;
    private final OAuthService oAuthService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final SessionManager sessionManager;


    private final UserRepository userRepository;

    private final InterestRepository interestRepository;

    public UserService(OAuthService oAuthService, SessionManager sessionManager, UserRepository userRepository, InterestRepository interestRepository) {
        this.oAuthService = oAuthService;
        this.sessionManager = sessionManager;
        this.userRepository = userRepository;
        this.interestRepository = interestRepository;
    }

    public void signUp(SignUpForm signUpForm, Long id) {
        Optional<User> user = userRepository.findById(id);

        user.get().update(signUpForm);

    }

    public Boolean saveInterest(User loginUser, List<String> interestList) {

        for (String interest : interestList) {
            Interest newInterest = new Interest(interest, loginUser);
            interestRepository.save(newInterest);

        }

        return true;

    }

    public UserResponse getInfo(Long id) {

        Optional<User> user = userRepository.findById(id);
        UserResponse userResponse=user.get().toResponse();

        return userResponse;

    }

    /**
     * 현재 로그인 사용자
     */
    public User getLoginUser(HttpServletRequest request) throws LoginException {
        String sessionId = request.getHeader(SessionManager.SESSION_ID);
        log.info("클라이언트로 부터 요청받은 쿠키(세션값) = {}", sessionId);
        return sessionManager.getLoginUser(sessionId);
    }


    /**
     * 카카오 로그인
     */
    public SessionResponse kakaoLogin(String token, HttpServletRequest request) {
        KakaoUserInfo userInfo = oAuthService.getUserInfoByToken(token);

//        KakaoUserInfo userInfo = oAuthService.getUserInfo(code);
        String email = userInfo.getKakaoEmail();
        String pw = userInfo.getId() + ADMIN_TOKEN;
        String age = userInfo.getAgeRange();
        User user = null;
        String encodedPassword = "";

        //회원 연령대 확인
        String [] ages=age.split("~");
        Integer ageEnd = Integer.parseInt(ages[1]);
        System.out.println("before if"+ageEnd);

        if ( ageEnd < 30) {
            //회원가입 진행
            System.out.println("after if"+pw);

            if (!userRepository.existsByEmail(email)) {
                log.info("save user");
                encodedPassword = passwordEncoder.encode(pw);
                user = User.builder().email(email).password(encodedPassword).build();
                userRepository.save(user);
            }

            user = userRepository.findByEmail(email);

        }


        HttpSession session = request.getSession();
        log.info("방금 로그인 처리할 새로생성된 회원의 세션값 = {}", session.getId());
        sessionManager.save(SessionManager.PREFIX + session.getId(), user);
        System.out.println("로그인 완료");

        return new SessionResponse(user.toResponse(), SessionManager.PREFIX + session.getId());




    }



}
