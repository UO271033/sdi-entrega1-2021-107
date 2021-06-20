package com.uniovi.services;

import java.util.ArrayList;
import java.util.List;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Oferta;
import com.uniovi.entities.User;
import com.uniovi.repositories.OfertasRepository;

@Service
public class OfertasService {
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private OfertasRepository ofertasRepository;
	
	/**
	 * Metodo para ñadir una oferta
	 * @param oferta
	 */
	public void addOferta(Oferta oferta) {
		oferta.setId(ofertasRepository.count() + 1);
		ofertasRepository.save(oferta);
	}
	
	/**
	 * Método para eliminar una oferta
	 * @param id
	 */
	public void deleteOferta(Long id) {
		ofertasRepository.deleteById(id);
	}
	
	/**
	 * Metodo para listar las ofertas
	 * @return
	 */
	public List<Oferta> getOfertas() {
		List<Oferta> ofertas = new ArrayList<Oferta>();
		ofertasRepository.findAll().forEach(ofertas::add);;
		return ofertas;
	}

	/**
	 * Metodo para listar las ofertas según un título que se pasa como parámetro
	 * @param title
	 * @return
	 */
	public List<Oferta> getOfertasByTitle(String title) {
		List<Oferta> ofertas = new ArrayList<Oferta>();
		title='%'+title+'%';
		ofertasRepository.findAllByTitle(title).forEach(ofertas::add);
		return ofertas;
	}

	/**
	 * Metodo que retorna una oferta cuyo id sea el que se pasa como paramtro
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Oferta getOferta(Long id) {
		Set<Oferta> consultedList = (Set<Oferta>) httpSession.getAttribute("consultedList");
		if (consultedList == null) {
			consultedList = new HashSet<Oferta>();
		}
		Oferta obtainedoferta = ofertasRepository.findById(id).get();
		consultedList.add(obtainedoferta);
		httpSession.setAttribute("consultedList", consultedList);
		return obtainedoferta;
	}

	/**
	 * Metodo para actualizar una oferta a comprada
	 * @param id
	 * @param idUser
	 * @param b
	 */
	public void updateComprada(Long id, Long idUser, boolean b) {
		ofertasRepository.updateComprada(id,idUser,b);
	}

	/**
	 * Metodo para listar las ofertas segun el comprador que se pasa como parametro
	 * @param buyer
	 * @return
	 */
	public List<Oferta> getOfertasByBuyer(User buyer) {
		List<Oferta> ofertas = new ArrayList<Oferta>();
		ofertasRepository.findAllByBuyer(buyer.getId()).forEach(ofertas::add);;
		return ofertas;
	}

	/**
	 * Metodo que borra las ofertas de un usuario cuyo id se pasa como parametros
	 * @param id
	 */
	public void deleteOfertaByUserId(long id) {
		ofertasRepository.deleteOfertaByUserId(id);
	}

	/**
	 * Metodo que lista en paginas las ofertas segun un titulo que se pasa como parametro
	 * @param pageable
	 * @param title
	 * @return
	 */
	public Page<Oferta> getOfertasByTitle(Pageable pageable, String title) {
		Page<Oferta> ofertas = new PageImpl<Oferta>(new LinkedList<Oferta>());
		title='%'+title+'%';
		ofertas = ofertasRepository.findAllByTitle(pageable,title);
		return ofertas;
	}

	/**
	 * Metodo que lista las ofertas en paginas
	 * @param pageable
	 * @return
	 */
	public Page<Oferta> getOfertas(Pageable pageable) {
		Page<Oferta> ofertas = new PageImpl<Oferta>(new LinkedList<Oferta>());
		ofertas=ofertasRepository.findAll(pageable);
		return ofertas;
	}

}
