//���ݿ�ӿ�
package com.kinglin.dao;

import java.util.List;

import com.kinglin.model.Contact;

public interface ContactDao {
	
	//ֱ�ӷ���������ϵ����Ϣ
	public List<Contact> getallContacts();
	
	//����һ��ʵ����󣬼��뵽���ݿ⣬���سɹ����1Ϊ�ɹ���0Ϊʧ��
	public boolean addContact(Contact contact);
	
	//����ʵ����󣬸�����id������ϵ������
	public boolean updateContact(Contact contact);
	
	public Contact selectContactById(String selectid) ;
	
	//����Ҫ���������ݣ������ݿ��в��ң�������ȫƥ�䣬���벿��ƥ��
	public List<Contact> searContacts(String search);  
	
	//����һ���ַ���������Ϊ���룬����������ϵ�˵�ʵ�����
	public Contact selectContact(String selectnum);
	
	//����һ���ַ���������Ϊ���룬ɾ������ϵ����Ϣ����ɾ�����1Ϊ�ɹ���0Ϊʧ��
	public boolean deleteContact(String deletenum);
	
}
