package com.lots.travel.network.model.result;

/**
 * Created by nalanzi on 2018/1/22.
 */

public class GetShippingResult {

    /**
     * friend : false
     * gzMeAll : 0
     * meGzAll : 0
     * user : {"birthday":"1992-09-24","city":"渭南市","company":"黑色玫瑰","constellation":"天枰座","country":"CN","department":"箭术","district":"华阴市","faceImg":"http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg","faceImg132":"http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg","faceImg64":"http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg","faceImg640":"http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg","height":175,"mail":"1551618805@qq.com","maritalStatus":"0","openid":"oQGFKxLzGIVVdN-822ScwjrZ8oa0","province":"陕西省","qq":"1551618805","school":"德玛西亚","sex":"1","tags":"演出,自由行,潜水,海岛,海淘购物,酒吧,温泉,游艇,丛林,雪山,夜跑,艺术,阅读,美剧,美食,动漫,电影,绘画,户外,音乐","title":"射手","usedBalance":0,"usedPoints":0,"userId":11941,"userName":"剑锋妖娆","weight":65}
     * userbyaddress : {"address":"幽灵路444号","city":"北京市","consignee":"李义","descript":"测试测试","district":"东城区","id":16,"phone":"13668693527","province":"北京市","status":1,"userId":11941}
     */

    private boolean friend;
    private int gzMeAll;
    private int meGzAll;
    private UserBean user;
    private ShippingBean userbyaddress;

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public int getGzMeAll() {
        return gzMeAll;
    }

    public void setGzMeAll(int gzMeAll) {
        this.gzMeAll = gzMeAll;
    }

    public int getMeGzAll() {
        return meGzAll;
    }

    public void setMeGzAll(int meGzAll) {
        this.meGzAll = meGzAll;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public ShippingBean getUserbyaddress() {
        return userbyaddress;
    }

    public void setUserbyaddress(ShippingBean userbyaddress) {
        this.userbyaddress = userbyaddress;
    }

    public static class UserBean {
        /**
         * birthday : 1992-09-24
         * city : 渭南市
         * company : 黑色玫瑰
         * constellation : 天枰座
         * country : CN
         * department : 箭术
         * district : 华阴市
         * faceImg : http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg
         * faceImg132 : http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg
         * faceImg64 : http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg
         * faceImg640 : http://naan.oss-cn-qingdao.aliyuncs.com/develop/11941/icon/e8033b09-8d26-4d14-addf-cadeeee50949_2018-01-19.jpg
         * height : 175
         * mail : 1551618805@qq.com
         * maritalStatus : 0
         * openid : oQGFKxLzGIVVdN-822ScwjrZ8oa0
         * province : 陕西省
         * qq : 1551618805
         * school : 德玛西亚
         * sex : 1
         * tags : 演出,自由行,潜水,海岛,海淘购物,酒吧,温泉,游艇,丛林,雪山,夜跑,艺术,阅读,美剧,美食,动漫,电影,绘画,户外,音乐
         * title : 射手
         * usedBalance : 0
         * usedPoints : 0
         * userId : 11941
         * userName : 剑锋妖娆
         * weight : 65
         */

        private String birthday;
        private String city;
        private String company;
        private String constellation;
        private String country;
        private String department;
        private String district;
        private String faceImg;
        private String faceImg132;
        private String faceImg64;
        private String faceImg640;
        private int height;
        private String mail;
        private String maritalStatus;
        private String openid;
        private String province;
        private String qq;
        private String school;
        private String sex;
        private String tags;
        private String title;
        private int usedBalance;
        private int usedPoints;
        private int userId;
        private String userName;
        private int weight;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getConstellation() {
            return constellation;
        }

        public void setConstellation(String constellation) {
            this.constellation = constellation;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getFaceImg() {
            return faceImg;
        }

        public void setFaceImg(String faceImg) {
            this.faceImg = faceImg;
        }

        public String getFaceImg132() {
            return faceImg132;
        }

        public void setFaceImg132(String faceImg132) {
            this.faceImg132 = faceImg132;
        }

        public String getFaceImg64() {
            return faceImg64;
        }

        public void setFaceImg64(String faceImg64) {
            this.faceImg64 = faceImg64;
        }

        public String getFaceImg640() {
            return faceImg640;
        }

        public void setFaceImg640(String faceImg640) {
            this.faceImg640 = faceImg640;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getMaritalStatus() {
            return maritalStatus;
        }

        public void setMaritalStatus(String maritalStatus) {
            this.maritalStatus = maritalStatus;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getUsedBalance() {
            return usedBalance;
        }

        public void setUsedBalance(int usedBalance) {
            this.usedBalance = usedBalance;
        }

        public int getUsedPoints() {
            return usedPoints;
        }

        public void setUsedPoints(int usedPoints) {
            this.usedPoints = usedPoints;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }
    }

    public static class ShippingBean {
        /**
         * address : 幽灵路444号
         * city : 北京市
         * consignee : 李义
         * descript : 测试测试
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
}
