//添加或编辑页面对应的activity
package com.kinglin.addressbook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.kinglin.dao.DBImp;
import com.kinglin.dao.Dbhelper;
import com.kinglin.model.Contact;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class AddActivity extends Activity {

	EditText etName,etNumber,etMail;
	
	ImageButton ibtnSelectIcon;
	
	RadioGroup rgSex;
	int tmp_sex = -1;
	RadioButton rbtnMale,rbtnFemale;
	
	ImageButton btnSelectBirth;
	ImageButton btnAdd,btnCancel;
	EditText etShowBirth;
	Calendar c;
	int image = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	
	
	@SuppressLint({ "ShowToast", "SimpleDateFormat", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contact);
		
		Dbhelper helper=new Dbhelper(this, "addressbook.db", null, 1);
        final SQLiteDatabase db=helper.getWritableDatabase();
		
		
		/**
		 * 点击图片按钮事件
		 */
		ibtnSelectIcon = (ImageButton) findViewById(R.id.imgBtn_icon);
		//点击图片选择按钮
		ibtnSelectIcon.setOnClickListener(new ImgButtonClickListener());
		
		
		/**
		 * 单项选择事件
		 */
		rgSex = (RadioGroup) findViewById(R.id.rg_sex);
		rbtnMale = (RadioButton) findViewById(R.id.rbtn_male);
		rbtnFemale = (RadioButton) findViewById(R.id.rbtn_female);

		//单选框选择事件
		rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				
				if(AddActivity.this.rbtnMale.getId() == checkedId)
				{
					tmp_sex = 1;
				}
				else if(AddActivity.this.rbtnFemale.getId() == checkedId){
					tmp_sex = 0;
				}
				
			}
		});
		
		
		/**
		 * 选择生日事件
		 */
		etShowBirth = (EditText) findViewById(R.id.et_birthday);
		btnSelectBirth = (ImageButton) findViewById(R.id.btn_selectbirth);
		
		//获取当前的年月日
		c = Calendar.getInstance();
		
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		//按钮点击响应事件
		btnSelectBirth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DatePickerDialog dpdlg = new DatePickerDialog(AddActivity.this, 
						mDateSetListner, mYear, mMonth, mDay);
				dpdlg.getDatePicker().setMaxDate(c.getTimeInMillis());
				
				dpdlg.show();
				
				updateDisplay();
				
			}
		});
		
		
		
		
		//save按钮功能响应，获取界面上所有内容赋给contact，然后new一个dbimp(db)，调用去add函数，然后毁掉这个activity
		etName = (EditText)findViewById(R.id.et_name);
		etNumber = (EditText)findViewById(R.id.et_phone);
		etMail = (EditText)findViewById(R.id.et_email);
		btnAdd = (ImageButton)findViewById(R.id.btn_add);
		
		btnAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Contact newContact = new Contact();
				SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");       
				String id = sDateFormat.format(new java.util.Date()); 
				
				if (tmp_sex == -1) {
					Toast.makeText(AddActivity.this, R.string.input_sex, 1000).show();
				} else if (etName.getText().length()==0) {
					Toast.makeText(AddActivity.this, R.string.input_name, 1000).show();
				}else if (etNumber.getText().length() != 8 && etNumber.getText().length() != 11) {
					Toast.makeText(AddActivity.this, R.string.input_number, 1000).show();
				}else if (etMail.getText().length() != 0 && etMail.getText().toString().indexOf("@") <= 0) {
					Toast.makeText(AddActivity.this, R.string.input_mail, 1000).show();
				}else {
					newContact.setId(id);
					newContact.setName(etName.getText().toString());
					newContact.setNumber(etNumber.getText().toString());
					newContact.setMail(etMail.getText().toString());
					newContact.setSex(tmp_sex);
					newContact.setBirthday(etShowBirth.getText().toString());
					newContact.setImg(image);
					
					DBImp dbImp = new DBImp(db);
					boolean flag = dbImp.addContact(newContact);
					if(flag){
						Toast.makeText(AddActivity.this, R.string.add_suc, 1000).show();
						AddActivity.this.finish();
					}else {
						Toast.makeText(AddActivity.this, R.string.add_fail, 1000).show();
					}
				}
			}
		});
		
		
		//cancel按钮，直接毁掉这个activity
		btnCancel = (ImageButton)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AddActivity.this.finish();
				
			}
		});
	}
	
	//点击选择图片按钮后事件响应
	private class ImgButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			final IconSelectDlg dlg= new IconSelectDlg(AddActivity.this);
			dlg.setTitle(R.string.select_photo);
			dlg.show();
			
			//监听当Dialog消失后要触发的事件
			dlg.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					//判断点击的位置从而设置相应的头像
					switch (dlg.getPosition()) {
					case 0:
						ibtnSelectIcon.setImageResource(R.drawable.icon1);
						image = 1;
						break;
					case 1:
						ibtnSelectIcon.setImageResource(R.drawable.icon2);
						image = 2;
						break;
					case 2:
						ibtnSelectIcon.setImageResource(R.drawable.icon3);
						image = 3;
						break;
					case 3:
						ibtnSelectIcon.setImageResource(R.drawable.icon4);
						image = 4;
						break;
					case 4:
						ibtnSelectIcon.setImageResource(R.drawable.icon5);
						image = 5;
						break;
					case 5:
						ibtnSelectIcon.setImageResource(R.drawable.icon6);
						image = 6;
						break;
					case 6:
						ibtnSelectIcon.setImageResource(R.drawable.icon7);
						image = 7;
						break;
					case 7:
						ibtnSelectIcon.setImageResource(R.drawable.icon8);
						image = 8;
						break;
					case 8:
						ibtnSelectIcon.setImageResource(R.drawable.icon9);
						image = 9;
					}
					
				}
			});
			
		}
	}
	
	//更新日期
	private void updateDisplay(){
		etShowBirth.setText(new StringBuilder().append(mYear).append(
				"-"+((mMonth+1) < 10 ? "0"+(mMonth+1) : (mMonth+1))).append(
				"-"+((mDay < 10) ? "0"+mDay : mDay)));
	}
		
	//日期控件的事件
	private DatePickerDialog.OnDateSetListener mDateSetListner = 
	new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			
			updateDisplay();
		}
	};

}

