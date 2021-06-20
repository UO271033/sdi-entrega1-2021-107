package com.uniovi.services;

import org.springframework.stereotype.Service;

@Service
public class RolesService {
	
	String[] roles = { "ROLE_USER", "ROLE_ADMIN" };

	/**
	 * Metodo que devuelve los posibles roles en la app
	 * @return
	 */
	public String[] getRoles() {
		return roles;
	}

}
