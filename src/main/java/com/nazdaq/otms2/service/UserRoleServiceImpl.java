package com.nazdaq.otms2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nazdaq.otms2.dao.UserRoleDao;
import com.nazdaq.otms2.model.UserRole;

/**
 * @author nasrin.akter
 *
 */
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {

	@Autowired
	private UserRoleDao userRoleDao;

	public void addUserRole(UserRole userRole) {
		userRoleDao.addUserRole(userRole);
	}

	public List<UserRole> listUserRole() {
		return userRoleDao.listUserRole();
	}

	public UserRole getUserRole(int userRoleId) {
		return userRoleDao.getUserRole(userRoleId);
	}

	public void deleteUserRole(UserRole userRole) {
		userRoleDao.deleteUserRole(userRole);
	}

	public List<String> getUserRoleName() {
		return userRoleDao.getUserRoleName();
	}

}
