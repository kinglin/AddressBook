//查询结果页面对应的activity
package com.kinglin.addressbook;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kinglin.dao.DBImp;
import com.kinglin.dao.Dbhelper;
import com.kinglin.model.Contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

@SuppressLint("ShowToast")
public class SearchResultActivity extends Activity {

	ListView lv_search_result;
	List<Contact> search_resultContacts;
	
	@SuppressLint("ShowToast")
	@SuppressWarnings("unchecked")
	@Override
	//接到MainActivity传过来的intent，获得其中的list，将list中的内容(姓名和号码)显示到listview
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		
		//关联变量与控件
		lv_search_result = (ListView)findViewById(R.id.lv_search_result);
		
		//获取查询结果列表
		search_resultContacts = (List<Contact>)getIntent().getSerializableExtra("searchRes");
		
		//显示list上的内容
		updatelist(search_resultContacts);
         
        lv_search_result.setOnItemClickListener(new mOnItemClickListener());
        
        lv_search_result.setOnItemLongClickListener(new mOnItemLongClickListener());
	}
	
	//长按item时，获取item的number，弹出对话框
	public class mOnItemLongClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
			TextView tv_tmp_name = (TextView)view.findViewById(R.id.tv_item_name);
			
			new AlertDialog.Builder(SearchResultActivity.this)
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
	
	//删除按钮
	public class mDeleteButtonClickLister implements OnClickListener{

		int position;
		public mDeleteButtonClickLister(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Dbhelper helper=new Dbhelper(SearchResultActivity.this, "addressbook.db", null, 1);
		    final SQLiteDatabase db=helper.getWritableDatabase();
			DBImp dbImp4 = new DBImp(db);
			if(dbImp4.deleteContact(search_resultContacts.get(position).getId())){
				Toast.makeText(SearchResultActivity.this, "delete success", 1000).show();
				SearchResultActivity.this.finish();
			}else {
				Toast.makeText(SearchResultActivity.this, "delete failed", 1000).show();
			}
		}
	}
	
	//编辑按钮
	public class mEditButtonClickLister implements OnClickListener{

		int position;
		public mEditButtonClickLister(int position) {
			this.position = position;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			Contact editContact = search_resultContacts.get(position);
			
			Intent intent = new Intent();
			intent.setClass(SearchResultActivity.this, EditActivity.class);
			intent.putExtra( "editContact",(Serializable)editContact);
			startActivity(intent);
			SearchResultActivity.this.finish();
		}
	}
	
	//单击item时，获取此item中的number，传给DBImp，然后得到返回的Contact
    //然后将这个对象传给DetailActivity
	public class mOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent();
			intent.setClass(SearchResultActivity.this, DetailsActivity.class);
			
			Contact contactDetails = search_resultContacts.get(position);
			intent.putExtra( "contactDetails",(Serializable)contactDetails);
			startActivity(intent);
		}
		
	}

	public void updatelist(List<Contact> contacts) {
		//显示list上的内容
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
		SimpleAdapter adapter = new SimpleAdapter(SearchResultActivity.this, data, R.layout.listitem,   
                new String[]{"name", "number","img"}, new int[]{R.id.tv_item_name, R.id.tv_item_number,R.id.iv_item_pic});    
       //实现列表的显示  
       lv_search_result.setAdapter(adapter);  
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
