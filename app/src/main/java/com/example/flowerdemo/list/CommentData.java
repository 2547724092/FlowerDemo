package com.example.flowerdemo.list;

/**
 * 作者 : Run
 * 日期 : 2022/5/17
 * 描述 : 点评数据库
 */


public class CommentData {
    public String user;
    public String name;
    public String content;

    public String getUser() {
        return user == null ? "" : user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
