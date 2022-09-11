package com.example.poten.config;

import com.example.poten.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SessionManager {

    public static final String SESSION_ID = "login";
    public static final String PREFIX = "JSESSIONID=";

    // concurrentHashMap 으로 해야 동시 접속 처리 가능(멀티쓰레드)
    private final Map<String, User> sessionStore = new ConcurrentHashMap<>();

    // 세션 값 저장
    public void save(String sessionId, User user) {
        sessionStore.put(sessionId, user);
    }

    // 세션 조회
    public User getLoginUser(String sessionId) throws LoginException {
        log.info("세션저장소 = {}", sessionStore);
        if (!sessionStore.containsKey(sessionId)) throw new LoginException("로그인 되어 있지 않음.");
        return sessionStore.get(sessionId);
    }

    // 세션 삭제
    public void expire(String sessionId) {
        sessionStore.remove(sessionId);
    }

}
