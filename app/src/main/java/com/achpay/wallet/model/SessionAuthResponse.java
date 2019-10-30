package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SessionAuthResponse implements Serializable {

    /**
     * resperr :
     * respcd : 0000
     * respmsg :
     * data : {"userid":"1613839","shopname":"窗边的小豆豆的店"}
     */

    @SerializedName("resperr")
    private String resperr;
    @SerializedName("respcd")
    private String respcd;
    @SerializedName("respmsg")
    private String respmsg;
    @SerializedName("data")
    private UserInfo userInfo;

    public String getResperr() {
        return resperr;
    }

    public void setResperr(String resperr) {
        this.resperr = resperr;
    }

    public String getRespcd() {
        return respcd;
    }

    public void setRespcd(String respcd) {
        this.respcd = respcd;
    }

    public String getRespmsg() {
        return respmsg;
    }

    public void setRespmsg(String respmsg) {
        this.respmsg = respmsg;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfo {
        /**
         * userid : 1613839
         * shopname : 窗边的小豆豆的店
         */

        @SerializedName("userid")
        private String userid;
        @SerializedName("shopname")
        private String shopname;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }
    }
}
