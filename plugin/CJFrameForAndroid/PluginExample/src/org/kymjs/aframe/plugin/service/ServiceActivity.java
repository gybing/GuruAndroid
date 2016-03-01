package org.kymjs.aframe.plugin.service;

import org.kymjs.aframe.plugin.MainActivity;
import org.kymjs.aframe.plugin.activity.CJActivity;
import org.kymjs.aframe.plugin.example.R;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ��Ϊ���Activity����̳�CJActivity
 */
public class ServiceActivity extends CJActivity {

    private Button mBtnService;
    private Button mBtnBind;

    private TestBindService serviceTwo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that.setContentView(R.layout.aty_service);
        mBtnService = (Button) that
                .findViewById(R.id.aty_service_btn);
        mBtnService.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // �����Intent�����Ǵ�CJServiceUtils�л�ȡ��
                that.startService(CJServiceUtils.getPluginIntent(
                        that, MainActivity.pluginPath,
                        TestService.class));
            }
        });
        mBtnBind = (Button) that.findViewById(R.id.aty_service_bind);
        mBtnBind.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // �����Intent�����Ǵ�CJServiceUtils�л�ȡ��
                Intent serviceIntent = CJServiceUtils
                        .getPluginIntent(that,
                                MainActivity.pluginPath,
                                TestBindService.class);
                that.bindService(serviceIntent, serviceConnection,
                        BIND_AUTO_CREATE);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        that.unbindService(serviceConnection);
    }

    // �󶨷��� ����
    private ServiceConnection serviceConnection = new ServiceConnection() {
        // �󶨷���
        public void onServiceConnected(ComponentName name,
                IBinder service) {
            // ���ڻ�ȡ���񷵻ص�������Ϣ -- �˴���ȡServiceTwo�Ķ���
            serviceTwo = ((TestBindService.ServiceHolder) service)
                    .create();
            serviceTwo.show();
        }

        // ������
        public void onServiceDisconnected(ComponentName name) {
            Log.i("debug", "--" + name);
        }
    };
}
