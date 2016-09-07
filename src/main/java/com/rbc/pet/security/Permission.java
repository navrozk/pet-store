package com.rbc.pet.security;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
	
	CREATE, READ, DELETE;

	@Override
	public String getAuthority() {
		return name();
	}

	public static final Permission[] USER_PERMISSIONS = new Permission[] { READ, };

	public static final Permission[] ADMIN_PERMISSIONS = new Permission[] { CREATE, READ, DELETE };
}
