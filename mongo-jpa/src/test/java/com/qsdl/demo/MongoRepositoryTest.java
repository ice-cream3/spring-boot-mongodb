package com.qsdl.demo;

import com.qsdl.demo.pojo.MemberMongo;
import com.qsdl.demo.service.MemberJpaService;
import jakarta.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoTemplateApplication.class)
public class MongoRepositoryTest {

    @Resource
    private MemberJpaService memberJpaService;

    @Test
    public void testRepositoryMember() {
        MemberMongo membermongo = new MemberMongo();
        membermongo.setId(11);
        memberJpaService.save(membermongo);
//        Optional<MemberMongo> repository = memberMongoRepository.findById("6606c9e3fda3bda2687fd60a");
        Optional<MemberMongo> repository = memberJpaService.findById(5L);
        MemberMongo memberMongo = repository.get();
        membermongo.setId(16);
        memberJpaService.save(memberMongo);
        MemberMongo byId = repository.orElse(null);
        Iterable<MemberMongo> page = memberJpaService.findAll(PageRequest.of(0,2));
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.and("id").is(5);
        query.addCriteria(criteria);
        List<MemberMongo> all = memberJpaService.findAll();
        System.out.println(all);
    }

    @Test
    public void testRepositoryAddMember() {
        MemberMongo member = new MemberMongo();
        member.setMemberAccount("txxbsky001");
//        member.setFirstName("sky1");
        Example<MemberMongo> example = Example.of(member);
        // 定义条件匹配器,由于mongo在添加数据时会默认添加_class属性.所以在查询是要忽略些属性才能查询到数据
        // 如果不忽略_class属性,自定义转换类也是可以的,具体代码:com.example.demo.config.MongodbConfig#mappingMongoConverter
//        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
//                .withIgnorePaths("_id","_class")//忽略属性
//                .withNullHandler(ExampleMatcher.NullHandler.IGNORE)
//                .withIgnoreCase(true)//忽略大小写
//                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
//                .withMatcher("memberAccount", ExampleMatcher.GenericPropertyMatchers.contains());//采用“包含匹配”的方式查询
//        Example<MemberMongo> example = Example.of(member, exampleMatcher);
        List<MemberMongo> all = memberJpaService.findAll(example);
        MemberMongo byId = memberJpaService.findById(5L).get();
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
        memberJpaService.deleteById(11);
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
