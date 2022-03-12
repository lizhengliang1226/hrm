package com.hrm.common.interceptor;

import cn.hutool.core.util.StrUtil;
import com.hrm.common.entity.ResultCode;
import com.hrm.common.exception.CommonException;
import com.hrm.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description jwt拦截器
 * @Author LZL
 * @Date 2022/3/12-4:53
 */
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    private JwtUtils jwtUtil;
    private static final String BEARER="Bearer";

    @Autowired
    public void setJwtUtil(JwtUtils jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String authorization = request.getHeader("Authorization");
        if (!StrUtil.isBlank(authorization) && authorization.startsWith(BEARER)) {
            String token = authorization.replace("Bearer ", "");
            final Claims claims = jwtUtil.parseToken(token);
            if (claims != null) {
                String apis = (String) claims.get("apis");
                HandlerMethod h = (HandlerMethod) handler;
                final String permCode = getPermCode(h);
                if(permCode != null){
                    if (apis.contains(permCode)) {
                        request.setAttribute("userClaims", claims);
                        return true;
                    }else{
                        throw new CommonException(ResultCode.UNAUTHORISE);
                    }
                }
                throw new CommonException(ResultCode.SERVER_ERROR);
            }
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }

    private String getPermCode(HandlerMethod handlerMethod) {
        if (handlerMethod.hasMethodAnnotation(DeleteMapping.class)) {
            DeleteMapping methodAnnotation = handlerMethod.getMethodAnnotation(DeleteMapping.class);
            return methodAnnotation.name();
        } else if (handlerMethod.hasMethodAnnotation(PutMapping.class)) {
            PutMapping methodAnnotation = handlerMethod.getMethodAnnotation(PutMapping.class);
            return methodAnnotation.name();
        } else if (handlerMethod.hasMethodAnnotation(GetMapping.class)) {
            GetMapping methodAnnotation = handlerMethod.getMethodAnnotation(GetMapping.class);
            return methodAnnotation.name();
        } else if (handlerMethod.hasMethodAnnotation(PostMapping.class)) {
            PostMapping methodAnnotation = handlerMethod.getMethodAnnotation(PostMapping.class);
            return methodAnnotation.name();
        }
        return null;
    }

}
