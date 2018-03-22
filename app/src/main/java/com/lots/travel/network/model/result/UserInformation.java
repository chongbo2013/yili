package com.lots.travel.network.model.result;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 2017/11/16.
 */

public class UserInformation {
        //是否是朋友
        private boolean friend;
        //粉丝数量
        private int gzMeAll;
        //我关注的人
        private int meGzAll;
        private User user;

        public static class User implements Parcelable{
                //账号信息
                private long userId;
                private String realName;
                //实名认证状态  1是  0 否
                private String authStatus;
                //兴趣爱好
                private String tags;
                //手机号
                private String mobilePhone;
                //头像
                private String faceImg,faceImg64,faceImg132,faceImg640;
                //昵称、性别、生日、星座
                private String userName,sex,birthday,constellation;
                //感情状况、身高、体重
                private String maritalStatus;
                private int height,weight;
                //公司、职业、学校、专业
                private String company,title,school,department;
                //地址 - 详细地址：province+city+district
                private String country,province,city,district;
                //邮箱、QQ
                private String mail,qq;
                //额外
                private String openid;
                private String usedBalance,usedPoints;

                public long getUserId() {
                        return userId;
                }

                public void setUserId(long userId) {
                        this.userId = userId;
                }

                public String getUserName() {
                        return userName;
                }

                public void setUserName(String userName) {
                        this.userName = userName;
                }

                public String getRealName() {
                        return realName;
                }

                public void setRealName(String realName) {
                        this.realName = realName;
                }

                public String getAuthStatus() {
                        return authStatus;
                }

                public void setAuthStatus(String authStatus) {
                        this.authStatus = authStatus;
                }

                public String getTags() {
                        return tags;
                }

                public void setTags(String tags) {
                        this.tags = tags;
                }

                public String getMobilePhone() {
                        return mobilePhone;
                }

                public void setMobilePhone(String mobilePhone) {
                        this.mobilePhone = mobilePhone;
                }

                public String getFaceImg() {
                        return faceImg;
                }

                public void setFaceImg(String faceImg) {
                        this.faceImg = faceImg;
                }

                public String getFaceImg64() {
                        return faceImg64;
                }

                public void setFaceImg64(String faceImg64) {
                        this.faceImg64 = faceImg64;
                }

                public String getFaceImg132() {
                        return faceImg132;
                }

                public void setFaceImg132(String faceImg132) {
                        this.faceImg132 = faceImg132;
                }

                public String getFaceImg640() {
                        return faceImg640;
                }

                public void setFaceImg640(String faceImg640) {
                        this.faceImg640 = faceImg640;
                }

                public String getSex() {
                        return sex;
                }

                public void setSex(String sex) {
                        this.sex = sex;
                }

                public String getBirthday() {
                        return birthday;
                }

                public void setBirthday(String birthday) {
                        this.birthday = birthday;
                }

                public String getConstellation() {
                        return constellation;
                }

                public void setConstellation(String constellation) {
                        this.constellation = constellation;
                }

                public String getMaritalStatus() {
                        return maritalStatus;
                }

                public void setMaritalStatus(String maritalStatus) {
                        this.maritalStatus = maritalStatus;
                }

                public int getHeight() {
                        return height;
                }

                public void setHeight(int height) {
                        this.height = height;
                }

                public int getWeight() {
                        return weight;
                }

                public void setWeight(int weight) {
                        this.weight = weight;
                }

                public String getCompany() {
                        return company;
                }

                public void setCompany(String company) {
                        this.company = company;
                }

                public String getTitle() {
                        return title;
                }

                public void setTitle(String title) {
                        this.title = title;
                }

                public String getSchool() {
                        return school;
                }

                public void setSchool(String school) {
                        this.school = school;
                }

                public String getDepartment() {
                        return department;
                }

                public void setDepartment(String department) {
                        this.department = department;
                }

                public String getCountry() {
                        return country;
                }

