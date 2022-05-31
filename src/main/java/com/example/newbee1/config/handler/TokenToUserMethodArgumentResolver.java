package com.example.newbee1.config.handler;

import com.example.newbee1.common.Constants;
import com.example.newbee1.config.annotation.TokenToUser;
import com.example.newbee1.domain.User;
import com.example.newbee1.domain.UserToken;
import com.example.newbee1.domain.repo.UserRepo;
import com.example.newbee1.domain.repo.UserTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private UserRepo userRepo;
    private UserTokenRepo userTokenRepo;

    @Autowired
    public TokenToUserMethodArgumentResolver(UserRepo userRepo, UserTokenRepo userTokenRepo) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToUser.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("token");
        if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenRepo.findUserTokenByToken(token);
            if (userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                throw new RuntimeException("TOKEN_EXPIRE_ERROR");
            }
            User user = userRepo.findUserById(userToken.getUserId());
            if (user == null) {
                throw new RuntimeException("USER_NULL_ERROR");
            }
            return user;
        } else {
            throw new RuntimeException("NOT_LOGIN_ERROR");
        }
    }
}
