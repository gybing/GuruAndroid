package cn.dcs.leef.provider4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	public static String DATABASE_NAME = "mydatabase2.db";
	public static int version = 1;
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, version);
		// TODO Auto-generated constructor stub
	}
	
    @Override  
    public void onOpen(SQLiteDatabase db) {  
        super.onOpen(db);  
        //���ǵ������ݿ�ʱ�Ļص�������һ��Ҳ�����õ���
    }
	@Override
	public void onCreate(SQLiteDatabase db) {
		// �����ݿ��һ�����ɵ�ʱ���������������һ�������������������������ݿ��
		// SQL���
//		String sql = "CREATE TABLE " + MyColumns.TABLE_NAME + " (" +MyColumns._ID +"int not null,"+ MyColumns.TITLE
//		+ " text not null, " + MyColumns.BODY + " text not null," +MyColumns.NAME+"text not null"+ ");";
		String s = "CREATE TABLE \"mytable\"( [_id] int PRIMARY KEY ,[title] varchar(100) ,[body] varchar(10) ,[name] varchar(100) ) ";
		//ִ������SQL���
		db.execSQL(s);

		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// �����ݿ���Ҫ������ʱ��Androidϵͳ�������ĵ������������һ������������������ɾ�����ݱ��������µ����ݱ�
		db.execSQL("DROP TABLE IF EXISTS notes");
		onCreate(db);

	}  

}
