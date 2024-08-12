package com.qsdl.demo.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseMongoRepository<T, ID> extends CrudRepository<T, ID> {


}
