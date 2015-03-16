//查看联系人页面对应的activity
package com.kinglin.addressbook;


import java.lang.reflect.Field;
import java.util.ArrayList;

import com.kinglin.model.Contact;

import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.telephony.gsm.SmsManager;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ShowToast")
@SuppressWarnings("deprecation")
public class DetailsActivity extends Activity {

	TextView tvName;
	EditText etSex,etPhone,etEmail,etBirth;
	ImageButton btnMsg,btnCall;
	ImageView ivIcon;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		Contact contactDetails = (Contact) getIntent().getSerializableExtra("contactDetails");
		
		tvName = (TextView) findViewById(R.id.tv_name);
		etSex=(EditText) findViewById(R.id.et_sex);
		etPhone=(EditText) findViewById(R.id.et_phone);
   		etEmail=(EditText) findViewById(R.id.et_email);
   		etBirth=(EditText) findViewById(R.id.et_birthday);
   		ivIcon = (ImageView)findViewById(R.id.iv_icon);
 
   		if(contactDetails.getSex() == 1){
   			etSex.setText("male");
   		}else {
			etSex.setText("female");
		}
   		
   		
   		tvName.setText(contactDetails.getName().toString());
   		etPhone.setText(contactDetails.getNumber().toString());
   		etEmail.setText(contactDetails.getMail().toString());
   		etBirth.setText(contactDetails.getBirthday().toString());
   		ivIcon.setImageResource(showImage(contactDetails.getImg()));
   		
		btnCall = (ImageButton) findViewById(R.id.btn_call);
		btnMsg = (ImageButton) findViewById(R.id.btn_msg);
		
		btnCall.setOnClickListener(new CallButtonClickListener());
		btnMsg.setOnClickListener(new SendButtonClickListener());
		
	}
	
	//打电话事件
	private class CallButtonClickListener implements View.OnClickListener{
       	public void onClick(View v){
       		
       		String aPhone=etPhone.getText().toString();
       		Intent intent =new Intent();
       		intent.setAction("android.intent.action.CALL");
       		intent.addCategory("android.intent.category.DEFAULT");
       		intent.setData(Uri.parse("tel:"+aPhone));
       		startActivity(intent);
       	}
    }
	
	AlertDialog dialog;
	//发送短信事件
    @SuppressLint({ "InflateParams", "NewApi" })
	private class SendButtonClickListener implements View.OnClickListener{
    	public void onClick(View v){
    		LayoutInflater Anew = LayoutInflater.from(DetailsActivity.this);  
       		final View view = Anew.inflate(R.layout.message, null); 
       		
       		final String aPhone=etPhone.getText().toString();
    		
    		dialog=new AlertDialog.Builder(DetailsActivity.this)
    		.setIcon(android.R.drawable.ic_dialog_info)
    		.setView(view)
    		.setPositiveButton(Html.fromHtml("<img src=''/>", new ImageGetter() {  
				  
                @Override  
                public Drawable getDrawable(String source) {  
                    // TODO Auto-generated method stub  
                    Drawable mDrawable = getResources()  
                            .getDrawable(R.drawable.img_send);  
                    mDrawable.setBounds(0, 0, 80, 80);  
                    return mDrawable;  
                }  
            }, null), new DialogInterface.OnClickListener() {
				
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					EditText aM=(EditText)view.findViewById(R.id.message);
					String aMessage=aM.getText().toString();
					
					if(aMessage.length()==0){
						Field field;
						try {
							field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true); 
							field.set(dialog, false);
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ReflectiveOperationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
						Toast.makeText(DetailsActivity.this, R.string.input_message, 1000).show();
					}else {
						SmsManager manager= SmsManager.getDefault();
						ArrayList<String> texts=manager.divideMessage(aMessage);
						for(String text:texts){
							manager.sendTextMessage(aPhone, null, text, null, null);
						}
						Toast.makeText(getApplicationContext(), R.string.send_suc, Toast.LENGTH_SHORT).show();
						try {
							Field field;
							field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
							field.setAccessible(true);
							field.set(dialog, true);
						} catch (NoSuchFieldException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ReflectiveOperationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
				}

			}).setNegativeButton(Html.fromHtml("<img src=''/>", new ImageGetter() {  
				  
                @Override  
                public Drawable getDrawable(String source) {  
                    // TODO Auto-generated method stub  
                    Drawable mDrawable = getResources()  
                            .getDrawable(R.drawable.img_back);  
                    mDrawable.setBounds(0, 0, 80, 80);  
                    return mDrawable;  
                }  
            }, null),new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					try {
						Field field;
						field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
						field.setAccessible(true);
						field.set(dialog, true);
					} catch (NoSuchFieldException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ReflectiveOperationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).create();
    		
    		dialog.show();
    		
       	}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public int showImage(int imgnum){
		switch (imgnum) {
		case 1:
			return R.drawable.icon1;
		case 2:
			return R.drawable.icon2;
		case 3:
			return R.drawable.icon3;
		case 4:
			return R.drawable.icon4;
		case 5:
			return R.drawable.icon5;
		case 6:
			return R.drawable.icon6;
		case 7:
			return R.drawable.icon7;
		case 8:
			return R.drawable.icon8;
		case 9:
			return R.drawable.icon9;
		default:
			return R.drawable.icon1;
		}
	}
}
