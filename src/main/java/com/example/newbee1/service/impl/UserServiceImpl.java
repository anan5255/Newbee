package com.example.newbee1.service.impl;

import com.example.newbee1.common.ServiceEnum;
import com.example.newbee1.domain.User;
import com.example.newbee1.domain.UserToken;
import com.example.newbee1.domain.repo.UserRepo;
import com.example.newbee1.domain.repo.UserTokenRepo;
import com.example.newbee1.uitl.NumberUtil;
import com.example.newbee1.uitl.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
@Service
public class UserServiceImpl {
    private UserRepo userRepo;

    private UserTokenRepo userTokenRepo;

    private NumberUtil numberUtil;

    private SystemUtil systemUtil;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserTokenRepo userTokenRepo, NumberUtil numberUtil, SystemUtil systemUtil) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
        this.numberUtil = numberUtil;
        this.systemUtil = systemUtil;
    }

    @Override
    public String register(String loginName, String password){
        if (userRepo.findUserByLoginName(loginName) != null) {
            return ServiceEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        User registerUser = new User();
        registerUser.setLoginName(loginName);
        registerUser.setNickName(loginName);
        registerUser.setIntroduceSign("Welcome!");
        registerUser.setPasswordMd5(password);
        if (userRepo.save(registerUser) != null) {
            return ServiceEnum.SUCCESS.getResult();
        }
        return ServiceEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String password){
        User user = userRepo.findUserByLoginNameAndPassword(loginName, password);
        if (user == null) {
            return ServiceEnum.LOGIN_ERROR.getResult();

        }
        //登录后即执行修改token的操作
        String token = getNewToken(System.currentTimeMillis() + "", user.getUserId());
        UserToken userToken = userTokenRepo.findUserTokenByUserId(user.getUserId());
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
        if (userToken == null) {
            userToken = new UserToken();
            userToken.setUserId(user.getUserId());
        }
        userToken.setToken(token);
        userToken.setUpdateTime(now);
        userToken.setExpireTime(expireTime);
        userTokenRepo.save(userToken);
        return token;
    }





    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + NumberUtil.genRandomNum(4);
        return SystemUtil.genToken(src);
    }
}
