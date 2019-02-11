package com.yc.pojo;

import java.util.Date;

public class RoleUser {
    private Integer id;

    private Integer roleId;

    private Integer userId;

    private Date createTime;

    private Date updateTime;

    private String remark;

    public RoleUser(Integer id, Integer roleId, Integer userId, Date createTime, Date updateTime, String remark) {
        this.id = id;
        this.roleId = roleId;
        this.userId = userId;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.remark = remark;
    }

    public RoleUser() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}