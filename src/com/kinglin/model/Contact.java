//ʵ���ඨ��
package com.kinglin.model;

import java.io.Serializable;

public class Contact implements Serializable{
	private String id; //�Ҵ�����ģ�������ϵ��ʱ�ĵ�ǰʱ��
	private String name; //����
	private String number; //����
	private String mail;  //����
	private int sex;  //�Ա�1Ϊ�У�0ΪŮ
	private String birthday;  //����������
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
