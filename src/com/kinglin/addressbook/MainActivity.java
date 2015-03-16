//主界面对应的activity
package com.kinglin.addressbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kinglin.dao.DBImp;
import com.kinglin.dao.Dbhelper;
import com.kinglin.model.Contact;

import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ShowToast", "NewApi" })
public class MainActivity extends ActionBarActivity {

	EditText et_search;
	ImageButton btn_search;
	ListView lv_main;
	
	List<Contact> contacts;
	
	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		getSupportActionBar().setBackgroundDrawable(MainActivity.this.getResources().getDrawable(R.drawable.actionbar_bg));
		
		//关联变量和控件
		et_search = (EditText)findViewById(R.id.et_search);
		btn_search = (ImageButton)findViewById(R.id.btn_search);
		lv_main = (ListView)findViewById(R.id.lv_main);
		
		//连接数据库并在第一次运行时创建表
		Dbhelper helper=new Dbhelper(MainActivity.this, "addressbook.db", null, 1);
	    final SQLiteDatabase db=helper.getWritableDatabase();
        String contact_table="create table if not exists contact (id text,name text,number text,mail text,sex integer,birthday text,img integer)";  
        db.execSQL(contact_table);
		
        updateListview();
       
        btn_search.setOnClickListener(new mSearchClickListener());
		
        lv_main.setOnItemClickListener(new mOnItemClickListener());
       
        lv_main.setOnItemLongClickListener(new mOnItemLongClickListener());
       
	}
	
	//长按item时，获取item的number，弹出对话框
	public class mOnItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			TextView tv_tmp_name = (TextView)view.findViewById(R.id.tv_item_name);
			
			new AlertDialog.Builder(MainActivity.this)
			.setMessage(R.string.contact_operation)
			.setTitle(tv_tmp_name.getText().toString())
			.setPositiveButton(Html.fromHtml("<img src=''/>", new ImageGetter() {  
				  
                @Override  
                public Drawable getDrawable(String source) {  
                    // TODO Auto-generated method stub  
                    Drawable mDrawable = getResources()  
                            .getDrawable(R.drawable.img_edit);  
                    mDrawable.setBounds(0, 0, 80, 80);  
                    return mDrawable;  
                }  
            }, null), new mEditButtonClickLister(position))
			.setNegativeButton(Html.fromHtml("<img src=''/>", new ImageGetter() {  
				  
                @Override  
                public Drawable getDrawable(String source) {  
                    // TODO Auto-generated method stub  
                    Drawable mDrawable = getResources()  
                            .getDrawable(R.drawable.img_delete);  
                    mDrawable.setBounds(0, 0, 80, 80);  
                    return mDrawable;  
                }  
            }, null), new mDeleteButtonClickLister(position))
			.show();
			
			return true;
		}
		
	}
	
	//删除按钮事件
	public class mDeleteButtonClickLister implements OnClickListener{

		int position;
		public mDeleteButtonClickLister(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Dbhelper helper=new Dbhelper(MainActivity.this, "addressbook.db", null, 1);
		    final SQLiteDatabase db=helper.getWritableDatabase();
			DBImp dbImp4 = new DBImp(db);
			if(dbImp4.deleteContact(contacts.get(position).getId())){
				updateListview();
				Toast.makeText(MainActivity.this, R.string.delete_suc, 1000).show();
			}else {
				Toast.makeText(MainActivity.this, R.string.delete_fail, 1000).show();
			}
		}
		
	}
	
	//点击编辑按钮事件
	public class mEditButtonClickLister implements OnClickListener{

		int position;
		public mEditButtonClickLister(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Contact editContact = contacts.get(position);
			
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, EditActivity.class);
			intent.putExtra( "editContact",(Serializable)editContact);
			startActivity(intent);
		}
	}
	
	//单击item时，获取此item中的number，传给DBImp，然后得到返回的Contact
    //然后将这个对象传给DetailActivity
	public class mOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, DetailsActivity.class);
			
			Contact contactDetails = contacts.get(position);
			intent.putExtra( "contactDetails",(Serializable)contactDetails);
			startActivity(intent);
			
		}
		
	}
	
	//查找按钮响应，获取查询狂中内容，传给DBImp对象，并将得到的list传给SearchResultActivity
	public class mSearchClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			String str_search = et_search.getText().toString();
			if(str_search.length()>0){
				Dbhelper helper=new Dbhelper(MainActivity.this, "addressbook.db", null, 1);
			    final SQLiteDatabase db=helper.getWritableDatabase();
				DBImp dbImp1 = new DBImp(db);
				List<Contact> search_resultContacts = dbImp1.searContacts(str_search);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SearchResultActivity.class);
				intent.putExtra("searchRes", (Serializable)search_resultContacts);
				startActivity(intent);
			}else {
				Toast.makeText(MainActivity.this, R.string.search_content, 1000).show();
			}
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_contact) {
			Intent intent = new Intent(MainActivity.this, AddActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//返回主界面时调用此函数
	@Override
	protected void onPostResume() {
		super.onPostResume();
		updateListview();
	}
	
	//更新主界面listview
	public void updateListview() {
		lv_main.removeAllViewsInLayout();
		
		Dbhelper helper=new Dbhelper(MainActivity.this, "addressbook.db", null, 1);
	    final SQLiteDatabase db=helper.getWritableDatabase();
		DBImp dbImp = new DBImp(db);
		contacts = dbImp.getallContacts();
		
		if (contacts.size() != 0) {
			List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>(); 
			for(Contact contact : contacts){  
	            HashMap<String, Object> item = new HashMap<String, Object>();
	            item.put("id", contact.getId());
	            item.put("name", contact.getName());  
	            item.put("number", contact.getNumber());  
	            item.put("mail", contact.getMail());
	            item.put("sex", contact.getSex());
	            item.put("birthday", contact.getBirthday());
	            item.put("img", showImage(contact.getImg()));
	            
	            data.add(item);  
	        }  
			SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listitem,   
	                new String[]{"name", "number","img"}, new int[]{R.id.tv_item_name, R.id.tv_item_number,R.id.iv_item_pic});  
	       
			//实现列表的显示  
	        lv_main.setAdapter(adapter);
		}
	}
	
	public int showImage(int imgnum){
		switch (imgnum) {
		case 1:
			return R.drawable.s_icon1;
		case 2:
			return R.drawable.s_icon2;
		case 3:
			return R.drawable.s_icon3;
		case 4:
			return R.drawable.s_icon4;
		case 5:
			return R.drawable.s_icon5;
		case 6:
			return R.drawable.s_icon6;
		case 7:
			return R.drawable.s_icon7;
		case 8:
			return R.drawable.s_icon8;
		case 9:
			return R.drawable.s_icon9;
		default:
			return R.drawable.s_icon1;
		}
	}
}
