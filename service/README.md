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

[service_life]: http://www.cnblogs.com/mengdd/archive/2013/03/24/2979944.html

