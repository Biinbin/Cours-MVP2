package monprojet.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.extern.slf4j.Slf4j;
import monprojet.dao.CityRepository;
import monprojet.dao.CountryRepository;
import monprojet.entity.City;
import monprojet.entity.Country;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/city") // This means URL's start with /hello (after Application path)
@Slf4j
public class CityControler {
	
	// On affichera par défaut la page 'city.mustache'
	private static final String DEFAULT_VIEW = "city";
        private static final String CREATE_VIEW = "add";
        @Autowired
        public CityRepository cityDAO;
        
        @Autowired
        public CountryRepository countryDAO;
        
	@GetMapping()
	public String sayHello(Model model) {
		model.addAttribute("cities", cityDAO.findAll());
		return DEFAULT_VIEW;
	}
        
        @GetMapping(path = "add") //à l'URL http://localhost:8989/cities/show
	public String montreLesVilles(Model model) {
		log.info("On affiche les villes");
		// On initialise la ville avec des valeurs par défaut
		Country france = countryDAO.findById(1).orElseThrow();
		City nouvelle = new City("Nouvelle ville", france);
		nouvelle.setPopulation(50);
		model.addAttribute("cities", cityDAO.findAll());
		model.addAttribute("city", nouvelle);
		model.addAttribute("countries", countryDAO.findAll());
		return CREATE_VIEW;
	}
        
        @PostMapping(path = "save")
	public String ajouteLaVillePuisAffiche(City city) {
		// cf. https://www.baeldung.com/spring-data-crud-repository-save
		cityDAO.save(city); // Ici on peut avoir une erreur (doublon dans un libellé par exemple)
		return "redirect:/" + DEFAULT_VIEW; // POST-Redirect-GET : on se redirige vers l'affichage de la liste		
	}
        
        @GetMapping(path = "delete/{id}")
	public String supprimeUneCategoriePuisMontreLaListe(@PathVariable int id) {
		cityDAO.deleteById(id); // Ici on peut avoir une erreur (Si il y a des produits dans cette catégorie par exemple)
		return "redirect:/" + DEFAULT_VIEW; // on se redirige vers l'affichage de la liste
	}
        
        @GetMapping(path = "edit/{id}")
	public String montreLeFormulairePourEdition(@PathVariable int id, Model model) {
		model.addAttribute("city", cityDAO.findById(id).get());
                model.addAttribute("countries", countryDAO.findAll());
		return CREATE_VIEW;
	}
}