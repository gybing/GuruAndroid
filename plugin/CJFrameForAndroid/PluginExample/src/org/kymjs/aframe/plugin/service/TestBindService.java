package org.kymjs.aframe.plugin.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * ��Ϊ���Service����̳�CJService
 */
public class TestBindService extends CJService {

    public class ServiceHolder extends Binder {
        public TestBindService create() {
            return TestBindService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return new ServiceHolder();
    }

    public void show() {
        Log.i("debug", "bind Service success!");
        Toast.makeText(that, "bind���������ɹ�", Toast.LENGTH_SHORT).show();
    }
}
