package com.edu.dao.domain;

/**
 * @author Tangzhihao
 * @date 2018/3/20
 */

public class Project {

    private Long id;
    private String domain;
    private String cmdbDomain;
    private int level;
    private String bu;
    private String cmdbProductline;
    private String owner;
    private String email;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCmdbDomain() {
        return cmdbDomain;
    }

    public void setCmdbDomain(String cmdbDomain) {
        this.cmdbDomain = cmdbDomain;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBu() {
        return bu;
    }

    public void setBu(String bu) {
        this.bu = bu;
    }

    public String getCmdbProductline() {
        return cmdbProductline;
    }

    public void setCmdbProductline(String cndbProductline) {
        this.cmdbProductline = cndbProductline;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
