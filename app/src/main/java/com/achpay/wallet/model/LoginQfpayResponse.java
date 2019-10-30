package com.achpay.wallet.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginQfpayResponse implements Serializable {

    /**
     * resperr :
     * respcd : 0000
     * respmsg :
     * data : {"sessionid":"e6a91405-f46b-4e49-9dcf-0a7c57d879c8","userid":21006719,"shopname":"nickname"}
     */

    @SerializedName("resperr")
    private String resperr;
    @SerializedName("respcd")
    private String respcd;
    @SerializedName("respmsg")
    private String respmsg;
    @SerializedName("data")
    private UserData data;

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

    public UserData getUserData() {
        return data;
    }

    public void setUserData(UserData data) {
        this.data = data;
    }

    public static class UserData {
        /**
         * sessionid : e6a91405-f46b-4e49-9dcf-0a7c57d879c8
         * userid : 21006719
         * shopname : nickname
         */

        @SerializedName("sessionid")
        private String sessionid;
        @SerializedName("shopname")
        private String shopname;
        @SerializedName("userid")
        private int userid;
        @SerializedName("currency")
        private int currency;
        @SerializedName("country")
        private String country;

        public String getSessionid() {
            return sessionid;
        }

        public void setSessionid(String sessionid) {
            this.sessionid = sessionid;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
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
