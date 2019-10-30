package com.achpay.wallet.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * sharapreferences 工具类
 *
 * @author ysh
 * 
 */
public class SharedPreferenceUtil implements SharedPreferences {

	private static SharedPreferenceUtil mInstance = null;
	private SharedPreferences mSharedPreferences;
	private Editor mEditor;

	public synchronized static SharedPreferenceUtil getPref(Context context) {
		if (null == mInstance)
			mInstance = new SharedPreferenceUtil(context);
		return mInstance;
	}

	private SharedPreferenceUtil(Context context) {
		//此处将sp文件名改为固定包名
		mSharedPreferences = context.getSharedPreferences("com.achpay.wallet",
				Context.MODE_PRIVATE);
	}

	public void clearDate() {
		mEditor = mSharedPreferences.edit();
		mEditor.clear().apply();
	}

	public void putStringValue(String key, String value) {
		mEditor = mSharedPreferences.edit();
		mEditor.putString(key, value);
		mEditor.apply();
	}

	public String getStringValue(String key) {
		return mSharedPreferences.getString(key, "");
	}

	public void putLongValue(String key, long value) {
		mEditor = mSharedPreferences.edit();
		mEditor.putLong(key, value);
		mEditor.apply();
	}

	public long getLongValue(String key) {
		return mSharedPreferences.getLong(key, 0);
	}

	public void putIntValue(String key, int value) {
		mEditor = mSharedPreferences.edit();
		mEditor.putInt(key, value);
		mEditor.apply();
	}

	public int getIntValue(String key) {
		return mSharedPreferences.getInt(key, 0);
	}

	public void putBooleanValue(String key, boolean flag) {
		mEditor = mSharedPreferences.edit();
		mEditor.putBoolean(key, flag);
		mEditor.apply();
	}

	public boolean getBooleanValue(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	@Override
	public boolean contains(String key) {
		return mSharedPreferences.contains(key);
	}

	@Override
	public Editor edit() {
		return mSharedPreferences.edit();
	}

	@Override
	public Map<String, ?> getAll() {
		return mSharedPreferences.getAll();
	}

	@Override
	public boolean getBoolean(String key, boolean defValue) {
		return mSharedPreferences.getBoolean(key, defValue);
	}

	@Override
	public float getFloat(String key, float defValue) {
		return mSharedPreferences.getFloat(key, defValue);
	}

	@Override
	public int getInt(String key, int defValue) {
		return mSharedPreferences.getInt(key, defValue);
	}

	@Override
	public long getLong(String key, long defValue) {
		return mSharedPreferences.getLong(key, defValue);
	}

	@Override
	public String getString(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}

	public Set<String> getStringSet(String arg0, Set<String> arg1) {
		return null;
	}

	@Override
	public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
	}

	@Override
	public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
	}

}
