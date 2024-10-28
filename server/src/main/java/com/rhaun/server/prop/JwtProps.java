package com.rhaun.server.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("com.rhaun.server") // application.properties 하위 속성 경로 지정
public class JwtProps {

    // JWT 시그니처 암호화를 위한 정보
    private String secretKey;
}
