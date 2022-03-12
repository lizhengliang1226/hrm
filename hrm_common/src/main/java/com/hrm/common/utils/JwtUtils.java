package com.hrm.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/11-8:07
 */
@Getter
@Setter
@Component
@ConfigurationProperties("jwt.config")
public class JwtUtils {

    private String key;
    private Long ttl;

    public String createJwt(String id, String name, Map<String, Object> map) {
        // 设置失效时间
        Long now = System.currentTimeMillis();
        long exp = now + ttl;
        final JwtBuilder jwtBuilder = Jwts.builder()
                .setExpiration(new Date(exp))
                .setId(id).signWith(SignatureAlgorithm.HS256, key)
                .setSubject(name).setIssuedAt(new Date());
        map.forEach(jwtBuilder::claim);
        return jwtBuilder.compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
    }

//    public static void main(String[] args) throws InvalidKeyException {
//        final JwtUtils jwtUtils = new JwtUtils();
//        Map<String,Object> map=new HashMap<>();
//        map.put("class","aaaaasss");
//        final String jwt = jwtUtils.createJwt("aa", "aa", map);
//        final Claims claims = jwtUtils.parseToken(jwt);
//        System.out.println(claims.get("class"));
//    }
}
