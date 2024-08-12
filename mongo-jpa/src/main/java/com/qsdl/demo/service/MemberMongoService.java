package com.qsdl.demo.service;


import com.qsdl.demo.pojo.MemberMongo;

import java.util.List;

public interface MemberMongoService extends BaseMongoRepository<MemberMongo, Long> {

    MemberMongo getById(long id);

    List<MemberMongo> getByIds(List<Long> ids);

    MemberMongo addMemberMongo(MemberMongo member);

    List<MemberMongo> addBatchMemberMongo(List<MemberMongo> members);

    long deleteById(long id);

    List<MemberMongo> deleteByIds(List<Long> ids);

    long update(MemberMongo member);
}
