package com.qsdl.demo.service;

import com.qsdl.demo.pojo.VipRecordMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @ClassName: VipRecordRepository
 */
public interface VipRecordRepository extends MongoRepository<VipRecordMongo, String>,
        CrudRepository<VipRecordMongo, String>, QuerydslPredicateExecutor<VipRecordMongo> {
}
