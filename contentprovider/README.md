#ContentProvider Demo��ԭ�����

###ContentProvider�Զ����ɿ��
- https://github.com/BoD/android-contentprovider-generator
- https://github.com/SimonVT/schematic
- https://github.com/TimotheeJeannin/ProviGen
- http://triple-t.github.io/simpleprovider/
- https://github.com/foxykeep/ContentProviderCodeGenerator
- https://code.google.com/p/mdsd-android-content-provider/
- https://github.com/hamsterksu/Android-AnnotatedSQL
- http://robotoworks.com/mechanoid/doc/db/api.html
- https://github.com/robUx4/android-contentprovider-generator (a fork of this project that generates more code)
- https://github.com/novoda/sqlite-analyzer (based on sql statements, not json)
- https://github.com/ckurtm/simple-sql-provider

###ContentProvider�ڲ�ԭ��
- http://blog.csdn.net/luoshengyang/article/details/6963418
- http://www.cnblogs.com/hnrainll/archive/2012/03/29/2424116.html


###ContentProviderȨ�� (�ο�BookProvider��BookGallery)
![](https://raw.githubusercontent.com/fitzlee/GuruAndroid/master/contentprovider/_images/bookgallery.png =200)
```java
  //Server
 <permission android:name="com.android.provider.book.READ_DATABASE" android:protectionLevel="normal" />
 <permission android:name="com.android.provider.book.WRITE_DATABASE" android:protectionLevel="normal" />
 <provider android:name="BookProvider"
            android:exported="true"
            android:authorities="com.android.provider.book"
            android:readPermission="com.android.provider.book.READ_DATABASE"
            android:writePermission="com.android.provider.book.WRITE_DATABASE" />
 //Client
 <uses-permission android:name="com.android.provider.book.READ_DATABASE" />
 <uses-permission android:name="com.android.provider.book.WRITE_DATABASE" />
```
* android:authoritiesҪд��CONTENT_URI����
* ���������Ȩ�ޣ�����ContentResolver�Ƿ���һ�������������ֱ�ӷ���; ����Ϊ�����ݰ�ȫ������һ�㶼���ContentProvider����Ȩ��

###Ҫ��
��Ҫ�������·�����
```Java
final Uri insert(Uri url, ContentValues values)
//Inserts a row into a table at the given URL.
final int delete(Uri url, String where, String[] selectionArgs)
//Deletes row(s) specified by a content URI.
final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
//Query the given URI, returning a Cursor over the result set.
final int update(Uri uri, ContentValues values, String where, String[] selectionArgs)
//Update row(s) in a content URI.
```

```
public boolean onCreate()���÷�����ContentProvider������ͻᱻ���ã�Androidϵͳ���к�ContentProviderֻ���ڱ���һ��ʹ����ʱ�Żᱻ������
	public Uri insert(Uri uri, ContentValues values)���ⲿӦ�ó���ͨ����������� ContentProvider������ݡ�
	uri���� ��ʶ�������ݵ�URI
	values���� ��Ҫ������ݵļ�ֵ��
public int delete(Uri uri, String selection, String[] selectionArgs)���ⲿӦ�ó���ͨ����������� ContentProvider��ɾ�����ݡ�
	uri������ʶ�������ݵ�URI
	selection��������ɸѡ��ӵ���䣬�硱id=1�� ���� ��id=?��
	selectionArgs������Ӧselection������������Դ���null ���� new String[]{��1��}
public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)���ⲿӦ�ó���ͨ����������� ContentProvider�е����ݽ��и��¡�
	values������Ӧ��Ҫ���µļ�ֵ�ԣ���Ϊ��Ӧ���������е��ֶΣ�ֵΪ��Ӧ���޸�ֵ
	�������ͬdelete����
public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)���ⲿӦ�ó���ͨ�����������ContentProvider�л�ȡ���ݣ�������һ��Cursor����
	projection������Ҫ��Contentprovider��ѡ����ֶΣ����Ϊ�գ��򷵻ص�Cursor���������е��ֶΡ�
	sortOrder����Ĭ�ϵ��������
	�������ͬdelete������������
public String getType(Uri uri)���÷������ڷ��ص�ǰUrl���������ݵ�MIME���͡�
	����������������ڼ������ͣ���ôMIME�����ַ���Ӧ����vnd.android.cursor.dir/��ͷ��
	���磺Ҫ�õ�����person��¼��UriΪcontent://com.wirelessqa.content.provider/profile����ô���ص�MIME�����ַ���Ӧ��Ϊ����vnd.android.cursor.dir/profile����
	���Ҫ�������������ڷǼ����������ݣ���ôMIME�����ַ���Ӧ����vnd.android.cursor.item/��ͷ��
	���磺�õ�idΪ10��person��¼��UriΪcontent://com.wirelessqa.content.provider/profile/10����ô���ص�MIME�����ַ���Ϊ����vnd.android.cursor.item/profile����
```

����ʵ�֣�(�ο�provider3,���������log��Provider1��2Ҳ��)
![](https://raw.githubusercontent.com/fitzlee/GuruAndroid/master/contentprovider/_images/provider2.png =300)
![](https://raw.githubusercontent.com/fitzlee/GuruAndroid/master/contentprovider/_images/provider1.png =400)
```java
//�ṩClient�����ӿ�
public class Student implements BaseColumns {
    public static final String AUTHORITY="com.example.provider.students";
    private Student(){} //���췽��
    public static final Uri CONTENT_URI=Uri.parse("content://"+AUTHORITY+"/student");
    public static final String CONTENT_TYPE="vnd.android.cursor.dir/vnd.example.provider.students";
    public static final String CONTENT_ITEM_TYPE="vnd.android.cursor.item/vnd.example.provider.students";
    //���ݿ��еı��ֶ�
    public static final String NMAE="name";//����
    public static final String GENDER="gender";//�Ա�
    public static final String AGE="age";//����
}

//�������ݿ�
public class DbHelper extends SQLiteOpenHelper {

    //���ݿ������
    private static final String DATABASE_NAME="student.db";
    private static final int DATABASE_VERSION=1;
    public static final String TABLE_NAME="student";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    /**
     * ����table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //����ѧ����Ϣ�������ĸ��ֶ�
        //����id���������Ա�����
        String sql="create table "+TABLE_NAME+" ("
                +Student._ID+" integer primary key,"
                +Student.NMAE+" text,"
                +Student.GENDER+" text,"
                +Student.AGE+" integer "+");";
        db.execSQL(sql);
    }
    /**
     * ���±�
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String sql="drop table if exits student";
        db.execSQL(sql);
    }
}


//ʵ��Provider
public class StudentContentProvider extends ContentProvider{
	//�������ݿ���������
    private DbHelper dbHelper;
    //����ContentResolver ����
    private ContentResolver resolver;
    //����Uri������
    private static final UriMatcher Urimatcher;
    //ƥ����
    private static final int STUDENT=1;
    private static final int STUDENT_ID=2;
	static
	{
		Urimatcher=new UriMatcher(UriMatcher.NO_MATCH);
		//�����Ҫƥ���Uri��ƥ���򷵻���Ӧ��ƥ����
		Urimatcher.addURI(AUTHORITY, "student", STUDENT);
		Urimatcher.addURI(AUTHORITY, "student/#", STUDENT_ID);
	}
	
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


```


###���÷���
```java

//get
ContentResolver resolver=this.getContentResolver();

//query
Cursor cs = resolver.query(Book.CONTENT_URI, null, null, null, Book.ID + " ASC");
Book book = new Book(cs);
cs.close();

//insert
String countryText = String.valueOf(etTitle.getText());  
String codeNum = String.valueOf(etPrice.getText());  
contentValues = new ContentValues();  
contentValues.put(Book.TITLE, countryText);  
contentValues.put(Book.PRICE, codeNum);  
resolver.insert(Book.CONTENT_URI, contentValues); 

//delete
Uri uri=ContentUris.withAppendedId(Book.CONTENT_URI, 2);
resolver.delete(uri, null, null);

//update
Uri uri=ContentUris.withAppendedId(Student.CONTENT_URI, id);
resolver=this.getContentResolver();
ContentValues values=new ContentValues();
values.put(Student.NMAE, "xiaofang");
values.put(Student.GENDER, "Ů");
values.put(Student.AGE, 25);
resolver.update(uri, values, null, null);

```


###ϵͳuri
```
��ϵ�˵�URI��
ContactsContract.Contacts.CONTENT_URI ������ϵ�˵�Uri
ContactsContract.CommonDataKinds.Phone.CONTENT_URI ������ϵ�˵ĵ绰��Uri
ContactsContract.CommonDataKinds.Email.CONTENT_URI ������ϵ�˵�Email��Uri
��ע��Contacts���������ֱ���rawContact��Data��rawContact��¼���û���id��name��
����id������ Ϊ��ContactsContract.Contacts._ID��name������ΪContactContract.Contracts.DISPLAY_NAME��
�绰��Ϣ������idΪ ContactsContract.CommonDataKinds.Phone.CONTACT_ID��
�绰����������Ϊ��ContactsContract.CommonDataKinds.Phone.NUMBER��
data����Email��ַ������Ϊ��ContactsContract.CommonDataKinds.Email.DATA
�������Ϊ��ContactsContract.CommonDataKinds.Email.CONTACT_ID)

��ý���ContentProvider��Uri���£�
MediaStore.Audio.Media.EXTERNAL_CONTENT_URI  �洢��sd���ϵ���Ƶ�ļ�
MediaStore.Audio.Media.INTERNAL_CONTENT_URI  �洢���ֻ��ڲ��洢���ϵ���Ƶ�ļ�
MediaStore.Audio.Images.EXTERNAL_CONTENT_URI SD���ϵ�ͼƬ�ļ�����
MediaStore.Audio.Images.INTERNAL_CONTENT_URI �ֻ��ڲ��洢���ϵ�ͼƬ
MediaStore.Audio.Video.EXTERNAL_CONTENT_URI SD���ϵ���Ƶ
MediaStore.Audio.Video.INTERNAL_CONTENT_URI  �ֻ��ڲ��洢���ϵ���Ƶ
(ע��ͼƬ����ʾ������Media.DISPLAY_NAME��ͼƬ����ϸ������Ϊ��Media.DESCRIPTION  ͼƬ�ı���λ�ã�Media.DATA
����URI�� Content://sms
�������еĶ���URI�� Content://sms/outbox
```


###�ο�
- http://aijiawang-126-com.iteye.com/blog/655268
- http://blog.csdn.net/bage1988320/article/details/6749870#
- http://blog.csdn.net/wirelessqa/article/details/8618831
- http://blog.chinaunix.net/uid-24129645-id-3758633.html