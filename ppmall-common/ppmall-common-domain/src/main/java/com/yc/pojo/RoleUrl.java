package com.yc.pojo;

import java.util.Date;

public class RoleUrl {
    private Integer id;

    private Integer roleId;

    private String permissionUrl;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public RoleUrl(Integer id, Integer roleId, String permissionUrl, String remark, Date createTime, Date updateTime) {
        this.id = id;
        this.roleId = roleId;
        this.permissionUrl = permissionUrl;
        this.remark = remark;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public RoleUrl() {
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

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl == null ? null : permissionUrl.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
}