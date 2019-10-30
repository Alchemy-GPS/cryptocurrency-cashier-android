//
// Created by 陈达彤 on 2018/5/12.
//

#include <jni.h>
#include "com_achpay_wallet_utils_NativeMethods.h"

#define PWD "NATIVE_PASSWORD"

#define PWDSALTSTART "miaomiaozaixian"

#define PWDSALTEND "REy0727$UM+64S248fq63q88"

#define CLIENT_ID "2DAB13A0AF4D4031820149BCD58188D0"

extern "C" JNIEXPORT jstring JNICALL Java_com_achpay_wallet_utils_NativeMethods_getPWD (JNIEnv *env, jclass jclass1)
{
    return env->NewStringUTF(PWD);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_achpay_wallet_utils_NativeMethods_getPWDSaltStart (JNIEnv *env, jclass jclass1)
{
    return env->NewStringUTF(PWDSALTSTART);
}

extern "C" JNIEXPORT jstring JNICALL Java_com_achpay_wallet_utils_NativeMethods_getPWDSaltEnd (JNIEnv *env, jclass jclass1)
{
    return env->NewStringUTF(PWDSALTEND);
}


extern "C" JNIEXPORT jstring JNICALL Java_com_achpay_wallet_utils_NativeMethods_getClientId (JNIEnv *env, jclass jclass1)
{
    return env->NewStringUTF(CLIENT_ID);
}

