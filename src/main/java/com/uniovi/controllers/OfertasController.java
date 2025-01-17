package com.uniovi.controllers;

import java.security.Principal;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.Oferta;
import com.uniovi.entities.User;
import com.uniovi.services.OfertasService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.AddOfertaFormValidator;

@Controller
public class OfertasController {

	@Autowired
	private OfertasService ofertasService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private AddOfertaFormValidator addOfertaFormValidator;
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityService.class);

	@RequestMapping("/oferta/list")
	public String getList(Model model, Principal principal) {
		logger.debug("Acceso a /oferta/list");
		model.addAttribute("ofertaList", ofertasService.getOfertas());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "oferta/list";
	}

	@RequestMapping(value = "/oferta/add", method = RequestMethod.GET)
	public String setOferta(Model model, Principal principal) {
		logger.debug("Acceso a /oferta/add");
		model.addAttribute("oferta", new Oferta());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "oferta/add";
	}

	@RequestMapping(value = "/oferta/add", method = RequestMethod.POST)
	public String setOferta(@ModelAttribute Oferta oferta, BindingResult result, Model model, Principal principal) {
		addOfertaFormValidator.validate(oferta, result);
		if (result.hasErrors()) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			String email = auth.getName();
			User activeUser = usersService.getUserByEmail(email);
			model.addAttribute("email", activeUser.getEmail());
			User user = usersService.getUserByEmail(principal.getName());
			model.addAttribute("activeUser", user);
			return "oferta/add";
		}
		ofertasService.addOferta(oferta);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		logger.debug("Oferta añadida correctamente");
		return "redirect:/oferta/list";
	}

	@RequestMapping(value = "/oferta/add")
	public String getOferta(Model model, Principal principal) {
		model.addAttribute("usersList", usersService.getUsers());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "oferta/add";
	}

	@RequestMapping(value = "/oferta/delete/{id}")
	public String deleteOferta(@PathVariable Long id, Principal principal, Model model) {
		ofertasService.deleteOferta(id);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		logger.debug("Oferta eliminada correctamente");
		return "redirect:/oferta/list";
	}

	@RequestMapping(value = "/oferta/search", method = RequestMethod.GET)
	public String listOferta(Model model, Pageable pageable, @RequestParam(required = false) String titleInputSearch,
			@RequestParam(required = false) boolean noMoney, Principal principal) {

		logger.debug("Acceso a /oferta/search");
		Page<Oferta> ofertas = new PageImpl<Oferta>(new LinkedList<Oferta>());

		if (titleInputSearch != null && titleInputSearch != "") {
			ofertas = ofertasService.getOfertasByTitle(pageable, titleInputSearch);
			model.addAttribute("titleSearch", titleInputSearch);
		} else {
			ofertas = ofertasService.getOfertas(pageable);
			model.addAttribute("titleSearch", "");
		}
		model.addAttribute("ofertas", ofertas.getContent());
		model.addAttribute("page", ofertas);
		model.addAttribute("noMoney", noMoney);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "oferta/search";
	}

	@RequestMapping(value = "/oferta/buy/{id}")
	public String buyOferta(Model model, @PathVariable Long id, Principal principal) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);

		boolean noMoney = !usersService.buyOferta(activeUser, ofertasService.getOferta(id));
		
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);
		return "redirect:/oferta/search?noMoney=" + noMoney;
	}

	@RequestMapping(value = "/oferta/listPurchased")
	public String buyOferta(Model model, Principal principal) {

		logger.debug("Acceso a /oferta/listPurchased");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		User activeUser = usersService.getUserByEmail(email);

		model.addAttribute("ofertas", ofertasService.getOfertasByBuyer(activeUser));
		
		model.addAttribute("email", activeUser.getEmail());
		User user = usersService.getUserByEmail(principal.getName());
		model.addAttribute("activeUser", user);

		return "oferta/listPurchased";
	}

}
