package com.qsdl.demo.service;

import com.qsdl.demo.pojo.MemberMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberMongoRepository extends MongoRepository<MemberMongo, Integer>/*,
        org.springframework.data.repository.Repository<MemberMongo, Long>, PagingAndSortingRepository<MemberMongo, Long>*/ {


}
