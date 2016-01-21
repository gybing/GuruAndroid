###����
1. fragment���ظ�new
2. ����ܻ��ˣ�
3. demo 
4. ����������destroy��create����fragment�Ż�
5. �����������ڵڶ���·�Ż� (�����Ż�\�������Ż�\�����Ż�)  
	//�����Ż�ֻ������addToBackStack()�����ĵڶ�����������·��  	
	https://hzj163.gitbooks.io/android-fragment/content/fragmentyi_xie_you_hua.html  
5. fragment������
6. �������ڱ仯	
	
###�������� ���ڱ仯

###��Ļת�� ���ڱ仯




###Fragment����������
```
1.onAttach->onCreate.....->onDestroy->onDettach

  ��������������һ���������������ڣ�onAttach��ʾ��Actitivity���ţ�onDettach��ʾ��Activity���룬һ����˵������add����������������̡�

  ע�⣺��FragmentActivity��ʹ������ķ���attach��dettach���������onAttach��onDettach���ǻᷢ��ʲô�仯�أ���������

2. onSaveInstanceState-->onStop ... onStart->onResume-->....

   �����������ں�Activity��onRestart����һ���Ĺ�����

   ע�⣬Activityÿ�ε���onRestart֮��Fragment�ͻ�ִ�������������ڣ�����Ҫע����ǣ������������ڲ����ɿ�����ʱ����ִ�С�

3.onDestroyView-> .... ->onCreateView ->onViewCreate

  �������ڷ���ô�����Ƿ񶨵ġ�����������������������ʹ��������ķ���dettach��attach��

ע�⣺�����������У����Ը��õĹ���Fragment�ļ��أ�Ҳ���Խ���������⣬��������ѭ�����⡣

4.�ظ�onAttach->onCreate.....->onDestroy->onDettach

������������������ÿ�ζ����õ���replace����

5.�־�̬

�ھ�����.onAttach->onCreate->onCreatView-->...->onResume֮�����û����replace��add,attach,dettach������ʹ���˼򵥵�hide,show�ȷ���

ע�⣺���ֿ����ڻ���ջ������
```



###fragment��ֹ�ظ�����
#####����
�����ʱ���س�����ҳ����Ҫ���¼���һ�Σ�����Ҳ�͸��ڴ�����������˼��ѹ�����û�������Ҳ�����Ѻã�����������������ȿ���ʱ���Ƚϳ��������
#####��״
* Ĭ������µ�ViewPager����������ҳ��ʱ�򣬵�һҳ��Fragment�ͻ�ִ��onDestroyView�����ٴλ������ڶ�ҳ��ʱ�򣬵�һҳ��Fragment��onCreateView�ֻ�����ִ�л���ҳ��
* fragment0,1,2,3; viewpager_init(0),��ʼ��ʱ�����0,1; ���һ�������2,�����ҳ�ʼ��3    ???
* viewpager����غ͵�ǰҳ������������fragment�����������ڵ�����ҳ���view���ٴε����ǻ�����oncreateview��onactivityreate
#####���˼·1 
```java
//����rootView,������غ��view������оͲ����¼������ݡ�
//�����ж��Ƿ��Ѿ�����������ɵı�־����������Ѿ����������ݣ��Ͳ����¼�������
public abstract class BaseFragment extends Fragment {  
  
    //����view  
    private View rootView;  
    protected Context context;  
    private Boolean hasInitData = false;  
  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        context = getActivity();  
    }  
  
	/**
	 * onDestroyView��ִ�к�Activity��onDestroy��һ�����������ٵ�ǰ��ҳ�棬����Fragment�����г�Ա���������ö����ڡ��Ǿͺð��ˣ�������onCreateView��ʱ�����жϸ�ȡ���������Ƿ�Ϊ�գ�����Fragment�ĸ���ͼrootView�����������ȡ�������ݵȣ������Ϊ�վͲ����ٴ�ִ��
	 */
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
                             Bundle savedInstanceState) {  
        if (rootView == null) {  
            rootView = initView(inflater);  
        }  
        return rootView;  
    }  
  
    @Override  
    public void onActivityCreated(Bundle savedInstanceState) {  
        super.onActivityCreated(savedInstanceState);  
        if (!hasInitData) {  
            initData();  
            hasInitData = true;  
        }  
    }  
	//��Ҫ�㣺 �������rootView�Ļ���һ��Ҫ�ǵ���onDestroyView�����rootView�ȸ��Ƴ�������Ϊ�Ѿ��й������ֵ�View�ǲ����ٴ���ӵ���һ���µĸ���������ġ���������
    @Override  
    public void onDestroyView() {  
        super.onDestroyView();  
        ((ViewGroup) rootView.getParent()).removeView(rootView);  
    }  
  
    /** 
     * ����ʵ�ֳ�ʼ��View���� 
     */  
    protected abstract View initView(LayoutInflater inflater);  
  
    /** 
     * ����ʵ�ֳ�ʼ�����ݲ���(�����Լ�����) 
     */  
    public abstract void initData();  
  
    /** 
     * ��װ�������������� 
     */  
    protected void loadData(HttpRequest.HttpMethod method, String url,  
                            RequestParams params, RequestCallBack<String> callback) {  
        if (0 == NetUtils.isNetworkAvailable(getActivity())) {  
            new CustomToast(getActivity(), "�����磬�����������ӣ�", 0).show();  
        } else {  
            NetUtils.loadData(method, url, params, callback);  
        }  
    }  
}

```

