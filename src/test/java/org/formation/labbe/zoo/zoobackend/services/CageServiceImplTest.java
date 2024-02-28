package org.formation.labbe.zoo.zoobackend.services;

import org.formation.labbe.zoo.utils.Trace;
import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;
import org.formation.labbe.zoo.zoobackend.entites.GazellePOJO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CageServiceImplTest {

    @Autowired
    private CageService service;

    @Test
    void lireTous() {
        List<CagePOJO> liste = service.lireTous();
        assertNotNull(liste);
        liste.forEach(e-> Trace.info(e.toString()));
    }

    @Test
    void AjouterLireModifierSupprimer() {
        List<CagePOJO> liste = service.lireTous();
        assertNotNull(liste);
        CagePOJO obj = new CagePOJO();
        obj.setAge(10);
        obj.setNom("Test");
        obj.setX(100);
        obj.setY(50);
        obj.setPoids(12.5);
        GazellePOJO gaz = new GazellePOJO();
        gaz.setLgCornes(15);
        obj.setGaz(gaz);
        service.ajouter(obj);
        assertFalse(liste.isEmpty());
        List<CagePOJO> listeAp =service.lireTous();
        assertTrue(listeAp.size()>liste.size());
        //Recherche de l'objet ajouté
        List<CagePOJO> find = listeAp.stream().filter(el->el.getX()==100 && el.getY()==50 && el.getAge()==10 && el.getPoids()==12.5 && el.getNom().equals("Test")).toList();
        Trace.info(find.toString());
        obj = find.get(0);
        assertNotNull(obj);
        int id = obj.getIdAnimal();
        obj.setNom("TestModifié");
        service.modifier(obj);
        obj = null;
        if(service.lire(id).isPresent()){
            obj = service.lire(id).get();
        }
        assertNotNull(obj);
        assertEquals("TestModifié", obj.getNom());
        service.supprimer(obj.getIdAnimal());
        assertFalse(service.lire(id).isPresent());
    }
}