package com.rhaun.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import com.rhaun.server.dto.UserAuth;
import com.rhaun.server.dto.Users;
import com.rhaun.server.mapper.UserMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    /*
     * 회원 가입
     * 1. 비밀번호 암호화
     * 2. 회원 등록
     * 3. 권한 등록
     */
    @Override
    public int insert(Users user) throws Exception {

        // 비밀번호 암호화
        String userPw = user.getUserPw();
        String encodedPw = passwordEncoder.encode(userPw);
        user.setUserPw(encodedPw);

        // 회원 등록
        int result = userMapper.insert(user);

        // 권한 등록
        if (result > 0) {
            UserAuth userAuth = new UserAuth();
            userAuth.setUserId(user.getUserId());
            userAuth.setAuth("ROLE_USER");
            result = userMapper.insertAuth(userAuth);
        }

        return result;
    }

    // 회원 조회
    @Override
    public Users select(int userNo) throws Exception {
        System.err.println("####################: " + userMapper.select(userNo));
        return userMapper.select(userNo);
    }

    // 로그인
    @Override
    public void login(Users user, HttpServletRequest request) throws Exception {
        String username = user.getUserId();
        String password = user.getUserPw();

        log.info("username : " + username);
        log.info("password : " + password);

        // AuthenticationManager
        // 아이디, 패스워드 인증 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // 토큰에 요청정보 등록
        token.setDetails(new WebAuthenticationDetails(request));

        // 토큰으로 인증 요청 - 로그인- DB의 ID와 암호화된 패스워드와 일치하는지 확인, 일치 시 Authentication 객채 반환
        Authentication authentication = authenticationManager.authenticate(token);
        log.info("인증 여부: " + authentication.isAuthenticated());
        User authUser = (User) authentication.getPrincipal();
        log.info("인증된 사용자 아이디 : " + authUser.getUsername());

        // SecurityContext에 인증된 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

    // Update
    @Override
    public int update(Users user) throws Exception {
        String userPw = user.getUserPw();
        String encodedPw = passwordEncoder.encode(userPw);
        user.setUserPw(encodedPw);

        int result = userMapper.update(user);

        return result;
    }

    @Override
    public int delete(String userId) throws Exception {
        return userMapper.delete(userId);
    }

}
