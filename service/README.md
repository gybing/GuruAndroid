#Service�򵥽���
###����
######����1
Local����: �÷����������������ϣ������̱�Kill�󣬷�������ֹ��Local������Ϊ����ͬһ������˲���ҪIPC��Ҳ����ҪAIDL  
Remote���񣺸÷����Ƕ����Ľ��̣���Activity���ڽ��̱�Kill��ʱ�򣬸÷�����Ȼ�����С� android:processs  
######����2
ǰ̨���� ����֪ͨһ����ʾ ONGOING �� Notification      
��̨����: �ϵķ���Ϊ��̨���񣬼�������֪ͨһ����ʾ ONGOING �� Notification   
######����3
startService stopService  
bindService unbindService   
start+bind  


###Service �� Thread ������
Thread: ��һ�� Activity �� finish ֮�������û������ֹͣ Thread ���� Thread ��� run ����û��ִ����ϵĻ���Thread Ҳ��һֱִ��  
Service:  service+thread���Խ��"��ͣ�ظ�һ��ʱ�����ӷ�������ĳ��ͬ��"(��application�Ĵ�����ȫ��threadҲ����ʵ��? )  


###Service����������
#####start service
(����������Ϊcontext.startService() ->onCreate()- >onStart()->Service running-->(�������context.stopService() )->onDestroy() ->Service shut down)
��������serviceͨ������������� startService()��������  
����service�������޵�������ȥ���������stopSelf()�������������������stopService()������ֹͣ����  
��service��ֹͣʱ��ϵͳ����������  
Service��startService ���������������ôonCreate����ֻ�����һ�Σ�onStart���ᱻ���ö��   
���ٴ�startService()��ϵͳֻ�ᴴ��Service��һ��ʵ��,��˶�ֻ�����һ�� stopService()��ֹͣ   
#####bind service
(context.bindService()->onCreate()->onBind()->Service running-->onUnbind() -> onDestroy() ->Service stop)
���󶨵�service�ǵ����������һ���ͻ�������bindService()�������ġ�  
�ͻ�����ͨ��һ��IBinder�ӿں�service����ͨ�š�   
�ͻ�����ͨ�� unbindService()�������ر��������ӡ�  
һ��service����ͬʱ�Ͷ���ͻ��󶨣�������ͻ��������֮��ϵͳ������service��
onBind(ֻһ�Σ����ɶ�ΰ�)      
Context(Activity��),���Service����һ��Context�˳��ˣ�service�ͻ�onDestroy    
����ͻ��˿��԰���ͬһ��������������ʱ��û�м��أ�bindService()���ȼ� ����  (bindService(intent, mServiceConnection, BIND_AUTO_CREATE);//���û�У��Զ�����service)   
#####start+bind
���磬һ����̨����service��������� startService()�������������ˣ��Ժ󣬿����û���Ҫ���Ʋ��������ߵõ�һЩ��ǰ��������Ϣ������ͨ��bindService()��һ��activity��service�󶨡���������£�stopService()�� stopSelf()ʵ���ϲ�����ֹͣ���service���������еĿͻ�������󶨡�
onCreateʼ��ֻ�����һ�Σ���ӦstartService���ö��ٴ�   
Service ����ֹ����ҪunbindService��stopServiceͬʱ���ã�������ֹ Service������startService �� bindService �ĵ���˳��
#####ֹͣ
��һ��Service����ֹ��1������stopService��2������stopSelf��3�������а󶨵����ӣ�û�б���������ʱ��onDestroy�������ᱻ����   
start+stop �� bind+unbind ������һ��  



###��Ҫʹ�÷���

#####Remote AIDL  (�ο�ServiceDemo/bindservice/MusicService)

![alt text](https://raw.githubusercontent.com/fitzlee/GuruAndroid/master/_images/aidl_remote_register_callback.jpg)

```java
interface IParticipateCallback {
    // �û���������뿪�Ļص�
    void onParticipate(String name, boolean joinOrLeave);
}
interface IRemoteService {
    int someOperate(int a, int b);

    void join(IBinder token, String name);
    void leave(IBinder token);
    List<String> getParticipators();

    void registerParticipateCallback(IParticipateCallback cb);
    void unregisterParticipateCallback(IParticipateCallback cb);
}

public class RemoteService extends Service {

    private static final String TAG = RemoteService.class.getSimpleName();

    private List<Client> mClients = new ArrayList<>();

    private RemoteCallbackList<IParticipateCallback> mCallbacks = new RemoteCallbackList<>();

    private final IRemoteService.Stub mBinder = new IRemoteService.Stub() {
        @Override
        public int someOperate(int a, int b) throws RemoteException {
            Log.d(TAG, "called RemoteService someOperate()");
            return a + b;
        }

        @Override
        public void join(IBinder token, String name) throws RemoteException {
            int idx = findClient(token);
            if (idx >= 0) {
                Log.d(TAG, "already joined");
                return;
            }

            Client client = new Client(token, name);

            // ע��ͻ���������֪ͨ
            token.linkToDeath(client, 0);
            mClients.add(client);

            // ֪ͨclient����
            notifyParticipate(client.mName, true);
        }

        @Override
        public void leave(IBinder token) throws RemoteException {
            int idx = findClient(token);
            if (idx < 0) {
                Log.d(TAG, "already left");
                return;
            }

            Client client = mClients.get(idx);
            mClients.remove(client);

            // ȡ��ע��
            client.mToken.unlinkToDeath(client, 0);

            // ֪ͨclient�뿪
            notifyParticipate(client.mName, false);
        }

        @Override
        public List<String> getParticipators() throws RemoteException {
            ArrayList<String> names = new ArrayList<>();
            for (Client client : mClients) {
                names.add(client.mName);
            }
            return names;
        }

        @Override
        public void registerParticipateCallback(IParticipateCallback cb) throws RemoteException {
            mCallbacks.register(cb);
        }

        @Override
        public void unregisterParticipateCallback(IParticipateCallback cb) throws RemoteException {
            mCallbacks.unregister(cb);
        }
    };

    public RemoteService() {
    }

    private int findClient(IBinder token) {
        for (int i = 0; i < mClients.size(); i++) {
            if (mClients.get(i).mToken == token) {
                return i;
            }
        }
        return -1;
    }

    private void notifyParticipate(String name, boolean joinOrLeave) {
        final int len = mCallbacks.beginBroadcast();
        for (int i = 0; i < len; i++) {
            try {
                // ֪ͨ�ص�
                mCallbacks.getBroadcastItem(i).onParticipate(name, joinOrLeave);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service","destroy");
        // ȡ�������еĻص�
        mCallbacks.kill();
    }

    private final class Client implements IBinder.DeathRecipient {
        public final IBinder mToken;
        public final String mName;

        public Client(IBinder token, String name) {
            mToken = token;
            mName = name;
        }

        @Override
        public void binderDied() {
            // �ͻ���������ִ�д˻ص�
            int index = mClients.indexOf(this);
            if (index < 0) {
                return;
            }

            Log.d(TAG, "client died: " + mName);
            mClients.remove(this);

            // ֪ͨclient�뿪
            notifyParticipate(mName, false);
        }
    }
}

```


#####Local BindService
```java
public class MusicService extends Service
	public class MyBinder extends Binder {
		public MusicService getService() {
			return MusicService.this;
		}
	}
	
	public void MyMethod(){
		//your methods
	}
}
```


```java
public class MainActivity extends Activity{

	ServiceConnection conn = new ServiceConnection() {

			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				MusicService.MyBinder binder = (MusicService.MyBinder) service;
				bindService = binder.getService();
			}
	};
	
	void test(){
		//����service�Ľӿ�
		bindService.MyMethod();
		//ע������ʹ��broadcast����ע��ص�interface��ʵ��service����֪ͨActivity
	}
}
```

#####IntentService = thread+service

```java
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
```

#####Broadcast+Service
```java
public MyService extends service{

	@Override
	public void onDestroy() {   //��д��onDestroy����
		myThread.flag = false;  //ֹͣ�߳�����
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		myThread = new MyThread() ;  //��ʼ���߳�
		myThread.start();            //�����߳�
		return super.onStartCommand(intent, flags, startId);
	}

	//�����߳���
	class MyThread extends Thread{
		boolean flag = true;        //ѭ����־λ
		int c = 0;                  //��ֵΪ���͵���Ϣ
		@Override
		public void run() {
			while(flag){
				Intent i = new Intent("cn.com.sgmsc.ServBroad.myThread");//����Intent
				i.putExtra("myThread", c);      //��������
				sendBroadcast(i);               //���͹㲥
				c++;
				try{
					Thread.sleep(1000);        //˯��ָ��������
				}catch(Exception e){           //�����쳣
					e.printStackTrace();       //��ӡ�쳣
				}
			}
		}
	};
}
```

#####System Service
```java
TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
SmsManager sManager = SmsManager.getDefault();
```


###ԭ��


[service_life]: http://www.cnblogs.com/mengdd/archive/2013/03/24/2979944.html

