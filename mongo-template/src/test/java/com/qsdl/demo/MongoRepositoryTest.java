package com.qsdl.demo;

import com.qsdl.demo.pojo.MemberMongo;
import com.qsdl.demo.service.MemberMongoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoTemplateApplication.class)
public class MongoRepositoryTest {

    @Autowired
    private MemberMongoRepository memberMongoRepository;

    @Test
    public void testRepositoryMember() {
        MemberMongo membermongo = new MemberMongo();
        membermongo.setId(11);
        memberMongoRepository.save(membermongo);
//        Optional<MemberMongo> repository = memberMongoRepository.findById("6606c9e3fda3bda2687fd60a");
        Optional<MemberMongo> repository = memberMongoRepository.findById(5);
        MemberMongo byId = repository.orElse(null);
        Iterable<MemberMongo> page = memberMongoRepository.findAll(PageRequest.of(0, 2));
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        List<MemberMongo> all = memberMongoRepository.findAll();
        System.out.println(all);
    }

    @Test
    public void testRepositoryAddMember() {
        MemberMongo member = new MemberMongo();
        member.setMemberAccount("txwbice");
//        member.setFirstName("sky1");
        Example<MemberMongo> example = Example.of(member);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("memberAccount", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnoreCase("_class");
//        Example<MemberMongo> example = Example.of(member, matcher);
        List<MemberMongo> all = memberMongoRepository.findAll(example);
        Optional<MemberMongo> byId = memberMongoRepository.findById(5);
        System.out.println(byId);
    }

    @Test
    public void testMember() {
    }

    /*@org.junit.Test
    public void testAddMember() {
        // 86,84,83,82,78,28,22,21
        Member member = memberService.getById(21);
        MemberMongo memberMongo = new MemberMongo();
        BeanCopierUtils.copy(member, memberMongo);
        memberMongoService.addMemberMongo(member);
        //mongoTemplate.insert(memberMongo);
        // 86,84,83,82,78,28,22,21
        List<Member> members = memberService.list(new LambdaQueryWrapper<Member>().in(Member::getId, 22,28,78));
        List<MemberMongo> memberMongos = BeanCopierUtils.copyList(members, MemberMongo.class);
        memberMongoService.addBatchMemberMongo(members);
        //mongoTemplate.insertAll(memberMongos);
        System.out.println(member);
    }*/

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
        memberMongoRepository.deleteById(11);
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
