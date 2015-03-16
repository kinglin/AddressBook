package com.kinglin.addressbook;

import java.util.Calendar;

import com.kinglin.dao.DBImp;
import com.kinglin.dao.Dbhelper;
import com.kinglin.model.Contact;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class EditActivity extends Activity {

	EditText et_name,et_number,et_mail,et_birthday;
	RadioButton rb_male,rb_female;
	RadioGroup rg_sex;
	ImageButton btn_cancel,btn_update;
	Calendar c;
	ImageButton ibtn_selecticon,btn_edit_selectbirth;
	private int mYear;
	private int mMonth;
	private int mDay;
	int tmp_sex = -1;
	String id;
	int img;
	
	@SuppressLint({ "ShowToast", "NewApi" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_contact);
		
		Dbhelper helper=new Dbhelper(this, "addressbook.db", null, 1);
        final SQLiteDatabase db=helper.getWritableDatabase();
        
        
		
		et_name = (EditText)findViewById(R.id.et_edit_name);
		et_number = (EditText)findViewById(R.id.et_edit_phone);
		et_mail = (EditText)findViewById(R.id.et_edit_email);
		et_birthday = (EditText)findViewById(R.id.et_edit_birthday);
		btn_update = (ImageButton)findViewById(R.id.btn_edit_update);
		btn_cancel = (ImageButton)findViewById(R.id.btn_edit_back);
		btn_edit_selectbirth = (ImageButton)findViewById(R.id.btn_edit_selectbirth);
		rb_male = (RadioButton)findViewById(R.id.rbtn_edit_male);
		rb_female = (RadioButton)findViewById(R.id.rbtn_edit_female);
		rg_sex = (RadioGroup)findViewById(R.id.rg_edit_sex);
		
		Contact editContact = (Contact) getIntent().getSerializableExtra("editContact");
		id = editContact.getId();
		et_name.setText(editContact.getName());
		et_number.setText(editContact.getNumber());
		et_mail.setText(editContact.getMail());
		et_birthday.setText(editContact.getBirthday());
		img = editContact.getImg();
		
		ibtn_selecticon = (ImageButton) findViewById(R.id.imgBtn_edit_icon);
		ibtn_selecticon.setImageResource(showImage(img));
		
		//点击图片选择按钮
		ibtn_selecticon.setOnClickListener(new ImgButtonClickListener());
		

		if (editContact.getSex()==1) {
			rb_male.setChecked(true);
			tmp_sex = 1;
		}else {
			rb_female.setChecked(true);
			tmp_sex = 0;
		}
		
		rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				if(EditActivity.this.rb_male.getId() == checkedId)
				{
					tmp_sex = 1;
				}
				else{
					tmp_sex = 0;
				}
				
			}
		});
		
		//获取当前的年月日
		c = Calendar.getInstance();
		
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		//按钮点击响应事件
		btn_edit_selectbirth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DatePickerDialog dpdlg = new DatePickerDialog(EditActivity.this, 
						mDateSetListner, mYear, mMonth, mDay);
				dpdlg.getDatePicker().setMaxDate(c.getTimeInMillis());
				dpdlg.show();
				
				updateDisplay();
				
			}
		});
		
		
		btn_update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Contact editContact = new Contact();
				
				if (tmp_sex == -1) {
					Toast.makeText(EditActivity.this, R.string.input_sex, 1000).show();
				} else if (et_name.getText().length()==0) {
					Toast.makeText(EditActivity.this, R.string.input_name, 1000).show();
				}else if (et_number.getText().length() != 8 && et_number.getText().length() != 11) {
					Toast.makeText(EditActivity.this, R.string.input_number, 1000).show();
				}else if (et_mail.getText().length() != 0 && et_mail.getText().toString().indexOf("@") <= 0) {
					Toast.makeText(EditActivity.this, R.string.input_mail, 1000).show();
				}else {
					editContact.setId(id);
					editContact.setName(et_name.getText().toString());
					editContact.setNumber(et_number.getText().toString());
					editContact.setMail(et_mail.getText().toString());
					editContact.setSex(tmp_sex);
					editContact.setBirthday(et_birthday.getText().toString());
					editContact.setImg(img);
					
					DBImp dbImp = new DBImp(db);
					boolean flag = dbImp.updateContact(editContact);
					if(flag){
						Toast.makeText(EditActivity.this, R.string.update_suc, 1000).show();
						EditActivity.this.finish();
					}else {
						Toast.makeText(EditActivity.this, R.string.update_fail, 1000).show();
					}
				}
			}
		});
		
		
		//cancel按钮，直接毁掉这个activity
		btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EditActivity.this.finish();
				
			}
		});
	}

	//点击选择图片按钮后事件响应
	private class ImgButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			final IconSelectDlg dlg= new IconSelectDlg(EditActivity.this);
			dlg.show();
			
			//监听当Dialog消失后要触发的事件
			dlg.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					//判断点击的位置从而设置相应的头像
					switch (dlg.getPosition()) {
					case 0:
						ibtn_selecticon.setImageResource(R.drawable.icon1);
						img = 1;
						break;
					case 1:
						ibtn_selecticon.setImageResource(R.drawable.icon2);
						img = 2;
						break;
					case 2:
						ibtn_selecticon.setImageResource(R.drawable.icon3);
						img = 3;
						break;
					case 3:
						ibtn_selecticon.setImageResource(R.drawable.icon4);
						img = 4;
						break;
					case 4:
						ibtn_selecticon.setImageResource(R.drawable.icon5);
						img = 5;
						break;
					case 5:
						ibtn_selecticon.setImageResource(R.drawable.icon6);
						img = 6;
						break;
					case 6:
						ibtn_selecticon.setImageResource(R.drawable.icon7);
						img = 7;
						break;
					case 7:
						ibtn_selecticon.setImageResource(R.drawable.icon8);
						img = 8;
						break;
					case 8:
						ibtn_selecticon.setImageResource(R.drawable.icon9);
						img = 9;
					}
					
				}
			});
			
		}
	}
	
	//更新日期
	private void updateDisplay(){
		et_birthday.setText(new StringBuilder().append(mYear).append(
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
