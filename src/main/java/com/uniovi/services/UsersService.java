package com.uniovi.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uniovi.entities.Oferta;
import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private OfertasService ofertasService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	/**
	 * Metodo que devuelve los usuarios
	 * @return
	 */
	public List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		usersRepository.findAll().forEach(users::add);
		return users;
	}
	
	@Autowired
	private RolesService rolesService;

	/**
	 * Metodo para añadir usuarios
	 * @param user
	 */
	public void addUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		usersRepository.save(user);
	}

	/**
	 * Metodo que devuelve un usuario cuyo email se pasa como parametro
	 * @param email
	 * @return
	 */
	public User getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}

	/**
	 * Metodo que elimina usuarios
	 * @param users
	 */
	public void deleteUsers(List<User> users) {
		for(User user:users) {
			if(user.isSelected()) {
				ofertasService.deleteOfertaByUserId(user.getId());
				usersRepository.delete(user);
			}
		}
	}

	/**
	 * Metodo que devuelve los usuarios que no son administradores
	 * @return
	 */
	public List<User> getNotAdminUsers() {
		List<User> users = getUsers();
		List<User> notAdminUsers = new ArrayList<User>(); 
		for(User user:users) {
			if(!user.getRole().equals(rolesService.getRoles()[1])) {
				notAdminUsers.add(user);
			}
		}
		return notAdminUsers;
	}

	/**
	 * Metodo para comprar ofertas que se pasa como parametros para un usuario que se pasa como parametros
	 * @param user
	 * @param oferta
	 * @return
	 */
	@Transactional
	public boolean buyOferta(User user, Oferta oferta) {
		if(user.getDinero()>=oferta.getCantidad()) {
			usersRepository.updateDinero(user.getId(), user.getDinero()-oferta.getCantidad());
			ofertasService.updateComprada(oferta.getId(),user.getId(), true);
			return true;
		}
		return false;
	}
}
