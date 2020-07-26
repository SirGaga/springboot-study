package com.zhangjie.springbootstudy.service;

import com.zhangjie.springbootstudy.domain.User;

public interface UserService {
    /**
     * Redis string类型模拟
     * 需求：用户输入一个key
     *  先判断redis中是否存在该key
     *  如果存在，在Redis中进行查询，返回结果
     *  如果不存在，查询mysql，将查询结果赋值给redis，返回结果
     *
     */

    String getString(String key);

}
