package com.lots.travel.network.model.result;

/**
 * Created by nalanzi on 2018/1/22.
 */

public class SaveShipping {

    /**
     * address : 幽灵路444号
     * city : 北京市
     * consignee : 李义
     * descript :
     * district : 东城区
     * id : 16
     * phone : 13668693527
     * province : 北京市
     * status : 1
     * userId : 11941
     */

    private String address;
    private String city;
    private String consignee;
    private String descript;
    private String district;
    private int id;
    private String phone;
    private String province;
    private int status;
    private int userId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
