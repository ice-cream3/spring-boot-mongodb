package com.qsdl.demo.service;

import com.qsdl.demo.pojo.MemberMongo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMongoCrudRepository extends CrudRepository<MemberMongo, Long> {

    List<MemberMongo> findByMemberAccountLike(String memberAccount);

    List<MemberMongo> findByCid(Integer cid);

    List<MemberMongo> findByCidAndSiteId(Integer cid, Integer sid);

    Optional<MemberMongo> findById(Integer id);
}
