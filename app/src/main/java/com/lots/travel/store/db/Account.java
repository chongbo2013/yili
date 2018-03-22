package com.lots.travel.store.db;

import android.os.Parcel;
import android.os.Parcelable;

import com.lots.travel.network.model.result.EditUserInfo;
import com.lots.travel.network.model.result.UserInformation;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by nalanzi on 2017/10/14.
 */
@Entity
public class Account implements Parcelable{
    @Id
    //账号信息
    private Long userId;
    private String secret;
    private String realname;
    //实名认证状态  1是  0 否
    private String authStatus;
    //兴趣爱好
    private String interests;
    //手机号
    private String mobilePhone;
    //头像
    private String portraitUrl;
    //昵称、性别、生日、星座
    //sex：0 - 保密、1 - 男、2 - 女
    private String username,sex,birthday;
    //感情状况、身高、体重
    //emotion：0 - 单身、1 - 已婚、2 - 离异
    private String emotion;
    private int height,weight;
    //公司、职业、学校、专业
    private String company,profession,school,department;
    //地址 - 详细地址：province+city+district
    private String country,province,city,district;
    //邮箱、QQ
    private String mail,qq;
    //兴趣爱好等标签
    private String tags;
    //关注、粉丝
    private int follow,fans;
    //微信用户
    private boolean wxUser;

    public Account(Account a){
        update(a);
    }

    protected Account(Parcel in) {
        userId = in.readLong();
        secret = in.readString();
        realname = in.readString();
        authStatus = in.readString();
        interests = in.readString();
        mobilePhone = in.readString();
        portraitUrl = in.readString();
        username = in.readString();
        sex = in.readString();
        birthday = in.readString();
        emotion = in.readString();
        height = in.readInt();
        weight = in.readInt();
        company = in.readString();
        profession = in.readString();
        school = in.readString();
        department = in.readString();
        country = in.readString();
        province = in.readString();
        city = in.readString();
        district = in.readString();
        mail = in.readString();
        qq = in.readString();
        tags = in.readString();
        follow = in.readInt();
        fans = in.readInt();
        wxUser = in.readByte() != 0;
    }

    @Generated(hash = 1097288656)
    public Account(Long userId, String secret, String realname, String authStatus,
            String interests, String mobilePhone, String portraitUrl,
            String username, String sex, String birthday, String emotion,
            int height, int weight, String company, String profession,
            String school, String department, String country, String province,
            String city, String district, String mail, String qq, String tags,
            int follow, int fans, boolean wxUser) {
        this.userId = userId;
        this.secret = secret;
        this.realname = realname;
        this.authStatus = authStatus;
        this.interests = interests;
        this.mobilePhone = mobilePhone;
        this.portraitUrl = portraitUrl;
        this.username = username;
        this.sex = sex;
        this.birthday = birthday;
        this.emotion = emotion;
        this.height = height;
        this.weight = weight;
        this.company = company;
        this.profession = profession;
        this.school = school;
        this.department = department;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.mail = mail;
        this.qq = qq;
        this.tags = tags;
        this.follow = follow;
        this.fans = fans;
        this.wxUser = wxUser;
    }

    @Generated(hash = 882125521)
    public Account() {
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public void update(Account a){
        userId = a.userId;
        secret = a.secret;
        realname = a.realname;
        authStatus = a.authStatus;
        interests = a.interests;
        mobilePhone = a.mobilePhone;
        portraitUrl = a.portraitUrl;
        username = a.username;
        sex = a.sex;
        birthday = a.birthday;
        emotion = a.emotion;
        height = a.height;
        weight = a.weight;
        company = a.company;
        profession = a.profession;
        school = a.school;
        department = a.department;
        country = a.country;
        province = a.province;
        city = a.city;
        district = a.district;
        mail = a.mail;
        qq = a.qq;
        follow = a.follow;
        fans = a.fans;
        wxUser = a.wxUser;
    }

    public void update(EditUserInfo s){
//        authStatus = ;
//        mobilePhone = ;
//        nickname = ;
//        interests = ;
//        follow = ;
//        fans = ;
//        wxUser = ;
        username = s.getUserName();
        portraitUrl = s.getFaceImg();
        sex = s.getSex();
        birthday = s.getBirthday();
        emotion = s.getMaritalStatus();
        height = s.getHeight();
        weight = s.getWeight();
        company = s.getCompany();
        profession = s.getTitle();
        school = s.getSchool();
        department = s.getDepartment();
        country = s.getCountry();
        province = s.getProvince();
        city = s.getCity();
        district = s.getDistrict();
        mail = s.getMail();
        qq = s.getQq();
    }

    public void update(UserInformation info){
        follow = info.getMeGzAll();
        fans = info.getGzMeAll();

        UserInformation.User s = info.getUser();
        if(s==null)
            return;

        realname = s.getRealName();
        authStatus = s.getAuthStatus();
        mobilePhone = s.getMobilePhone();
        username = s.getUserName();
        interests = s.getTags();
        emotion = s.getMaritalStatus();
        portraitUrl = s.getFaceImg();
        sex = s.getSex();
        birthday = s.getBirthday();
        height = s.getHeight();
        weight = s.getWeight();
        company = s.getCompany();
        profession = s.getTitle();
        school = s.getSchool();
        department = s.getDepartment();
        country = s.getCountry();
        province = s.getProvince();
        city = s.getCity();
        district = s.getDistrict();
        mail = s.getMail();
        qq = s.getQq();
        tags = s.getTags();
    }

    public static int long2int(long value){
        return (int) (value & 0x00000000FFFFFFFFL);
    }

    public static long int2long(int value){
        return value & 0x00000000FFFFFFFFL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(userId);
        dest.writeString(secret);
        dest.writeString(realname);
        dest.writeString(authStatus);
        dest.writeString(interests);
        dest.writeString(mobilePhone);
        dest.writeString(portraitUrl);
        dest.writeString(username);
        dest.writeString(sex);
        dest.writeString(birthday);
        dest.writeString(emotion);
        dest.writeInt(height);
        dest.writeInt(weight);
        dest.writeString(company);
        dest.writeString(profession);
        dest.writeString(school);
        dest.writeString(department);
        dest.writeString(country);
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(district);
        dest.writeString(mail);
        dest.writeString(qq);
        dest.writeString(tags);
        dest.writeInt(follow);
        dest.writeInt(fans);
        dest.writeByte((byte) (wxUser ? 1 : 0));
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
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

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
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

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public boolean isWxUser() {
        return wxUser;
    }

    public void setWxUser(boolean wxUser) {
        this.wxUser = wxUser;
    }

    public boolean getWxUser() {
        return this.wxUser;
    }
}
