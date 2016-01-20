package cn.dcs.leef.provider3;

/**
 * Created by iof on 2016/1/20.
 */
import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class StudentContentProvider extends ContentProvider {
    //�������ݿ���������
    private DbHelper dbHelper;
    //����ContentResolver ����
    private ContentResolver resolver;
    //����Uri������
    private static final UriMatcher Urimatcher;
    //ƥ����
    private static final int STUDENT=1;
    private static final int STUDENT_ID=2;
    //��Ҫ��ѯ ���м���
    private static HashMap<String,String> stu;
    //

    static
    {
        Urimatcher=new UriMatcher(UriMatcher.NO_MATCH);
        //�����Ҫƥ���Uri��ƥ���򷵻���Ӧ��ƥ����
        Urimatcher.addURI(Student.AUTHORITY, "student", STUDENT);
        Urimatcher.addURI(Student.AUTHORITY, "student/#", STUDENT_ID);
        //ʵ��������
        stu=new HashMap<String,String>();
        //��Ӳ�ѯ��map
        stu.put(Student._ID, Student._ID);
        stu.put(Student.NMAE,Student.NMAE );
        stu.put(Student.GENDER, Student.GENDER);
        stu.put(Student.AGE,Student.AGE );
    }

    /**
     * ��ʼ��
     */
    @Override
    public boolean onCreate() {
        //����DbHelper����
        dbHelper=new DbHelper(getContext());
        return true;
    }
    /**
     * ��ѯ����
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db=dbHelper.getReadableDatabase();
        Cursor cursor;
        switch(Urimatcher.match(uri))
        {
            case STUDENT:
                cursor=db.query(DbHelper.TABLE_NAME,projection , selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case STUDENT_ID:
                String stuId=uri.getPathSegments().get(1);
                String where="Student._ID="+stuId;
                if(selection!=null&&"".equals(selection.trim()))//trim()�����Ĺ�����ȥ����β�ո�
                {
                    where+=" and "+selection;
                }
                cursor=db.query(DbHelper.TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
                break;
            default:
                //�����������Uri����������Ҫ������
                throw new IllegalArgumentException("this is Unknown Uri��"+uri);
        }
        return cursor;
    }
    /**
     * �������
     */
    @Override
    public String getType(Uri uri) {


        return null;
    }
    /**
     * ��������
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        //��ÿ�д������ݿ�
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        ContentResolver resolver=this.getContext().getContentResolver();
        //�������ݣ������к�ID
        long rowid=db.insert(DbHelper.TABLE_NAME, Student.NMAE, values);
        //�������ɹ�������Uri
        if(rowid>0)
        {
            Uri stuUri=ContentUris.withAppendedId(uri, rowid);
            resolver.notifyChange(stuUri, null);//���ݷ��ͱ仯ʱ�򣬷���֪ͨ��ע������Ӧuri��
            return stuUri;
        }
        return null;
    }
    /**
     * ɾ������
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();
        resolver=this.getContext().getContentResolver();
        int count;
        //���ݷ��ص�ƥ���������Ӧ��ɾ������
        switch(Urimatcher.match(uri))
        {
            case STUDENT:
                count=db.delete(DbHelper.TABLE_NAME, selection, selectionArgs);
                break;
            case STUDENT_ID: //ֻɾ�����ڵ�id
                //getPathSegments()�����õ�һ��String��List
                String stuId=uri.getPathSegments().get(1);
                count=db.delete(DbHelper.TABLE_NAME, Student._ID+"="+stuId+(!TextUtils.isEmpty(selection) ?
                        " and ("+selection+')':""), selectionArgs);
                break;
            default:
                //�����������Uri����������Ҫ������
                throw new IllegalArgumentException("this is Unknown Uri��"+uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }
    /**
     * ��������
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        SQLiteDatabase db=dbHelper.getWritableDatabase();//����һ���ɶ������ݿ�
        resolver=this.getContext().getContentResolver();
        int count;
        switch(Urimatcher.match(uri))
        {
            case STUDENT:
                count=db.update(DbHelper.TABLE_NAME, values, selection, selectionArgs);
                break;
            case STUDENT_ID:
                String stuId=uri.getPathSegments().get(1);//���id
                count=db.update(DbHelper.TABLE_NAME, values,Student._ID+"="+stuId+(!TextUtils.isEmpty(selection) ?
                        " and ("+selection+')':""), selectionArgs);
                break;
            default:
                //�����������Uri����������Ҫ������
                throw new IllegalArgumentException("this is Unknown Uri��"+uri);
        }
        resolver.notifyChange(uri, null);
        return count;
    }
}
