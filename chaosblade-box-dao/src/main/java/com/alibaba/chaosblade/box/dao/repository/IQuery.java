package com.alibaba.chaosblade.box.dao.repository;

import java.util.List;

/**
 * @author haibin
 *
 *
 */
public interface IQuery<T, Query> {

    public List<T> find(Query query);

    public int count(Query query);

    public int delete(Query query);
}
