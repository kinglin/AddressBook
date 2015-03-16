//��ӻ�༭ҳ���Ӧ��activity
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
		 * ���ͼƬ��ť�¼�
		 */
		ibtnSelectIcon = (ImageButton) findViewById(R.id.imgBtn_icon);
		//���ͼƬѡ��ť
		ibtnSelectIcon.setOnClickListener(new ImgButtonClickListener());
		
		
		/**
		 * ����ѡ���¼�
		 */
		rgSex = (RadioGroup) findViewById(R.id.rg_sex);
		rbtnMale = (RadioButton) findViewById(R.id.rbtn_male);
		rbtnFemale = (RadioButton) findViewById(R.id.rbtn_female);

		//��ѡ��ѡ���¼�
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
		 * ѡ�������¼�
		 */
		etShowBirth = (EditText) findViewById(R.id.et_birthday);
		btnSelectBirth = (ImageButton) findViewById(R.id.btn_selectbirth);
		
		//��ȡ��ǰ��������
		c = Calendar.getInstance();
		
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		//��ť�����Ӧ�¼�
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
		
		
		
		
		//save��ť������Ӧ����ȡ�������������ݸ���contact��Ȼ��newһ��dbimp(db)������ȥadd������Ȼ��ٵ����activity
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
		
		
		//cancel��ť��ֱ�ӻٵ����activity
		btnCancel = (ImageButton)findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AddActivity.this.finish();
				
			}
		});
	}
	
	//���ѡ��ͼƬ��ť���¼���Ӧ
	private class ImgButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			final IconSelectDlg dlg= new IconSelectDlg(AddActivity.this);
			dlg.setTitle(R.string.select_photo);
			dlg.show();
			
			//������Dialog��ʧ��Ҫ�������¼�
			dlg.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					//�жϵ����λ�ôӶ�������Ӧ��ͷ��
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
	
	//��������
	private void updateDisplay(){
		etShowBirth.setText(new StringBuilder().append(mYear).append(
				"-"+((mMonth+1) < 10 ? "0"+(mMonth+1) : (mMonth+1))).append(
				"-"+((mDay < 10) ? "0"+mDay : mDay)));
	}
		
	//���ڿؼ����¼�
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

