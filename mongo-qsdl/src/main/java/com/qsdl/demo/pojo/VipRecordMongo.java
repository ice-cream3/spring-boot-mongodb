package com.qsdl.demo.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Document("vip_record")
public class VipRecordMongo implements Serializable {

    @Id
    @MongoId(targetType = FieldType.OBJECT_ID)
    private String id;

    @Field(value = "tid")
    private Integer tid;

    @Field(value = "cid")
    private Integer cid;

    @Field(value = "cagent")
    private String cagent;

    @Field(value = "siteId")
    private Integer siteId;

    @Field(value = "site")
    private String site;

    @Field(value = "memberId")
    private Long memberId;

    @Field(value = "memberAccount")
    private String memberAccount;

    @Field(value = "memberRealName")
    private String memberRealName;

    @Field(value = "currency")
    private String currency;

    @Field(value = "userCount")
    private String userCount;

    @Field(value = "profit")
    private Double profit;
    @Field(value = "vipStartTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipStartTime;
    @Field(value = "vipEndTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date vipEndTime;
    @Field(value = "pullTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pullTime;
    @Field(value = "lineCode")
    private String lineCode;
    @Field(value = "agentId")
    private String agentId;
    @Field(value = "result")
    private String result;
    @Field(value = "vipType")
    private String vipType;
    @Field(value = "vipCode")
    private String vipCode;
}