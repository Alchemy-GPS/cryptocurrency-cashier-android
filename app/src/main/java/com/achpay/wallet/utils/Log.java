package com.achpay.wallet.utils;

import com.achpay.wallet.Constants;

/**
 * 覆盖Android原生Log,可以控制不打印Log
 */

public final class Log {

	private static String Tag = "ACHPAY_WALLET";

	private Log() {
	}

	public static void setEnabled(boolean enabled) {
		Constants.logEnabled = enabled;
	}

	public static boolean isLoggingEnabled() {
		return Constants.logEnabled;
	}

	public static int v(String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.v(Tag, msg);
		}
		return 0;
	}

	public static int v(String tag, String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.v(tag, msg);
		}
		return 0;
	}

	public static int v(String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.v(Tag, msg, tr);
		}
		return 0;
	}

	public static int v(String tag, String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.v(tag, msg, tr);
		}
		return 0;
	}

	public static int d(String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.d(Tag, msg);
		}
		return 0;
	}

	public static int d(String tag, String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.d(tag, msg);
		}
		return 0;
	}

	public static int d(String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.d(Tag, msg, tr);
		}
		return 0;
	}

	public static int d(String tag, String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.d(tag, msg, tr);
		}
		return 0;
	}

	public static int i(String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.i(Tag, msg);
		}
		return 0;
	}

	public static int i(String tag, String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.i(tag, msg);
		}
		return 0;
	}

	public static int i(String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.i(Tag, msg, tr);
		}
		return 0;
	}

	public static int i(String tag, String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.i(tag, msg, tr);
		}
		return 0;
	}

	public static int w(String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.w(Tag, msg);
		}
		return 0;
	}

	public static int w(String tag, String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.w(tag, msg);
		}
		return 0;
	}

	public static int w(String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.w(Tag, msg, tr);
		}
		return 0;
	}

	public static int w(String tag, String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.w(tag, msg, tr);
		}
		return 0;
	}

	public static int e(String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.e(Tag, msg);
		}
		return 0;
	}

	public static int e(String tag, String msg) {
		if (Constants.logEnabled) {
			return android.util.Log.e(tag, msg);
		}
		return 0;
	}

	public static int e(String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.e(Tag, msg, tr);
		}
		return 0;
	}

	public static int e(String tag, String msg, Throwable tr) {
		if (Constants.logEnabled) {
			return android.util.Log.e(tag, msg, tr);
		}
		return 0;
	}

	public static int t(String msg, Object... args) {
		if (Constants.logEnabled) {
			return android.util.Log.v("test", String.format(msg, args));
		}
		return 0;
	}
}