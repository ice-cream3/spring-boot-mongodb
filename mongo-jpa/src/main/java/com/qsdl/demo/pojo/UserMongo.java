package com.qsdl.demo.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * MemberMongo会员表
 */
@Data
@Entity(name = "user")
public class UserMongo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Field(value = "id")
    private Integer id;

    @Field(value = "cid")
    private Integer cid;

    @Field(value = "cagent")
    private String cagent;

    @Field(value = "site_id")
    private Integer siteId;

    @Field(value = "site")
    private String site;

    @Field(value = "type_id")
    private Integer typeId;

    @Field(value = "currency")
    private String currency;

    @Field(value = "member_account")
    private String memberAccount;

    @Field(value = "game_account")
    private String gameAccount;

    @Field(value = "password")
    private String password;

    @Field(value = "qk_password")
    private String qkPassword;

    @Field(value = "mobile")
    private String mobile;

    @Field(value = "area_code")
    private String areaCode;

    @Field(value = "real_name")
    private String realName;

    @Field(value = "last_name")
    private String lastName;

    @Field(value = "first_name")
    private String firstName;

    @Field(value = "gcode")
    private String gcode;

    @Field(value = "parent_member_id")
    private Long parentMemberId;

    @Field(value = "parent_member_account")
    private String parentMemberAccount;

    @Field(value = "superior_member_id")
    private Long superiorMemberId;

    @Field(value = "superior_member_account")
    private String superiorMemberAccount;

    @Field(value = "status")
    private Integer status;

    @Field(value = "remark")
    private String remark;

    @Field(value = "update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Field(value = "create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
