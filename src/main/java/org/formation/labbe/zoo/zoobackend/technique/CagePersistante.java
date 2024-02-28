package org.formation.labbe.zoo.zoobackend.technique;

import org.formation.labbe.zoo.dto.CageInfos;
import org.formation.labbe.zoo.modele.Proie;
import org.formation.labbe.zoo.modele.technique.BeurkException;
import org.formation.labbe.zoo.modele.technique.CagePleineException;
import org.formation.labbe.zoo.modele.technique.PorteException;
import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;
import org.formation.labbe.zoo.zoobackend.entites.GazellePOJO;
import org.formation.labbe.zoo.modele.Animal;
import org.formation.labbe.zoo.modele.Cage;
import org.formation.labbe.zoo.modele.Gazelle;
import org.formation.labbe.zoo.zoobackend.services.CageService;
import org.formation.labbe.zoo.zoobackend.utils.Conversion;

import java.util.Optional;


public class CagePersistante {
    private Cage laCage;

    private CagePOJO lePojo;
    private CageService dao;
    private int cle;

    public CagePersistante(int cle, CageService dao) {
        this.dao = dao;
        this.cle = cle;
        this.lePojo = null;
        Optional<CagePOJO> res = dao.lire(cle);
        res.ifPresent(cagePOJO -> this.lePojo = cagePOJO);
        this.laCage = Conversion.toCage(lePojo);
    }

    public void nourrir(){
        if(!estVide()){
            laCage.getOccupant().manger();
            lePojo.setPoids(laCage.getOccupant().getPoids());
            sauvegarder();
        }
    }
    public String manger(Proie m) throws BeurkException {
        if(!estVide()){
            String ret = laCage.getOccupant().manger(m);
            lePojo.setPoids(laCage.getOccupant().getPoids());
            sauvegarder();
            return ret;
        }
        return null;
    }

    public boolean estVide(){
        return this.laCage.getOccupant() == null;
    }

    public boolean contientProie(){
        boolean ret = false;
        if(!this.estVide()){
            ret = this.laCage.getOccupant() instanceof Proie;
        }
        return ret;
    }

    public void entrer(Animal a, int id) throws PorteException, CagePleineException {
        laCage.entrer(a);
        updatePOJO(getCodeAnimal(a), a.getNom(), a.getAge(), a.getPoids());
        if (a instanceof Gazelle){
            GazellePOJO gaz = new GazellePOJO(id, getIdAnimal(), ((Gazelle) a).getLgCornes());
            lePojo.setGaz(gaz);
        }
        sauvegarder();
    }

    private void sauvegarder() {
        dao.modifier(lePojo);
    }

    public Animal sortir() throws PorteException {
        Animal ret = laCage.sortir();
        updatePOJO(null, null
        , 0, 0);
        lePojo.setGaz(null);
        sauvegarder();
        return ret;
    }


    public void ouvrir() {
        laCage.ouvrir();
    }

    public void fermer() {
        laCage.fermer();
    }

    public boolean isOuvert() {
        return this.laCage.isOuvert();
    }

    @Override
    public String toString() {
        return laCage.toString();
    }

    public int getIdAnimal(){
        return lePojo.getIdAnimal();
    }

    public int getId(){
        if (lePojo.getGaz() != null){
            return lePojo.getGaz().getId();
        }
        return -1;
    }

    private void updatePOJO(String codeAnimal, String nom, int age, double poids){
        lePojo.setAge(age);
        lePojo.setPoids(poids);
        lePojo.setCodeAnimal(codeAnimal);
        lePojo.setNom(nom);
    }

    private String getCodeAnimal(Animal a){
        return a.getClass().getSimpleName();
    }

    public CageInfos getCageInfo() {
        String pancarte = "Cage vide";
        String image = "images/CageVide.png";
        if(laCage.getOccupant() != null){
            pancarte = laCage.getOccupant().getInfos();
            image = String.join("","images/",lePojo.getCodeAnimal().toLowerCase(), ".gif");
        }
        return new CageInfos(pancarte, image, lePojo.getX(), lePojo.getY(), lePojo.getIdAnimal(), this.estVide());
    }
}
