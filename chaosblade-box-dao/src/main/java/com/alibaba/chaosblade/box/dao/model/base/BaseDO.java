package com.alibaba.chaosblade.box.dao.model.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDO implements Serializable {

//    private static final long serialVersionUID = -2178161680256745472L;
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_GMT_CREATE = "gmt_create";
    private Long id;

    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE, update = "now()")
    private Date gmtModified;

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Date getGmtCreate() {
//        return gmtCreate;
//    }
//
//    public void setGmtCreate(Date gmtCreate) {
//        this.gmtCreate = gmtCreate;
//    }
//
//    public Date getGmtModified() {
//        return gmtModified;
//    }
//
//    public void setGmtModified(Date gmtModified) {
//        this.gmtModified = gmtModified;
//    }

//    @Override
//    public String toString() {
//        return "BaseDO{" + "id=" + id + ", gmtCreate=" + gmtCreate + ", gmtModified=" + gmtModified + '}';
//    }
}
