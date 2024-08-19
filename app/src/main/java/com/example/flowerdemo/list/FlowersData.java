package com.example.flowerdemo.list;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 : 用户管理数据
 */

public class FlowersData {
    public String name;
    public String kind;
    public String price;
    public String address;
    public byte[] bytePic;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind == null ? "" : kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte[] getBytePic() {
        return bytePic;
    }

    public void setBytePic(byte[] bytePic) {
        this.bytePic = bytePic;
    }
}
