package com.aisia.item.module.service;

import com.aisia.item.module.entity.TestEntity;
import com.aisia.item.module.mapper.TestMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kaikai
 * @since 2026-07-02
 */
@Service
public class TestService {

    @Autowired
    private TestMapper TestMapper;

    public int insert(TestEntity testEntity) {
        return TestMapper.insert(testEntity);
    }

    public int update(TestEntity testEntity) {
        return TestMapper.update(testEntity);
    }

    public TestEntity getById(Long id) {
        return TestMapper.getById(id);
    }

    public TestEntity extractById(Long id) {
        return TestMapper.extractById(id);
    }

    public int delete(Long id) {
        return TestMapper.delete(id);
    }
}
