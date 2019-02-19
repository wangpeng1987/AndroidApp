package com.wop.serverdemo.me.model;

/**
 * @Description: login 返回 的 数据 data
 * @author: wop
 */
public class LoginData {

    /**
     * idUserTable : 1
     * phone : 1308898989
     * name : WANG
     * pwd : peng
     */
    private int idUserTable;
    private String phone;
    private String name;
    private String pwd;

    public int getIdUserTable() {
        return idUserTable;
    }

    public void setIdUserTable(int idUserTable) {
        this.idUserTable = idUserTable;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
