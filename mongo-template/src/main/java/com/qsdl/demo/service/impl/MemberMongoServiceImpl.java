package com.qsdl.demo.service.impl;

import com.qsdl.demo.pojo.MemberMongo;
import com.qsdl.demo.service.MemberMongoService;
import com.qsdl.demo.utils.BeanCopierUtils;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MemberMongoServiceImpl
 * @Description:
 * @Author: dream
 * @Date: 2024/3/30 20:33
 */
@Component
public class MemberMongoServiceImpl implements MemberMongoService {

    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public MemberMongo getById(long id) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(id);
        query.addCriteria(criteria);
        List<MemberMongo> memberMongos = mongoTemplate.find(new Query(), MemberMongo.class);
        return mongoTemplate.findOne(query, MemberMongo.class);
    }

    @Override
    public List<MemberMongo> getByIds(List<Long> ids) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").in(ids);
        query.addCriteria(criteria);
        return mongoTemplate.find(query, MemberMongo.class);
    }

    @Override
    public MemberMongo addMemberMongo(MemberMongo member) {
        if (null == member) {
            return null;
        }
        MemberMongo memberMongo = new MemberMongo();
        BeanCopierUtils.copy(member, memberMongo);
        return mongoTemplate.insert(memberMongo);
    }

    @Override
    public List<MemberMongo> addBatchMemberMongo(List<MemberMongo> members) {
        if (CollectionUtils.isEmpty(members)) {
            return new ArrayList<>();
        }
        List<MemberMongo> memberMongos = BeanCopierUtils.copyList(members, MemberMongo.class);
        return (List<MemberMongo>) mongoTemplate.insertAll(memberMongos);
    }

    @Override
    public long deleteById(long id) {
        MemberMongo mongoTemplateOne = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), MemberMongo.class);
        if (null != mongoTemplateOne) {
            return 0L;
        }
        DeleteResult remove = mongoTemplate.remove(mongoTemplateOne);
        return remove.getDeletedCount();
    }

    @Override
    public List<MemberMongo> deleteByIds(List<Long> ids) {
        Query query = Query.query(Criteria.where("id").in(ids));
        List<MemberMongo> members = mongoTemplate.find(query, MemberMongo.class);
        if (CollectionUtils.isEmpty(members)) {
            return new ArrayList<>();
        }
        mongoTemplate.findAndRemove(query, MemberMongo.class);
        return mongoTemplate.findAllAndRemove(query, MemberMongo.class);
    }

    @Override
    public long update(MemberMongo member) {
        if (null == member) {
            return 0L;
        }
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(member.getId());
        query.addCriteria(criteria);
        Update update = new Update();
        if (StringUtils.isNotBlank(member.getAreaCode())) {
            update.set("area_code", member.getAreaCode());
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, MemberMongo.class);
            return updateResult.getModifiedCount();
        }
        return 0L;
    }
}
