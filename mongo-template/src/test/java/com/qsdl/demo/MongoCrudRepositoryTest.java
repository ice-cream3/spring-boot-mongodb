package com.qsdl.demo;

import com.qsdl.demo.pojo.MemberMongo;
import com.qsdl.demo.service.MemberMongoCrudRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoTemplateApplication.class)
public class MongoCrudRepositoryTest {

    @Autowired
    private MemberMongoCrudRepository memberMongoCrudRepository;

    @Test
    public void testRepositoryMember() {
        MemberMongo membermongo = memberMongoCrudRepository.findById(5).get();
        membermongo.setId(11);
        memberMongoCrudRepository.save(membermongo);
        //Optional<MemberMongo> repository = memberMongoRepository.findById("6606c9e3fda3bda2687fd60a");
        Optional<MemberMongo> repository = memberMongoCrudRepository.findById(5);
        MemberMongo byId = repository.orElse(null);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        Iterable<MemberMongo> all = memberMongoCrudRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testRepositoryAddMember() {
        List<MemberMongo> likes = memberMongoCrudRepository.findByMemberAccountLike("txxb");
        List<MemberMongo> byIds = memberMongoCrudRepository.findByCid(35);
        List<MemberMongo> members = memberMongoCrudRepository.findByCidAndSiteId(35, 88);
        MemberMongo byId = memberMongoCrudRepository.findById(5).get();
        System.out.println(byId);
    }

    @Test
    public void testMember() {
    }

    @Test
    public void testUpdateMember() {
        /*Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        MemberMongo memberMongo = mongoTemplate.findOne(query, MemberMongo.class);
        Update update = new Update();
        update.set("area_code", "北京");
        mongoTemplate.updateFirst(query, update, MemberMongo.class);*/
    }

    @Test
    public void testDeleteMember() {
        // 21
        memberMongoCrudRepository.deleteById(11L);
        /*MemberMongo mongoTemplateOne = mongoTemplate.findOne(Query.query(Criteria.where("id").is(21)), MemberMongo.class);
        if (null != mongoTemplateOne) {
            //mongoTemplate.remove(mongoTemplateOne);
        }
        // 86,84,83,82,78,28,22,21
        memberMongoService.deleteByIds(Arrays.asList(22L, 28L, 78L));
        Query query = Query.query(Criteria.where("id").in(22, 28, 78));
        List<MemberMongo> members = mongoTemplate.find(query, MemberMongo.class);
        //mongoTemplate.findAndRemove(query, MemberMongo.class);
        //mongoTemplate.findAllAndRemove(query, MemberMongo.class);
        System.out.println(members);*/
    }

}
