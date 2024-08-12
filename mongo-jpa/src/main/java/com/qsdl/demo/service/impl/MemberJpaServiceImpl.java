package com.qsdl.demo.service.impl;

import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName: MemberMongoServiceImpl
 * @Description:
 * @Author: dream
 * @Date: 2024/3/30 20:33
 */
@Component
public class MemberJpaServiceImpl {

    @Resource
    private MongoTemplate mongoTemplate;

}
