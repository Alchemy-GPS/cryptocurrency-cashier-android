package com.achpay.wallet.model.params;

public interface ResponseParam {
    String SUCCESS = "SUCCESS";
    String MSUCCESS = "MSUCCESS";
    String LSUCCESS = "LSUCCESS";
    String CONFIRMING = "CONFIRMING";
    String DEALING = "DEALING";
    String FAIL = "FAIL";
    String TRUE = "true";
    String FALSE = "false";

    String SESSION_TIMEOUT = "4500";
    String SUCCESS_CODE = "0000";    //成功
    String AMOUNT_EXCEED_LIMIT = "1005";    //金额超限

    /*
    1251    您的账号未注册
    1001	用户名或密码错误
    1002	商户支持数字货币列表获取失败
    1003	输入参数错误[ {0} ]
    1004	商户号不存在
    1005	订单金额超限
    1007	订单号相同,订单金额不同
    2001	收款地址获取失败
    3001	换算异常
    4001	订单创建异常
    4002	查询订单接口异常,不代表订单失败
    4500	session过期。需重新登录
    9999	接口调用失败
    */
}
