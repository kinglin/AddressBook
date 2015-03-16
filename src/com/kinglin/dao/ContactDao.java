//数据库接口
package com.kinglin.dao;

import java.util.List;

import com.kinglin.model.Contact;

public interface ContactDao {
	
	//直接返回所有联系人信息
	public List<Contact> getallContacts();
	
	//传入一个实体对象，加入到数据库，返回成功与否，1为成功，0为失败
	public boolean addContact(Contact contact);
	
	//传入实体对象，根据其id更新联系人内容
	public boolean updateContact(Contact contact);
	
	public Contact selectContactById(String selectid) ;
	
	//传入要搜索的内容，到数据库中查找，姓名完全匹配，号码部分匹配
	public List<Contact> searContacts(String search);  
	
	//传入一个字符串，内容为号码，返回这名联系人的实体对象
	public Contact selectContact(String selectnum);
	
	//传入一个字符串，内容为号码，删除此联系人信息返回删除与否，1为成功，0为失败
	public boolean deleteContact(String deletenum);
	
}
