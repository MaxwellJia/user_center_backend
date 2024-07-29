package com.wangtao.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wangtao.usercenter.model.domain.User;
import com.wangtao.usercenter.service.UserService;
import com.wangtao.usercenter.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author lenovo
* @description 针对表【user(Save users)】的数据库操作Service实现
* @createDate 2024-07-29 16:44:08
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




