package com.example.flowerdemo.list;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 : 用户管理数据
 */

public class UserData {
    public String name;
    public String password;
    public byte[] bytePic;

    public String getName() {
        return name == null ? "" : name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password == null ? "" : password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getBytePic() {
        return bytePic ;
    }
    public void setBytePic(byte[] bytePic) {
        this.bytePic = bytePic;
    }
}
