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

    public Boolean signUp(SignUpForm signUpForm, Long id) {
        Optional<User> user = userRepository.findById(id);

        user.get().update(signUpForm);

        return true;

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
     * ?????? ????????? ?????????
     */
    public User getLoginUser(HttpServletRequest request) throws LoginException {
        String sessionId = request.getHeader(SessionManager.SESSION_ID);
        log.info("?????????????????? ?????? ???????????? ??????(?????????) = {}", sessionId);
        return sessionManager.getLoginUser(sessionId);
    }


    /**
     * ????????? ?????????
     */
    public SessionResponse kakaoLogin(String token, HttpServletRequest request) {
        //?????????????????? ????????? ??????????????? ?????????
        KakaoUserInfo userInfo = oAuthService.getUserInfoByToken(token);

        //????????? ????????? ??????????????? ?????????
//        KakaoUserInfo userInfo = oAuthService.getUserInfo(token);
        String email = userInfo.getKakaoEmail();
        String pw = userInfo.getId() + ADMIN_TOKEN;
        String age = userInfo.getAgeRange();
        User user = null;
        String encodedPassword = "";

        //?????? ????????? ??????
        String [] ages=age.split("~");
        Integer ageEnd = Integer.parseInt(ages[1]);
        System.out.println("before if"+ageEnd);

        if ( ageEnd < 30) {
            //???????????? ??????
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
        log.info("?????? ????????? ????????? ??????????????? ????????? ????????? = {}", session.getId());
        sessionManager.save(SessionManager.PREFIX + session.getId(), user);
        System.out.println("????????? ??????");

        return new SessionResponse(user.toResponse(), SessionManager.PREFIX + session.getId());




    }



}
