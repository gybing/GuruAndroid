package cn.dcs.leef.provider3;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MyContentProvider";
    private ContentResolver resolver;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insert();//��������
        //    delete(1);//ɾ����һ��
        update(4);
        query();//��ѯ
    }
    //��������
    public void insert()
    {
        Uri uri=Student.CONTENT_URI;
        //���ContentResolver����
        resolver=this.getContentResolver();
        ContentValues values=new ContentValues();
        //���ѧ����Ϣ
        values.put(Student.NMAE, "Jack");
        values.put(Student.GENDER, "��");
        values.put(Student.AGE, 20);
        //����Ϣ����
        resolver.insert(uri, values);
        Log.i(TAG, uri.toString());
    }
    //ɾ������
    //ɾ����id��
    public void delete(Integer id)
    {
        //ָ��uri
        Uri uri= ContentUris.withAppendedId(Student.CONTENT_URI, id);
        resolver=this.getContentResolver();
        resolver.delete(uri, null, null);
    }
    //����
    public void update(Integer id)
    {
        Uri uri=ContentUris.withAppendedId(Student.CONTENT_URI, id);
        resolver=this.getContentResolver();
        ContentValues values=new ContentValues();
        values.put(Student.NMAE, "xiaofang");
        values.put(Student.GENDER, "Ů");
        values.put(Student.AGE, 25);
        resolver.update(uri, values, null, null);
    }
    //��ѯ
    public void query()
    {
        Uri uri=Student.CONTENT_URI;
        String[] PROJECTION=new String[]{
                Student._ID,
                Student.NMAE,
                Student.GENDER,
                Student.AGE
        };
        resolver=this.getContentResolver();
        Cursor cursor=resolver.query(uri, PROJECTION, null, null, null);
        //�ж��α��Ƿ�Ϊ��
        if(cursor.moveToFirst())
        {
            for(int i=0;i<cursor.getCount();i++)
            {
                cursor.moveToPosition(i);
                int id=cursor.getInt(0);//���id
                String name=cursor.getString(1);//ȡ������
                String gender=cursor.getString(2);//ȡ���Ա�
                int age=cursor.getInt(3);//ȡ������
                //����ռ�
                Log.i(TAG, "id:"+id+" name:"+name+" gender:"+gender+" age:"+age);
            }
        }

    }
}
