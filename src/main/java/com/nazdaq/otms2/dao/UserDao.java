package com.nazdaq.otms2.dao;

import java.util.List;

import com.nazdaq.otms2.model.User;

/**
 * @author abu.taleb
 *
 */
public interface UserDao {

	public void addUser(User user);

	public List<User> listUser();

	public User getUser(int userid);

	public void deleteUser(User user);

	public User getUser(String username);

	public String getUserNameById(int uId);

	public boolean isExistsUser(String username);

	public User getUserByEmpId(Short empId);
}
