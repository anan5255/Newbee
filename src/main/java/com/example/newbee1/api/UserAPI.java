package com.example.newbee1.api;

import com.example.newbee1.api.param.UserLoginParam;
import com.example.newbee1.api.param.UserRegisterParam;
import com.example.newbee1.common.ServiceEnum;
import com.example.newbee1.service.UserService;
import com.example.newbee1.uitl.PhoneUtil;
import com.example.newbee1.uitl.Result;
import com.example.newbee1.uitl.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
@Slf4j
@RestController
@RequestMapping("/api/v1")
public class UserAPI {

    private PhoneUtil phoneUtil;
    private ResultGenerator resultGenerator;
    private UserService userService;

    @Autowired
    public UserAPI(PhoneUtil phoneUtil, ResultGenerator resultGenerator) {
        this.phoneUtil = phoneUtil;
        this.resultGenerator = resultGenerator;
        this.userService = userService;
    }



    @PostMapping("/user/register")
    public Result register(@RequestBody @Valid UserRegisterParam param){
        if (phoneUtil.isNotPhoneNumber(param.getLoginName())){
            return resultGenerator.genFailResult(ServiceEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }


        String registerResult = userService.register(param.getLoginName(), param.getPassword());

        log.info("register api,loginName={},loginResult={}", param.getLoginName(), registerResult);

        //注册成功
        if (ServiceEnum.SUCCESS.getResult().equals(registerResult)) {
            return resultGenerator.genSuccessResult();
        }
        //注册失败
        return resultGenerator.genFailResult(registerResult);

    }

    @PostMapping("/user/login")
    public Result login(@RequestBody @Valid UserLoginParam param){
        if (phoneUtil.isNotPhoneNumber(param.getLoginName())){
            return resultGenerator.genFailResult(ServiceEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }


    }
}
