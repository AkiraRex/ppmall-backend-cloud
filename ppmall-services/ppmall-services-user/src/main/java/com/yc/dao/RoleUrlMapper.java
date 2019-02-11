package com.yc.dao;

import java.util.List;

import com.yc.pojo.RoleUrl;

public interface RoleUrlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RoleUrl record);

    int insertSelective(RoleUrl record);

    RoleUrl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RoleUrl record);

    int updateByPrimaryKey(RoleUrl record);
    
    List<RoleUrl> selectByRoleid(Integer roleid);
}