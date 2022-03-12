package com.hrm.common.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description
 * @Author LZL
 * @Date 2022/3/7-20:28
 */
public class BaseController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    protected Claims claims;
    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;
        final Claims claims =(Claims) request.getAttribute("userClaims");
        if(claims != null){
            this.claims = claims;
            this.companyId= (String) claims.get("companyId");
            this.companyName = (String) claims.get("companyName");
        }

    }

}
