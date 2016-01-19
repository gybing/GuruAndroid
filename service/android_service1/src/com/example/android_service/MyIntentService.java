package com.example.android_service;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class MyIntentService extends IntentService {
	private static String TAG = "MusicService";

	public MyIntentService() {
		super("MyIntentServiceName");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// IntentServiceʹ�ö��еķ�ʽ�������Intent������У�Ȼ����һ��worker
		// thread(�߳�)����������е�Intent
		// �����첽��startService����IntentService�ᴦ�����һ��֮���ٴ���ڶ���
		try {
			int time = 20000;
			Log.e(TAG, "begin sleep" + time);
			Thread.sleep(time);
			Log.e(TAG, "after sleep" + time);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}