package com.achpay.wallet.utils;

public class NativeMethods {
    static {
        System.loadLibrary("encrypt");
    }

//    public static native String getPWD();
//
//    public static native String getPWDSaltStart();
//
//    public static native String getPWDSaltEnd();
//
//    public static native String getClientId();

    public static  String getPWD(){
        return "NATIVE_PASSWORD";
    }

    public static  String getPWDSaltStart(){
        return "miaomiaozaixian";
    }

    public static  String getPWDSaltEnd(){
        return "REy0727$UM+64S248fq63q88";
    }

    public static  String getClientId(){
        return "2DAB13A0AF4D4031820149BCD58188D0";
    }
}
