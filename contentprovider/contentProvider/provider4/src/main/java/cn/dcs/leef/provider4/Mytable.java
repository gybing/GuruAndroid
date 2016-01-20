package cn.dcs.leef.provider4;

import android.net.Uri;
import android.provider.BaseColumns;

public class Mytable{
	public static class MyColumns implements BaseColumns {/*
		 * BaseColumns ��һ���ӿڣ����������������һ����_ID=��_id����һ����_COUNT="_ count" ��
		 * ��Android���У�ÿһ�����ݿ��������һ���ֶΣ���������ֶ���_id��
		 * ���Ե����ǹ��������ĸ�����ʱ��ֱ��ʵ��BaseColumns ���������Ǳ�Ĭ�ϵ�ӵ����_id�ֶΡ�
		 * ���û�м̳�BaseColum�࣬��Ҫ�Լ��������_id
		 */
		private MyColumns() {}
		public static final String AUTHORITY = "hx.android.test.mycontentprovider";
	    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/mytable");
	    
	    
	    public static final String CONTENT_TYPE = "vnd.Android.cursor.dir/vnd.hx.mytable";
	    public static final String CONTENT_ITEM_TYPE = "vnd.Android.cursor.item/vnd.hx.mytable";
	    public static final String DEFAULT_SORT_ORDER = "created DESC";
	    public static final String TABLE_NAME = "mytable";
	    public static final int VERSION = 1;
	    
	    public static final String TITLE = "title";
	    public static final String BODY = "body";
	    public static final String NAME = "name";
}
	

}
