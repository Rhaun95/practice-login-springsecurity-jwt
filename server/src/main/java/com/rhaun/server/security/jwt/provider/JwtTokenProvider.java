package com.rhaun.server.security.jwt.provider;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.rhaun.server.dto.CustomUser;
import com.rhaun.server.dto.UserAuth;
import com.rhaun.server.dto.Users;
import com.rhaun.server.mapper.UserMapper;
import com.rhaun.server.prop.JwtProps;
import com.rhaun.server.security.jwt.constants.JwtConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * 토큰생성
 * 토큰해석
 * 토큰 유효성 검사
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Autowired
    private JwtProps jwtProps;

    @Autowired
    private UserMapper userMapper;


    public String createToken(int userNo, String userId, List<String> roles) {

        // JWT building
        String jwt = Jwts.builder()
                .signWith(getShaKey(), Jwts.SIG.HS512) // .signWith( 시크릿키,알고리즘);
                .header()
                    .add("typ", JwtConstants.TOKEN_TYPE)
                .and()
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 5)) // token 만료시간(5일)
                .claim("uno", userNo) // PAYLOAD : uid : user (사용자 아이디)
                .claim("uid", userId) // PAYLOAD : uid : user
                .claim("rol", roles) // PAYLOAD : rol : [ROLE_USER, ROLE_ADMIN] (권환 정보)
                .compact(); // 최종 Token 생성

        log.info("jwt: " + jwt);

        return jwt;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String authHeader) {
        if (authHeader == null || authHeader.length() == 0)
            return null;

        try {
            String jwt = authHeader.replace(JwtConstants.TOKEN_PREFIX, "");

            // JWT Parsing
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);
            log.info("parsedToken : " + parsedToken);

            String userNo = parsedToken.getPayload().get("uno").toString();
            int no = (userNo == null ? 0 : Integer.parseInt(userNo));
            log.info("userNo : " + userNo);

            String userId = parsedToken.getPayload().get("uid").toString();
            log.info("userId : " + userId);

            Claims claims = parsedToken.getPayload();
            Object roles = claims.get("rol");
            log.info("roles : " + roles);

            if (userId == null || userId.length() == 0)
                return null;

            // Users 객체에 정보 추가
            Users user = new Users();
            user.setNo(no);
            user.setUserId(userId);

            // Users 객체에 권한 추가
            List<UserAuth> authList = ((List<?>) roles)
                    .stream()
                    .map(auth -> new UserAuth(userId, auth.toString()))
                    .collect(Collectors.toList());
            user.setAuthList(authList);

            // CustomUser에 권한 담기
            List<SimpleGrantedAuthority> authorities = ((List<?>) roles)
                    .stream()
                    .map(auth -> new SimpleGrantedAuthority((String) auth))
                    .collect(Collectors.toList());

            try {
                Users userInfo = userMapper.select(no);
                if (userInfo != null) {
                    user.setName(userInfo.getName());
                    user.setEmail(userInfo.getEmail());
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                log.error("토큰 유효 -> DB 추가 정보 조회시 에서 발생...");
            }

            UserDetails UserDetails = new CustomUser(user);

            return new UsernamePasswordAuthenticationToken(UserDetails, null, authorities);

        } catch (ExpiredJwtException exception) {
            log.warn("Request to parse expired JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            log.warn("Request to parse unsupported JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (MalformedJwtException exception) {
            log.warn("Request to parse invalid JWT : {} failed : {}", authHeader, exception.getMessage());
        } catch (IllegalArgumentException exception) {
            log.warn("Request to parse empty or null JWT : {} failed : {}", authHeader, exception.getMessage());
        }

        return null;
    }

    /**
     * 토큰 유효성 검사
     * 
     * @param jwt
     * @return
     */
    public boolean validateToken(String jwt) {

        try {
            Jws<Claims> parsedToken = Jwts.parser()
                    .verifyWith(getShaKey())
                    .build()
                    .parseSignedClaims(jwt);
            log.info("##### 토큰 만료기간 #####");
            log.info("-> " + parsedToken.getPayload().getExpiration());

            Date exp = parsedToken.getPayload().getExpiration();

            // 만료시간과 현재시간 비교
            return !exp.before(new Date());

        } catch (ExpiredJwtException exception) { // 토큰 만료
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) { // 토큰 손상
            log.error("Token Tampered");
            return false;

        } catch (NullPointerException exception) { // 토큰 없음
            log.error("Token is null");
            return false;

        } catch (Exception e) {
            return false;
        }

    }

    // secretKey -> signinKey
    private byte[] getSigningKey() {
        return jwtProps.getSecretKey().getBytes();
    }

    // secretKey -> HMAC-SHA Algorithms -> signingKey
    private SecretKey getShaKey() {
        return Keys.hmacShaKeyFor(getSigningKey());
    }

}
