package com.example.camera;

import android.os.Environment;

public class AppUtile {
	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

}
