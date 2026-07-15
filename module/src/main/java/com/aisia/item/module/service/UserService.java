package com.aisia.item.module.service;

import com.aisia.item.module.entity.UserEntity;
import com.aisia.item.module.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author kaikai
 * @since 2026-07-14
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int insert(UserEntity userEntity){
        return userMapper.insert(userEntity);
    }

    public int update(UserEntity userEntity){
        return userMapper.update(userEntity);
    }

    public UserEntity getById(Long id){
        return userMapper.getById(id);
    }

    public UserEntity extractById(Long id){
        return userMapper.extractById(id);
    }

    public int delete(Long id){
        return userMapper.delete(id);
    }

    public UserEntity queryByUsername(String username) {
        return userMapper.queryByUsername(username);
    }
}
