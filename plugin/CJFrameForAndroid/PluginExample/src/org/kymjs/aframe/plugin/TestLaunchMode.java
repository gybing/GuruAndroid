package org.kymjs.aframe.plugin;

import org.kymjs.aframe.plugin.activity.CJActivity;
import org.kymjs.aframe.plugin.activity.CJActivityUtils;
import org.kymjs.aframe.plugin.example.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TestLaunchMode extends CJActivity {
    public TestLaunchMode() {
        setLunchMode(LaunchMode.SINGLETOP);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        that.setContentView(R.layout.aty_launch_test);
        ((TextView) that.findViewById(R.id.launch_text))
                .setText("��ǰ����ģʽΪsingleTop��������ڴ������޸�");
        Button btn = (Button) that.findViewById(R.id.launcu_btn);
        btn.setText("����ٴ���ת����Activity");
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CJActivityUtils.launchPlugin(that, MainActivity.pluginPath,
                        TestLaunchMode.class);
            }
        });
    }
}
