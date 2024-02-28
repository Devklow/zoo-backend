package org.formation.labbe.zoo.zoobackend.utils;

import org.formation.labbe.zoo.modele.Animal;
import org.formation.labbe.zoo.utils.Trace;
import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;
import org.formation.labbe.zoo.modele.Cage;
import org.formation.labbe.zoo.modele.technique.CagePleineException;
import org.formation.labbe.zoo.modele.technique.PorteException;

import java.lang.reflect.InvocationTargetException;

public class Conversion {

    public static Cage toCage(CagePOJO cp){
        Cage ret = null;
        ret = new Cage(cp.getX(), cp.getY());
        ret.setX(cp.getX());
        ret.setY(cp.getY());
        Animal bete = null;
        if (cp.getCodeAnimal() == null){
            return ret;
        }

        try {
            String classname = String.join(".",Animal.class.getPackage().getName(),cp.getCodeAnimal());
            Class<?> classe = Class.forName(classname);
            Class<?>[] params;
            Object[] attributes;
            if (cp.getGaz() != null){
                params = new Class[4];
                attributes = new Object[4];
                params[3] = int.class;
                attributes[3] = cp.getGaz().getLgCornes();
            } else {
                params = new Class[3];
                attributes = new Object[3];
            }
            params[0] = String.class;params[1] = int.class;params[2] = double.class;
            attributes[0] = cp.getNom();attributes[1] = cp.getAge();attributes[2] = cp.getPoids();
            bete = (Animal)classe.getConstructor(params).newInstance(attributes);
        } catch (ClassNotFoundException | IllegalAccessException | InvocationTargetException | InstantiationException |
                 NoSuchMethodException e) {
            Trace.erreur(e.getMessage());
        }

        try {
            ret.ouvrir();
            ret.entrer(bete);
            ret.fermer();
        } catch (CagePleineException | PorteException e) {
            Trace.erreur(e.getMessage());
        }
        Trace.info(ret.toString());
        return ret;
    }

}
