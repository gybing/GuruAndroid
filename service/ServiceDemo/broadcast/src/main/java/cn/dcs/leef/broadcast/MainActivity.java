package cn.dcs.leef.broadcast;

import android.os.Bundle;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    Button button1;//声明按钮的引用
    Button button2;//声明按钮的引用
    TextView myTextView;
    MyBroadcasReceiver myBroadcasReceiver;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTextView = (TextView) this.findViewById(R.id.myTextView);
        button1 = (Button) this.findViewById(R.id.myButton1);
        button2 = (Button) this.findViewById(R.id.myButton2);

        //Click"启动Service"按钮
        button1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
                Toast.makeText(MainActivity.this,
                        "Service启动成功", Toast.LENGTH_SHORT).show();
            }
        });
        //Click"停止Service"按钮
        button2.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyService.class);
                stopService(i);
                Toast.makeText(MainActivity.this,
                        "Service停止成功", Toast.LENGTH_SHORT).show();
            }
        });

        //动态注册一个BroadcastReceiver
        IntentFilter intentFilter = new IntentFilter("cn.com.sgmsc.ServBroad.myThread");
        //创建过滤器
        myBroadcasReceiver = new MyBroadcasReceiver();
        registerReceiver(myBroadcasReceiver, intentFilter);    //注册BroadcastReceiver对象
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcasReceiver);
    }

    //实现MyBroadcasReceiver
    public class MyBroadcasReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle myBundle = intent.getExtras();
            int myInt = myBundle.getInt("myThread");
            if(myInt < 60){                                    //后台Service运行两分钟
                myTextView.setText("后台Service运行了"+myInt+"秒");
            }else{
                Intent i = new Intent(MainActivity.this, MyService.class);
                stopService(i);
                myTextView.setText("后台Service在"+myInt+"秒后停止运行");
            }
        }
    }


}
