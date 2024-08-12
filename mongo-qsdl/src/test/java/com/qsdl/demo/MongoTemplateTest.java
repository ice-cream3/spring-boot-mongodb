package com.qsdl.demo;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.qsdl.demo.pojo.MemberMongo;
import com.qsdl.demo.pojo.MemberVipInfoPageVO;
import com.qsdl.demo.pojo.QVipRecordMongo;
import com.qsdl.demo.pojo.VipRecordMongo;
import com.qsdl.demo.service.MemberMongoService;
import com.qsdl.demo.service.VipRecordRepository;
import com.qsdl.demo.utils.BeanCopierUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.assertj.core.util.DateUtil;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MongoQsdlApplication.class)
public class MongoTemplateTest {

    @Autowired
    private MemberMongoService memberMongoService;

    @Autowired
    private VipRecordRepository vipRecordRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testGroupByVip() {
        MemberVipInfoPageVO vo = new MemberVipInfoPageVO();
        Criteria criteria = new Criteria("vipStartTime");
        criteria.gte(DateUtil.parse(vo.getStartTime())).lte(DateUtil.parse(vo.getEndTime()));
        criteria.and("vipType").is(vo.getType());
        criteria.and("vipCode").is(vo.getVipCode());
        Document matchDoc = criteria.getCriteriaObject();
        // 创建聚合管道
        List<Document> queryParam = Arrays.asList(
                // 匹配所有文档（可以根据需要添加或修改匹配条件）
                new Document("$match", new Document("cid",35)), //new Document("$match", matchDoc),
                // 分组文档，并计算每个组的数量
                new Document("$group",
                        new Document("_id",
                                new Document("vipAccount", "$vipAccount")
                                        .append("vipTime", new Document("$dateToString", new Document("format","%Y-%m-%d").append("date", "$vipStartTime")))
                                        .append("vipType","$vipType")
                                        .append("vipCode","$vipCode")
                        )
                                .append("profit", new Document("$sum", "$profit"))
                                .append("$vipAmount", new Document("$sum", "$vipAmount"))
                                .append("vipCount", new Document("$sum", 1))
                ),
                new Document("$sort", new Document("_id",-1)),
                new Document("$skip", 0),
                new Document("$limit", 2)
        );
        MongoCollection<Document> collection = mongoTemplate.getCollection("bet_record");
        AggregateIterable<Map> pageResult = collection.aggregate(queryParam, Map.class);

        // 遍历并打印结果
        for (Map doc : pageResult) {
            System.out.println("结果:" + doc);
            System.out.println("结果:" + doc.get("_id"));
        }

        // 创建聚合管道
        List<Document> countParam = Arrays.asList(
                // 匹配所有文档（可以根据需要添加或修改匹配条件）
                new Document("$match", new Document("cid",35)),
                // 分组文档，并计算每个组的数量
                new Document("$group",
                        new Document("_id",
                                new Document("vipAccount", "$vipAccount")
                                        .append("vipStartTime", new Document("$dateToString", new Document("format","%Y-%m-%d").append("date", "$vipStartTime")))
                                        .append("vipType","$vipType")
                                        .append("vipCode","$vipCode")
                        )
                                .append("totalProfit", new Document("$sum", "$profit"))
                                .append("totalvipAmount", new Document("$sum", "$vipAmount"))
                                .append("count", new Document("$sum", 1))
                ),
                // 分组文档，并计算每个组的数量
                new Document("$group", new Document("_id", null).append("count", new Document("$sum", 1)))
        );
        AggregateIterable<Map> countResult = collection.aggregate(countParam, Map.class);
        AggregateIterable<Map> countResult2 = collection.aggregate(countParam, Map.class);
        int count = Integer.parseInt(Objects.requireNonNull(countResult.first()).get("count").toString());
        System.out.println("统计结果:" + count);

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        BucketOperation bucketOperation = new BucketOperation(queryParam);
//        new ProjectionOperation().toDocument(new TypeBasedAggregationOperationContext().getMappedObject())
        Document document = new Document("_id",
                new Document("vipAccount", "$vipAccount")
                        .append("vipStartTime", new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", "$vipStartTime")))
                        .append("vipType", "$vipType")
                        .append("vipCode", "$vipCode")
        );
        //new AggregationOperation();
        GroupOperation vipAccount = new GroupOperation(Fields.fields("vipAccount","vipType","vipCode","$dateToString: { format: \"%Y-%m-%d\", date: $vipStartTime\\}"));
        AddFieldsOperation addFieldsOperation =
                new AddFieldsOperation("$group", document);//.addField("vipAccount", "$vipAccount").addField("vipType", "$vipType");
        Aggregation aggregationTest = Aggregation.newAggregation(
                addFieldsOperation,vipAccount
                /*new AggregationOperation() {
                    @Override
                    public Document toDocument(AggregationOperationContext context) {
                        return new Document("$group",
                                new Document("_id",
                                        new Document("vipAccount", "$vipAccount")
                                                .append("vipStartTime", new Document("$dateToString", new Document("format","%Y-%m-%d").append("date", "$vipStartTime")))
                                                .append("vipType","$vipType")
                                                .append("vipCode","$vipCode")
                                )
                                        .append("totalProfit", new Document("$sum", "$profit"))
                                        .append("totalValidVipAmount", new Document("$sum", "$validVipAmount"))
                                        .append("count", new Document("$sum", 1))
                        );
                    }

                    @Override
                    public List<Document> toPipelineStages(AggregationOperationContext context) {
                        return AggregationOperation.super.toPipelineStages(context);
                    }

                    @Override
                    public String getOperator() {
                        return AggregationOperation.super.getOperator();
                    }
                }*/
        );
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("vipAccount","vipType","vipCode","vipStartTime")
        );
        AggregationResults<Map> vipRecord1 = mongoTemplate.aggregate(aggregationTest, "vip_record", Map.class);
        System.out.println(vipRecord1.getMappedResults());
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        /*// 分组
        GroupBy groupBy = new GroupBy("vipAccount");
        // 统计数量值的key，可以任意字符串，例如total
        final String count = "count";
        DBObject initial = new BasicDBObject();
        // 初始值为0
        initial.put(count, 0);
        // reduce必须要定义，否则调用MongoDB的group方法时会抛出异常
        String reduce = "function (document, output) { "
                // 统计数量值的key自增
                + " output." + count + "++; }";
        // 可以不用定义finalize变量
        String finalize = "function (output) {return output;}";
        groupBy = groupBy.reduceFunction(reduce); // 必须要设置，不能为null，该函数的含义是在分组操作时定义一个操作文档的聚合函数
        groupBy = groupBy.collation(null); // 设置为null吧
        groupBy = groupBy.finalizeFunction(finalize); // 可以设置为null，该函数的含义是在group函数返回最终值之前，定义一个运行每个分组的结果集的函数
        groupBy = groupBy.initialDocument(initial.toString()); // 初始化分组统计数量
        String criteriaKey = "cid"; // 过滤的条件
        String criteriaValue = "35"; // 过滤值
        Criteria criteria = Criteria.where("cid").is(35);
        GroupByResults<Map> byResults = mongoTemplate.group(criteria, "Vip_record", groupBy, Map.class);*/

        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // 构建GroupBy操作的聚合管道
        Document groupByFields = new Document("_id", "$vipAccount");
        groupByFields.put("total", new Document("$sum", "$profit"));
        Document groupByStage = new Document("$group", groupByFields);
        Aggregation groupByStages = null;
        //mongoTemplate.aggregate(groupByStages);

        QVipRecordMongo VipRecord = QVipRecordMongo.vipRecordMongo;
        BooleanExpression expression = VipRecord.cid.eq(35);
        if (1==1) {
            expression = expression.and(VipRecord.tid.between(2900,3000));
        }
        Iterable<VipRecordMongo> all = vipRecordRepository.findAll(expression);
        Pageable pageable = PageRequest.of(0, 1);
        Page<VipRecordMongo> page = vipRecordRepository.findAll(expression, pageable);
        System.out.println(6666);
    }

