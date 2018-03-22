package com.lots.travel.network.model.result;

/**
 * Created by nalanzi on 2017/9/16.
 */

public class LoginByMobileCode {
    private boolean setinfo;
    private User user;

    public static class User{
        private String mobileCheck;
        private String mobilePhone;
        private String phone;
        private String scr;
        private int usedBalance;
        private int usedPoints;
        private int userId;

        public String getMobileCheck() {
            return mobileCheck;
        }

        public void setMobileCheck(String mobileCheck) {
            this.mobileCheck = mobileCheck;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getScr() {
            return scr;
        }

        public void setScr(String scr) {
            this.scr = scr;
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
    }

    public boolean isSetinfo() {
        return setinfo;
    }

    public void setSetinfo(boolean setinfo) {
        this.setinfo = setinfo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
