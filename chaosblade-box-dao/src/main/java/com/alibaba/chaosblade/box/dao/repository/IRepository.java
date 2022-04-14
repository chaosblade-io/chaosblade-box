package com.alibaba.chaosblade.box.dao.repository;

import java.util.Optional;

/**
 * @author haibin
 *
 *
 */
public interface IRepository<KEY, T> {

    Optional<T> findById(KEY key);

    boolean update(T t);

    boolean add(T t);

}
