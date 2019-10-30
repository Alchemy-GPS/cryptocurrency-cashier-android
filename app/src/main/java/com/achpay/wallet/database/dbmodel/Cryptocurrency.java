package com.achpay.wallet.database.dbmodel;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;


@Entity
public class Cryptocurrency{

    /**
     * cryptoCurrency : BTC
     * cryptoCurrencyDesc : 比特币
     * listlogoUrl : icon地址
     * cryptoName : coincapMarket上的地址后缀
     * cryptoProtocol : 二维码地址上的amount/value
     * cryptoCoin : 二维码地址上的币种标识
     * fullCoinName : 币种全名
     * coinunique : 虚拟币唯一ID
     * coinType : 是否支持闪电/雷电网络
     */

    @Id
    @SerializedName("coinunique")
    private String coinunique;
    @SerializedName("cryptoCurrency")
    private String cryptoCurrency;
    @SerializedName("cryptoCurrencyDesc")
    private String cryptoCurrencyDesc;
    @SerializedName("listlogoUrl")
    private String listlogoUrl;
    @SerializedName("cryptoName")
    private String cryptoName;
    @SerializedName("cryptoProtocol")
    private String cryptoProtocol;
    @SerializedName("cryptoCoin")
    private String cryptoCoin;
    @SerializedName("fullCoinName")
    private String fullCoinName;
    @SerializedName("coinType")
    private String coinType;

    @Generated(hash = 758541161)
    public Cryptocurrency(String coinunique, String cryptoCurrency,
            String cryptoCurrencyDesc, String listlogoUrl, String cryptoName,
            String cryptoProtocol, String cryptoCoin, String fullCoinName,
            String coinType) {
        this.coinunique = coinunique;
        this.cryptoCurrency = cryptoCurrency;
        this.cryptoCurrencyDesc = cryptoCurrencyDesc;
        this.listlogoUrl = listlogoUrl;
        this.cryptoName = cryptoName;
        this.cryptoProtocol = cryptoProtocol;
        this.cryptoCoin = cryptoCoin;
        this.fullCoinName = fullCoinName;
        this.coinType = coinType;
    }
    @Generated(hash = 64960040)
    public Cryptocurrency() {
    }
    public String getCryptoCurrency() {
        return this.cryptoCurrency;
    }
    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }
    public String getCryptoCurrencyDesc() {
        return this.cryptoCurrencyDesc;
    }
    public void setCryptoCurrencyDesc(String cryptoCurrencyDesc) {
        this.cryptoCurrencyDesc = cryptoCurrencyDesc;
    }
    public String getListlogoUrl() {
        return this.listlogoUrl;
    }
    public void setListlogoUrl(String listlogoUrl) {
        this.listlogoUrl = listlogoUrl;
    }
    public String getCryptoName() {
        return this.cryptoName;
    }
    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }
    public String getCryptoCoin() {
        return this.cryptoCoin;
    }
    public void setCryptoCoin(String cryptoCoin) {
        this.cryptoCoin = cryptoCoin;
    }
    public String getCryptoProtocol() {
        return this.cryptoProtocol;
    }
    public void setCryptoProtocol(String cryptoProtocol) {
        this.cryptoProtocol = cryptoProtocol;
    }
    public String getFullCoinName() {
        return this.fullCoinName;
    }
    public void setFullCoinName(String fullCoinName) {
        this.fullCoinName = fullCoinName;
    }
    public String getCoinunique() {
        return this.coinunique;
    }
    public void setCoinunique(String coinunique) {
        this.coinunique = coinunique;
    }
    public String getCoinType() {
        return this.coinType;
    }
    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }
}
