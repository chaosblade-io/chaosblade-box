package com.alibaba.chaosblade.box.dao.infrastructure.model;

import java.io.Serializable;
import java.util.List;

/**
 * Author: sunju
 *
 * Date:   2019/11/14
 */
public interface TreeNode<T> extends Serializable {

//    T getParent();

    List<T> getChildren();
}
