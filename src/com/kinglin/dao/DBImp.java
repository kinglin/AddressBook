//数据库接口实现
package com.kinglin.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kinglin.addressbook.R.id;
import com.kinglin.model.Contact;

public class DBImp implements ContactDao{
	
	private SQLiteDatabase db;
	
	public DBImp(SQLiteDatabase db){
		this.db = db;
	}

	@Override
	public List<Contact> getallContacts() {
		List<Contact> contacts = new ArrayList<Contact>();
		String id,name,number,mail,birthday;
		int img,sex;
		Cursor cursor = db.query("contact", 
				new String[]{"id","name","number","mail","sex","birthday","img"}, null, null,null,null,null);

		while(cursor.moveToNext()){
			Contact contact = new Contact();
			id = cursor.getString(cursor.getColumnIndex("id"));
			name=cursor.getString(cursor.getColumnIndex("name"));
			number=cursor.getString(cursor.getColumnIndex("number"));
			mail=cursor.getString(cursor.getColumnIndex("mail"));
			sex=cursor.getInt(cursor.getColumnIndex("sex"));
			birthday=cursor.getString(cursor.getColumnIndex("birthday"));
			img = cursor.getInt(cursor.getColumnIndex("img"));
			contact.setId(id);
			contact.setName(name);
			contact.setNumber(number);
			contact.setMail(mail);
			contact.setSex(sex);
			contact.setBirthday(birthday);
			contact.setImg(img);
			contacts.add(contact);
		}   
		return contacts;
	}

	@Override
	public List<Contact> searContacts(String search) {
		
		List<Contact> contacts = new ArrayList<Contact>();
		String id,name,number,mail,birthday;
		int img,sex;
		Cursor cursor = db.rawQuery("select id,name,number,mail,sex,birthday,img from contact where name like '%"+search+"%' or number like '%"+search+"%'",null);

		while(cursor.moveToNext()){
			Contact contact = new Contact();
			id = cursor.getString(cursor.getColumnIndex("id"));
			name=cursor.getString(cursor.getColumnIndex("name"));
			number=cursor.getString(cursor.getColumnIndex("number"));
			mail=cursor.getString(cursor.getColumnIndex("mail"));
			sex=cursor.getInt(cursor.getColumnIndex("sex"));
			birthday=cursor.getString(cursor.getColumnIndex("birthday"));
			img = cursor.getInt(cursor.getColumnIndex("img"));
			contact.setId(id);
			contact.setName(name);
			contact.setNumber(number);
			contact.setMail(mail);
			contact.setSex(sex);
			contact.setBirthday(birthday);
			contact.setImg(img);
			contacts.add(contact);
		}   
		return contacts;
	}

	@Override
	public Contact selectContact(String selectnum) {
		Contact contact = new Contact();
		int sex,img;
		String id,name,number,mail,birthday;
		Cursor cursor = db.query("contact", 
				new String[]{"id","name","number","mail","sex","birthday","img"}, 
				"number=?", 
				new String[]{selectnum}, 
				null,null,null);

		while(cursor.moveToNext()){
			id = cursor.getString(cursor.getColumnIndex("id"));
			name=cursor.getString(cursor.getColumnIndex("name"));
			number=cursor.getString(cursor.getColumnIndex("number"));
			mail=cursor.getString(cursor.getColumnIndex("mail"));
			sex=cursor.getInt(cursor.getColumnIndex("sex"));
			birthday=cursor.getString(cursor.getColumnIndex("birthday"));
			img = cursor.getInt(cursor.getColumnIndex("img"));
			contact.setId(id);
			contact.setName(name);
			contact.setNumber(number);
			contact.setMail(mail);
			contact.setSex(sex);
			contact.setBirthday(birthday);
			contact.setImg(img);
		}   
		return contact;
	}
	
	public Contact selectContactById(String selectid) {
		Contact contact = new Contact();
		int sex,img;
		String id,name,number,mail,birthday;
		Cursor cursor = db.query("contact", 
				new String[]{"id","name","number","mail","sex","birthday","img"}, 
				"id=?", 
				new String[]{selectid}, 
				null,null,null);

		while(cursor.moveToNext()){
			id = cursor.getString(cursor.getColumnIndex("id"));
			name=cursor.getString(cursor.getColumnIndex("name"));
			number=cursor.getString(cursor.getColumnIndex("number"));
			mail=cursor.getString(cursor.getColumnIndex("mail"));
			sex=cursor.getInt(cursor.getColumnIndex("sex"));
			birthday=cursor.getString(cursor.getColumnIndex("birthday"));
			img = cursor.getInt(cursor.getColumnIndex("img"));
			contact.setId(id);
			contact.setName(name);
			contact.setNumber(number);
			contact.setMail(mail);
			contact.setSex(sex);
			contact.setBirthday(birthday);
			contact.setImg(img);
		}   
		return contact;
	}

	@Override
	public boolean deleteContact(String deleteid) {
		db.execSQL("Delete from contact where id=?", new String[]{deleteid});
		return true;
	}

	@Override
	public boolean addContact(Contact contact) {
		ContentValues cValue = new ContentValues();  
	    cValue.put("id",contact.getId());  
	    cValue.put("name",contact.getName());
	    cValue.put("number", contact.getNumber());
	    cValue.put("mail", contact.getMail());
	    cValue.put("sex", contact.getSex());
	    cValue.put("birthday", contact.getBirthday());
	    cValue.put("img", contact.getImg());
	    db.insert("contact",null,cValue); 
		return true;
	}

	@Override
	public boolean updateContact(Contact contact) {
		String sql = "update contact set name='"+contact.getName().toString()+
				"',number='"+contact.getNumber().toString()+
				"',mail='"+contact.getMail().toString()+
				"',sex="+contact.getSex()+
				",birthday='"+contact.getBirthday().toString()+
				"',img="+contact.getImg()+
				" where id='"+contact.getId().toString()+"'";
	    db.execSQL(sql);   
		return true;
	}
}






