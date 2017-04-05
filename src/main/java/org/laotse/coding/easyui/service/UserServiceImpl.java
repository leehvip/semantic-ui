/*******************************************************************************
 *
 * Copyright (c) 2001-2017 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on Apr 5, 2017 10:42:49 PM
 *******************************************************************************/

package org.laotse.coding.easyui.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.laotse.coding.easyui.model.User;
import org.laotse.coding.easyui.util.UuidUtils;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl. <br>
 *
 * @author ZhongWen Li (mailto: lizw@primeton.com)
 */
@Service
public class UserServiceImpl implements UserService {
	
	private Map<String, User> users = new ConcurrentHashMap<>();

	/* (non-Javadoc)
	 * @see org.laotse.coding.easyui.service.UserService#create(org.apache.catalina.User)
	 */
	@Override
	public User create(User user) {
		if (null == user || StringUtils.isBlank(user.getEmail())) {
			return null;
		}
		User old = null;
		if (null != (old = get(user.getEmail()))) {
			return old;
		}
		String id = UuidUtils.generateNewId();
		user.setId(id);
		users.put(id, user);
		return user;
	}

	/* (non-Javadoc)
	 * @see org.laotse.coding.easyui.service.UserService#update(org.apache.catalina.User)
	 */
	@Override
	public User update(User user) {
		if (null == user) {
			return null;
		}
		User old = null;
		if (StringUtils.isNotBlank(user.getId()) && null != (old = users.get(user.getId()))) {
			users.put(user.getId(), user);
			return user;
		}
		if (StringUtils.isNotBlank(user.getEmail()) && null != (old = get(user.getEmail()))) {
			String id = StringUtils.isBlank(old.getId()) ? UuidUtils.generateNewId() : old.getId();
			users.put(id, user);
			return user;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.laotse.coding.easyui.service.UserService#get(java.lang.String)
	 */
	@Override
	public User get(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		if (users.containsKey(id)) {
			return users.get(id);
		}
		for (User user : users.values()) {
			if (StringUtils.equals(id, user.getEmail())) {
				return user;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.laotse.coding.easyui.service.UserService#remove(java.lang.String)
	 */
	@Override
	public User remove(String id) {
		if (StringUtils.isBlank(id)) {
			return null;
		}
		if (users.containsKey(id)) {
			User user = users.get(id);
			users.remove(id);
			return user;
		}
		for (User user : users.values()) {
			if (StringUtils.equals(id, user.getEmail())) {
				users.remove(user);
				return user;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.laotse.coding.easyui.service.UserService#query()
	 */
	@Override
	public List<User> query() {
		return Arrays.asList(users.values().toArray(new User[users.size()]));
	}

}
