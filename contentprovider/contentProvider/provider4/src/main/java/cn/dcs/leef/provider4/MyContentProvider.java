package cn.dcs.leef.provider4;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * ContentProvider��ʲôʱ�򴴽��ģ���˭�����ģ�����ĳ��Ӧ�ó���������ݣ��Ƿ���Ҫ�������Ӧ�ó���
 * ��������� Android SDK��û����ȷ˵�������Ǵ����ݹ���ĽǶȳ�����ContentProviderӦ����Android��ϵͳ����ʱ�ʹ����ˣ������̸�������ݹ����ˡ� 
 * ���Ҫ����AndroidManifest.XML��ʹ��Ԫ����ȷ���塣
 *  ���ܻ��ж������ͬʱͨ��ContentResolver����һ��ContentProvider���᲻�ᵼ�������ݿ������ġ����� �ݡ���
 *  �������һ������Ҫ���ݿ���ʵ�ͬ��������������д���ͬ������AndroidManifest.XML�ж���ContentProvider��ʱ ����Ҫ������Ԫ��multiprocess���Ե�ֵ��
 *  ����һ����Android��ContentResolver���ṩ��notifyChange() �ӿڣ������ݸı�ʱ��֪ͨ����ContentObserver������ط�Ӧ��ʹ���˹۲���ģʽ��
 *  ��ContentResolver��Ӧ����һЩ���� register��unregister�Ľӿڡ� 
 *  ���ˣ��Ѿ���ContentProvider�ṩ�˱Ƚ�ȫ��ķ�����������δ���ContentProvider��
 *  ��ͨ��2�ַ���������һ���������Լ��� ContentProvider���߽����������ӵ�һ���Ѿ����ڵ�ContentProvider�У���Ȼǰ��������ͬ�������Ͳ�����д�� Content provider��Ȩ�ޡ�
 * 
 * */
public class MyContentProvider extends ContentProvider {
	
	private static final String TABLE_NAME = "mytable";
	private static final int TABLES = 1;
	private static final int TABLE_ID = 2;
	private static final UriMatcher sUriMatcher ;
	static{//ע����Ҫƥ���Uri
		/**
		 * 	ʲô��URI��
			�����ΪA��B��C��D 4�����֣�
			A����׼ǰ׺������˵��һ��Content Provider������Щ���ݣ��޷��ı�ģ�"content://"
			B��URI�ı�ʶ�������������ĸ�Content Provider�ṩ��Щ���ݡ����ڵ�����Ӧ�ó���Ϊ�˱�֤URI��ʶ��Ψһ�ԣ���������һ�������ġ�Сд�� �����������ʶ�� Ԫ�ص� authorities������˵����һ���Ƕ����ContentProvider�İ�.������� ;"content://hx.android.text.myprovider"
			C��·������֪���ǲ���·����ͨ�׵Ľ�������Ҫ���������ݿ��б�����֣�������Ҳ�����Լ����壬�ǵ���ʹ�õ�ʱ�򱣳�һ�¾�ok�ˣ�"content://hx.android.text.myprovider/tablename"
			D�����URI�а�����ʾ��Ҫ��ȡ�ļ�¼��ID����ͷ��ظ�id��Ӧ�����ݣ����û��ID���ͱ�ʾ����ȫ���� "content://hx.android.text.myprovider/tablename/#" #��ʾ����id
		 * 
		 * 
		 * 
		 *  UriMatcher������ƥ��Uri�������÷����£�
			1.���Ȱ�����Ҫƥ��Uri·��ȫ����ע���ϣ����£� 
			//����UriMatcher.NO_MATCH��ʾ��ƥ���κ�·���ķ�����(-1)��
			 *  UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH); //���match()����ƥ��
			 *  content://com.changcheng.sqlite.provider.contactprovider/contact·��������ƥ����Ϊ1 
			 *  uriMatcher.addURI(��com.changcheng.sqlite.provider.contactprovider��, ��contact��, 1); //�����Ҫƥ��uri�����ƥ��ͻ᷵��ƥ���� //���match()����ƥ�� 
			 *  content://com.changcheng.sqlite.provider.contactprovider/contact/230·��������ƥ����Ϊ2 
			 *  uriMatcher.addURI(��com.changcheng.sqlite.provider.contactprovider��, ��contact/#��, 2);//#��Ϊͨ���
			2.ע������Ҫƥ���Uri�󣬾Ϳ���ʹ��uriMatcher.match(uri)�����������Uri����ƥ�䣬���ƥ��ͷ���ƥ���룬ƥ�����ǵ���addURI()��������ĵ���������������ƥ��content://com.changcheng.sqlite.provider.contactprovider/contact·�������ص�ƥ����Ϊ1��
		 * 
		 * 
		 * */
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(Mytable.MyColumns.AUTHORITY, TABLE_NAME, TABLES);
		sUriMatcher.addURI(Mytable.MyColumns.AUTHORITY, TABLE_NAME+"/#", TABLE_ID);
	
	};


	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		 
		return db.delete(TABLE_NAME, arg1, arg2);

	}
	 
	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
			case TABLES:
			return Mytable.MyColumns.CONTENT_TYPE;
			case TABLE_ID:
			return Mytable.MyColumns.CONTENT_ITEM_TYPE;
			default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

	}

	int i = 1;
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {
		// TODO Auto-generated method stub
		if (sUriMatcher.match(uri) != TABLES) {
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		ContentValues values;
		if (initialValues != null) {
			Log.i("##not null", "initialValues");
			values = new ContentValues(initialValues);
		} 
		else {
			Log.i("##null", "initialValues");
			values = new ContentValues();
		}
		
		if (values.containsKey(Mytable.MyColumns._ID) == false) {
			values.put(Mytable.MyColumns._ID, i);
			i++;
		}
		if (values.containsKey(Mytable.MyColumns.TITLE) == false) {
			values.put(Mytable.MyColumns.TITLE, "title"+i);
		}
		if (values.containsKey(Mytable.MyColumns.BODY) == false) {
			values.put(Mytable.MyColumns.BODY,"body"+i);
		}
		if (values.containsKey(Mytable.MyColumns.NAME) == false) {
		values.put(Mytable.MyColumns.NAME, "name"+i);
		}
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(TABLE_NAME, Mytable.MyColumns.BODY, values);
		if (rowId > 0) {
			/**
			 * ������õ�������ContentUris ������ʵ�÷���
			 * withAppendedId(uri, id)����Ϊ·������ID����
			 * parseId(uri)�������ڴ�·���л�ȡID����
			 */
			Uri myUri= ContentUris.withAppendedId(Mytable.MyColumns.CONTENT_URI, rowId);
			return myUri;//����ֵΪһ��uri
		}
		throw new SQLException("Failed to insert row into " + uri);

	}

	DatabaseHelper mOpenHelper ;
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = new DatabaseHelper(getContext());	
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();//��һ������SQL��ѯ���ĸ�����
		switch (sUriMatcher.match(uri)) {
			case TABLES:
				qb.setTables(TABLE_NAME);
				break;
			case TABLE_ID:
				qb.setTables(TABLE_NAME);
				qb.appendWhere(Mytable.MyColumns._ID + "="
						+ uri.getPathSegments().get(1));
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}
 
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, sortOrder);
		return c;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String rowId = uri.getPathSegments().get(1);
		return db.update(TABLE_NAME, values, Mytable.MyColumns._ID + "="
		+ rowId, null);

	}

}