#####���˼·2
```java
//replace()�������ֻ������һ��Fragment������Ҫʱ���õļ�㷽��
public void switchContent(Fragment fragment) {
	if(mContent != fragment) {
		mContent = fragment;
		mFragmentMan.beginTransaction()
			.setCustomAnimations(android.R.anim.fade_in, R.anim.slide_out)
			.replace(R.id.content_frame, fragment) // �滻Fragment��ʵ���л�
			.commit();
	}
}
//��ȷ���л���ʽ��add()���л�ʱhide()��add()��һ��Fragment���ٴ��л�ʱ��ֻ��hide()��ǰ��show()��һ��
public void switchContent(Fragment from, Fragment to) {
	if (mContent != to) {
		mContent = to;
		FragmentTransaction transaction = mFragmentMan.beginTransaction().setCustomAnimations(
				android.R.anim.fade_in, R.anim.slide_out);
		if (!to.isAdded()) {    // ���ж��Ƿ�add��
			transaction.hide(from).add(R.id.content_frame, to).commit(); // ���ص�ǰ��fragment��add��һ����Activity��
		} else {
			transaction.hide(from).show(to).commit(); // ���ص�ǰ��fragment����ʾ��һ��
		}
	}
}

//��������fragment getFragmentManager().findFragmentById
if (savedInstanceState == null) {
    // getFragmentManager().beginTransaction()...commit()
}else{
  //��ͨ��id����tag�ҵ������������UI-Fragment
  UIFragment fragment1 = getFragmentManager().findFragmentById(R.id.fragment1);
  UIFragment fragment2 = getFragmentManager().findFragmentByTag("tag");
  UIFragment fragment3 = ...
  ...
  //show()һ������
  getFragmentManager().beginTransaction()
          .show(fragment1)
          .hide(fragment2)
          .hide(fragment3)
          .hide(...)
          .commit();
}	
```


###fragment��activity����
- http://hukai.me/android-training-course-in-chinese/basics/fragments/communicating.html
- ͨ��fragment֮����ܻ���Ҫ��������������û��¼��ı�fragment�����ݡ�����fragment֮��Ľ�����Ҫͨ�����ǹ�����activity������fragment֮�䲻Ӧ��ֱ�ӽ�����





###�ο�
- http://www.yrom.net/blog/2013/03/10/fragment-switch-not-restart/
- http://hukai.me/android-training-course-in-chinese/basics/fragments/communicating.html
- http://blog.csdn.net/harvic880925/article/details/44927375
- http://www.lightskystreet.com/2015/02/02/fragment-note/
- https://github.com/bboyfeiyu/android-tech-frontier/tree/master/androidweekly/Square%20��Դ��Flow��Mortar�Ľ���
- http://toughcoder.net/blog/2015/04/30/android-fragment-the-bad-parts/
- http://blog.csdn.net/u013173289/article/details/44002371	
- http://mobile.51cto.com/abased-446691.htm
- https://hzj163.gitbooks.io/android-fragment/content/fragmentyi_xie_you_hua.html
- http://my.oschina.net/ososchina/blog/342210
- http://www.trinea.cn/android/android-performance-demo/