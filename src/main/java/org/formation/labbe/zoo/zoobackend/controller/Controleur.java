package org.formation.labbe.zoo.zoobackend.controller;

import jakarta.annotation.PostConstruct;
import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;
import org.formation.labbe.zoo.zoobackend.entites.GazellePOJO;
import org.formation.labbe.zoo.modele.*;
import org.formation.labbe.zoo.modele.technique.BeurkException;
import org.formation.labbe.zoo.zoobackend.services.CageService;
import org.formation.labbe.zoo.zoobackend.services.CageServiceImpl;
import org.formation.labbe.zoo.zoobackend.technique.CagePersistante;
import org.formation.labbe.zoo.modele.technique.CagePleineException;
import org.formation.labbe.zoo.modele.technique.PorteException;
import org.formation.labbe.zoo.utils.Balai;
import org.formation.labbe.zoo.zoobackend.utils.Conversion;
import org.formation.labbe.zoo.utils.Trace;
import org.formation.labbe.zoo.dto.CageInfos;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class Controleur {
	private HashMap<Integer, CagePersistante> lesCages;
	private List<Visiteur> lesVisiteurs;
	@Autowired
	private CageService service;

	private static Controleur instance;

	@PostConstruct
	public void init(){
		lesCages = new HashMap<>();
		lesVisiteurs=new ArrayList<>();
		lireCages();
	}

	public static Controleur getInstance(){
		if(instance == null){
			instance = new Controleur();
		}
		return instance;
	}



	/**
	 * Permet de nourrir tous les animaux du zoo
	 */
	public void nourrir ()
	{
		lesCages.forEach((key,el)->{
			el.nourrir();
		});
		//DAOFactory.getInstance().getDAO().ajouter(lesCages);
	}

	/**
	 *
	 * @param mangeur indice de l'animal mangeur (sa cage)
	 * @param mange indice de la cage de la proie
	 * @return le texte sur ce qu'il s'est passé
	 *
	 */
	public String devorer(int mangeur, int mange,boolean visiteur)
	{
		Proie laBeteConvoitee = null;
		String s = "INCOMPATIBLE";
		if (!visiteur){
			if (!lesCages.get(mange).estVide() && !lesCages.get(mangeur).estVide() && lesCages.get(mange).contientProie()) {
				lesCages.get(mange).ouvrir();
				int id = lesCages.get(mange).getId();
				try {
					laBeteConvoitee = (Proie) lesCages.get(mange).sortir();
				} catch (PorteException e2) {
					s = e2.getMessage();
				}
				try {
					s = lesCages.get(mangeur).manger(laBeteConvoitee);
				} catch (BeurkException e) {
					s = e.getMessage();
					try {
						lesCages.get(mange).entrer((Animal) laBeteConvoitee, id);
					} catch (PorteException e1) {
						s = e1.getMessage();
					} catch (CagePleineException e1) {
						s = e1.getMessage();
					}
					lesCages.get(mange).fermer();
				}
			}
		}else{
			if (lesVisiteurs.get(mange)!= null && !lesCages.get(mangeur).estVide()) {
				try {
					s = lesCages.get(mangeur).manger(lesVisiteurs.get(mange));
					lesVisiteurs.remove(mange);
					Balai.balayer();
				} catch (BeurkException e) {
					s = e.getMessage();
				}
			}

		}
		//DAOFactory.getInstance().getDAO().ajouter(lesCages);
		return s;
	}

	public List<String> getInfos() {
		ArrayList<String> ret = new ArrayList<>();
        lesCages.forEach((key, el)-> {
            ret.add(el.toString());
        });
		return ret;

	}

	public void entrer(){
		if (Visiteur.getNb() < Visiteur.MAX){
			lesVisiteurs.add(new Visiteur());
		}
	}

	public void sortir(){
		if (Visiteur.getNb() > 0){
			lesVisiteurs.remove(Visiteur.getNb()-1);
			Balai.balayer();
		}
	}

	public int getNbVisiteurs(){
		return Visiteur.getNb();
	}

	@GetMapping("/")
	public List<CageInfos> index(){
		ArrayList<CageInfos> ret = new ArrayList<>();
		lesCages.forEach((key, el)-> {
			ret.add(el.getCageInfo());
		});
		return ret;
	}

    public void ajouterCage(int x, int y) {
		CagePOJO c = new CagePOJO();
		c.setX(x);
		c.setY(y);
		service.ajouter(c);
		lireCages();
    }

	private void lireCages() {
		lesCages.clear();
		service.lireTous().forEach(
				el -> lesCages.putIfAbsent(el.getIdAnimal(),
						new CagePersistante(el.getIdAnimal(), service)));
	}

	public List<String> getTypesAnimal(){
		ArrayList<String> ret = new ArrayList<>();
		Reflections reflections = new Reflections("org.formation.zoo.modele",new SubTypesScanner(false));
		reflections.getSubTypesOf(Animal.class).forEach(el->{
			ret.add(el.getSimpleName());
		});
		return ret;
	}

	public void entrerAnimal(String animal, int id, HashMap<String, String> params) {
		CagePersistante laCage = this.lesCages.get(id);
		CagePOJO pojo = new CagePOJO();
		pojo.setCodeAnimal(animal);
		pojo.setIdAnimal(id);
		if(params.get("Nom")!=null && !params.get("Nom").isEmpty()){
			pojo.setNom(params.get("Nom"));
		}
		if(params.get("Age")!=null && !params.get("Age").isEmpty()){
			pojo.setAge(Integer.parseInt(params.get("Age")));
		}
		if(params.get("Poids")!=null && !params.get("Poids").isEmpty()){
			pojo.setPoids(Double.parseDouble(params.get("Poids")));
		}
		if(pojo.getCodeAnimal().equals("Gazelle")){
			GazellePOJO gaz = new GazellePOJO();
			gaz.setIdAnimal(id);
			if(params.get("lgCornes")!=null && !params.get("lgCornes").isEmpty()){
				gaz.setLgCornes(Integer.parseInt((params.get("lgCornes"))));
			}
			pojo.setGaz(gaz);
		}
		Cage tmp = Conversion.toCage(pojo);
		tmp.ouvrir();
		try{
			laCage.ouvrir();
			laCage.entrer(tmp.sortir(), id);
			laCage.fermer();
		} catch (CagePleineException | PorteException e) {
			Trace.erreur(e.getMessage());
        }
    }
}
