//实体类定义
package com.kinglin.model;

import java.io.Serializable;

public class Contact implements Serializable{
	private String id; //我传给你的，保存联系人时的当前时间
	private String name; //姓名
	private String number; //号码
	private String mail;  //邮箱
	private int sex;  //性别，1为男，0为女
	private String birthday;  //出生年月日
	private int img;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getImg() {
		return img;
	}
	public void setImg(int img) {
		this.img = img;
	}
	
}
