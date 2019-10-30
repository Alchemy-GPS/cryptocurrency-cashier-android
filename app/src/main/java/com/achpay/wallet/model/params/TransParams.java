package com.achpay.wallet.model.params;

public interface TransParams {
    String COIN_ID = "COIN_ID";
    String COIN_NAME = "COIN_NAME";
    String COIN_AMOUNT = "COIN_AMOUNT";
    String COIN = "COIN";
    String CURRENCY_ID = "CURRENCY_ID";
    String CURRENCY_NAME = "CURRENCY_NAME";
    String CURRENCY_AMOUNT = "CURRENCY_AMOUNT";
    String CURRENCY = "CURRENCY";
    String AMOUNT = "AMOUNT";
    String TITLE = "TITLE";
    String TYPE = "TYPE";
    String ORDER_ID = "TRANS_ORDER_ID";

    String ADDRESS_NORMAL = "normal";
    String ADDRESS_SEGWIT = "segwit";

    String CURRENCY_TO_COIN = "tocryptocurrency";
    String COIN_TO_CURRENCY = "tocurrency";

    String QUERY_TYPE_COINTYPE = "cryptocurrency";
    String QUERY_TYPE_TIME = "timeInterval";
    String QUERY_TYPE_SINGLE = "outSerialNum";

    String CHECK_WEBSOCKET = "CHECK_WEBSOCKET";
    String CLOSE_WEBSOCKET = "CLOSE_WEBSOCKET";

    String GLOBAL_DIALOG_DATA = "GLOBAL_DIALOG_DATA";
    String GLOBAL_DIALOG_LSUCCESS = "GLOBAL_DIALOG_LSUCCESS";

    String WEB_TXID = "WEB_TXID";
    String WEB_ADDRESS = "WEB_ADDRESS";

    String UPDATE_DIALOG_SHOWING = "UPDATE_DIALOG_SHOWING";

    int ACTIVITY_REQUEST_NORMAL = 1000;

    int ACTIVITY_RESULT_NORMAL = 2000;
    int ACTIVITY_RESULT_DESTROY = 2001;

    String LAST_THREE_DAY = "LAST_THREE_DAY";
    String LAST_WEEK = "LAST_WEEK";
    String LAST_MONTH = "LAST_MONTH";

    String[] COINTYPE_TAB_TITLES = {"BLOCKCHAIN", "LIGHTNING"};
    String[] TRANSACTION_TAB_TITLES = new String[2];

    String BLOCKCHAIN = "blockchain";
    String LIGHTNING = "lightning";
}
