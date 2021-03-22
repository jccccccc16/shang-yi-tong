package com.cjc.syt.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * Mybatis plus Handler配置类
 * 参数填充
 * Created by IntelliJ IDEA.
 * User: cjc
 * Date: 2021/3/22
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 **/
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime",new Date(),metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime",new Date(),metaObject);
    }
}