    @Test
    public void testQDSLRepositoryVip() {
        QVipRecordMongo VipRecord = QVipRecordMongo.vipRecordMongo;
        BooleanExpression expression = VipRecord.cid.eq(35);
        if (1==1) {
            expression = expression.and(VipRecord.tid.between(2900,3000));
        }
        Iterable<VipRecordMongo> all = vipRecordRepository.findAll(expression);
        Pageable pageable = PageRequest.of(0, 1);
        Page<VipRecordMongo> page = vipRecordRepository.findAll(expression, pageable);
        System.out.println(6666);
    }

    @Test
    public void testRepositoryVip() {
        VipRecordMongo VipRecord = new VipRecordMongo();
        VipRecord.setCid(35);
        VipRecord.setMemberId(999L);
        // 创建一个字符串匹配器
        ExampleMatcher.StringMatcher stringMatcher = ExampleMatcher.StringMatcher.valueOf(ExampleMatcher.StringMatcher.ENDING.name());
        // 创建一个匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                // 忽略指定字段"***"的查询条件
                .withIgnorePaths("memberId", "vipType")
                // 在上面的条件基础上 属性可以为null（包含空值）
                .withIncludeNullValues().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<VipRecordMongo> example = Example.of(VipRecord, matcher);
        Example<VipRecordMongo> objectExample = Example.of(VipRecord, matcher);
        long count = vipRecordRepository.count(objectExample);
        List<VipRecordMongo> all = vipRecordRepository.findAll(objectExample);
        Query query = new Query();
        Criteria criteria = new Criteria();
        criteria.alike(example);
        query.addCriteria(criteria);
        List<VipRecordMongo> all2 = vipRecordRepository.findAll(example);
        Pageable pageable = PageRequest.of(0, 1);
        Page<VipRecordMongo> page = vipRecordRepository.findAll(example, pageable);
        Sort sort = Sort.by("id");
        Pageable pageSort = PageRequest.of(0, 2, sort);
        Page<VipRecordMongo> sortPage = vipRecordRepository.findAll(example, pageSort);
        Sort sortDesc = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageSortDesc = PageRequest.of(0, 2, sortDesc);
        Page<VipRecordMongo> sortDescPage = vipRecordRepository.findAll(example, pageSortDesc);
        List<VipRecordMongo> all3 = mongoTemplate.find(query, VipRecordMongo.class);
        System.out.println(count);
    }

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
