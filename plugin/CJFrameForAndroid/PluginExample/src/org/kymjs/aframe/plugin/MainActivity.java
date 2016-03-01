package org.kymjs.aframe.plugin;

import org.kymjs.aframe.plugin.activity.CJActivity;
import org.kymjs.aframe.plugin.activity.CJActivityUtils;
import org.kymjs.aframe.plugin.example.R;
import org.kymjs.aframe.plugin.service.ServiceActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends CJActivity implements
        OnClickListener {
    // ����ΪAPP��������ʱʹ��CJConfig.DEF_STR
    public static String pluginPath = CJConfig.DEF_STR;
    private Button mBtnFrag;
    private Button mBtnService;
    private Button mBtnLaunchMode;
    private TextView textview1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that.setContentView(R.layout.aty_main);
        mBtnFrag = (Button) that.findViewById(R.id.button1);
        mBtnFrag.setOnClickListener(this);
        mBtnService = (Button) that.findViewById(R.id.button2);
        mBtnService.setOnClickListener(this);
        mBtnLaunchMode = (Button) that.findViewById(R.id.button5);
        mBtnLaunchMode.setOnClickListener(this);
        textview1 = (TextView) that.findViewById(R.id.textview1);
        Bundle data = that.getIntent().getExtras();
        if (data != null) {
            textview1.setText("�������ݴ���:"
                    + data.getCharSequence("courier"));
        } else {
            textview1.setText("�������ݴ���:������");
        }

        // ����Ϊ�������ʱʹ��apk��ַ������ΪAPP��������ʱֻ��Ҫע�͵����λ�
        // pluginPath = Environment.getExternalStorageDirectory()
        // + "/PluginExample.apk";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button1:
            CJActivityUtils.launchPlugin(that, pluginPath,
                    FragmentAty.class);
            break;
        case R.id.button2:
            CJActivityUtils.launchPlugin(that, pluginPath,
                    ServiceActivity.class);
            break;
        case R.id.button5:
            CJActivityUtils.launchPlugin(that, pluginPath,
                    TestLaunchMode.class);
            break;
        }
    }
}