                public void setCountry(String country) {
                        this.country = country;
                }

                public String getProvince() {
                        return province;
                }

                public void setProvince(String province) {
                        this.province = province;
                }

                public String getCity() {
                        return city;
                }

                public void setCity(String city) {
                        this.city = city;
                }

                public String getDistrict() {
                        return district;
                }

                public void setDistrict(String district) {
                        this.district = district;
                }

                public String getMail() {
                        return mail;
                }

                public void setMail(String mail) {
                        this.mail = mail;
                }

                public String getQq() {
                        return qq;
                }

                public void setQq(String qq) {
                        this.qq = qq;
                }

                public String getOpenid() {
                        return openid;
                }

                public void setOpenid(String openid) {
                        this.openid = openid;
                }

                public String getUsedBalance() {
                        return usedBalance;
                }

                public void setUsedBalance(String usedBalance) {
                        this.usedBalance = usedBalance;
                }

                public String getUsedPoints() {
                        return usedPoints;
                }

                public void setUsedPoints(String usedPoints) {
                        this.usedPoints = usedPoints;
                }

                protected User(Parcel in) {
                        userId = in.readLong();
                        realName = in.readString();
                        authStatus = in.readString();
                        tags = in.readString();
                        mobilePhone = in.readString();
                        faceImg = in.readString();
                        faceImg64 = in.readString();
                        faceImg132 = in.readString();
                        faceImg640 = in.readString();
                        userName = in.readString();
                        sex = in.readString();
                        birthday = in.readString();
                        constellation = in.readString();
                        maritalStatus = in.readString();
                        height = in.readInt();
                        weight = in.readInt();
                        company = in.readString();
                        title = in.readString();
                        school = in.readString();
                        department = in.readString();
                        country = in.readString();
                        province = in.readString();
                        city = in.readString();
                        district = in.readString();
                        mail = in.readString();
                        qq = in.readString();
                        openid = in.readString();
                        usedBalance = in.readString();
                        usedPoints = in.readString();
                }

                public static final Creator<User> CREATOR = new Creator<User>() {
                        @Override
                        public User createFromParcel(Parcel in) {
                                return new User(in);
                        }

                        @Override
                        public User[] newArray(int size) {
                                return new User[size];
                        }
                };

                @Override
                public int describeContents() {
                        return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                        dest.writeLong(userId);
                        dest.writeString(realName);
                        dest.writeString(authStatus);
                        dest.writeString(tags);
                        dest.writeString(mobilePhone);
                        dest.writeString(faceImg);
                        dest.writeString(faceImg64);
                        dest.writeString(faceImg132);
                        dest.writeString(faceImg640);
                        dest.writeString(userName);
                        dest.writeString(sex);
                        dest.writeString(birthday);
                        dest.writeString(constellation);
                        dest.writeString(maritalStatus);
                        dest.writeInt(height);
                        dest.writeInt(weight);
                        dest.writeString(company);
                        dest.writeString(title);
                        dest.writeString(school);
                        dest.writeString(department);
                        dest.writeString(country);
                        dest.writeString(province);
                        dest.writeString(city);
                        dest.writeString(district);
                        dest.writeString(mail);
                        dest.writeString(qq);
                        dest.writeString(openid);
                        dest.writeString(usedBalance);
                        dest.writeString(usedPoints);
                }
        }

        public boolean isFriend() {
                return friend;
        }

        public void setFriend(boolean friend) {
                this.friend = friend;
        }

        public User getUser() {
                return user;
        }

        public void setUser(User user) {
                this.user = user;
        }

        public int getMeGzAll() {
                return meGzAll;
        }

        public void setMeGzAll(int meGzAll) {
                this.meGzAll = meGzAll;
        }

        public int getGzMeAll() {
                return gzMeAll;
        }

        public void setGzMeAll(int gzMeAll) {
                this.gzMeAll = gzMeAll;
        }

}
