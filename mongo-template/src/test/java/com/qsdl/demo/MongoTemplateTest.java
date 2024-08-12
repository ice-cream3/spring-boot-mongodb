package com.qsdl.demo;

import com.qsdl.demo.pojo.MemberMongo;
import com.qsdl.demo.service.MemberMongoService;
import com.qsdl.demo.utils.BeanCopierUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoTemplateApplication.class)
public class MongoTemplateTest {

    @Autowired
    private MemberMongoService memberMongoService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testRepositoryMember() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        MemberMongo memberMongo = mongoTemplate.findOne(query, MemberMongo.class);
        System.out.println(memberMongo);
    }

    @org.junit.Test
    public void testRepositoryAddMember() {
        MemberMongo byId = memberMongoService.getById(5L);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        List<MemberMongo> memberMongos = mongoTemplate.find(new Query(), MemberMongo.class);
        MemberMongo memberMongo = mongoTemplate.findOne(query, MemberMongo.class);
        System.out.println(memberMongo);
    }

    @org.junit.Test
    public void testMember() {
        MemberMongo byId = memberMongoService.getById(5L);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        List<MemberMongo> memberMongos = mongoTemplate.find(new Query(), MemberMongo.class);
        MemberMongo memberMongo = mongoTemplate.findOne(query, MemberMongo.class);
        System.out.println(memberMongo);
    }

    @org.junit.Test
    public void testAddMember() {
        // 86,84,83,82,78,28,22,21
        MemberMongo member = memberMongoService.getById(21);
        MemberMongo memberMongo = new MemberMongo();
        BeanCopierUtils.copy(member, memberMongo);
        memberMongoService.addMemberMongo(member);
        //mongoTemplate.insert(memberMongo);
        // 86,84,83,82,78,28,22,21
        List<MemberMongo> members = memberMongoService.getByIds(Arrays.asList(22L,28L,78L));
        List<MemberMongo> memberMongos = BeanCopierUtils.copyList(members, MemberMongo.class);
        memberMongoService.addBatchMemberMongo(members);
        //mongoTemplate.insertAll(memberMongos);
        System.out.println(member);
    }

    @org.junit.Test
    public void testUpdateMember() {
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        MemberMongo memberMongo = mongoTemplate.findOne(query, MemberMongo.class);
        Update update = new Update();
        update.set("area_code", "北京");
        mongoTemplate.updateFirst(query, update, MemberMongo.class);
    }

    @Test
    public void testDeleteMember() {
        // 21
        memberMongoService.deleteById(21);
        MemberMongo mongoTemplateOne = mongoTemplate.findOne(Query.query(Criteria.where("id").is(21)), MemberMongo.class);
        if (null != mongoTemplateOne) {
            //mongoTemplate.remove(mongoTemplateOne);
        }
        // 86,84,83,82,78,28,22,21
        memberMongoService.deleteByIds(Arrays.asList(22L, 28L, 78L));
        Query query = Query.query(Criteria.where("id").in(22, 28, 78));
        List<MemberMongo> members = mongoTemplate.find(query, MemberMongo.class);
        //mongoTemplate.findAndRemove(query, MemberMongo.class);
        //mongoTemplate.findAllAndRemove(query, MemberMongo.class);
        System.out.println(members);
    }

}
