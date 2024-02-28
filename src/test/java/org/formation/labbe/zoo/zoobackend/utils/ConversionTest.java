package org.formation.labbe.zoo.zoobackend.utils;

import org.formation.labbe.zoo.modele.Cage;
import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;
import org.formation.labbe.zoo.zoobackend.entites.GazellePOJO;
import org.formation.labbe.zoo.modele.Gazelle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConversionTest {

    @Test
    void toCage() {
        CagePOJO tmp = new CagePOJO();
        Cage aRecup = null;
        tmp.setX(90);
        tmp.setY(230);
        tmp.setCodeAnimal("Gazelle");
        tmp.setNom("Lady Gaga");
        tmp.setAge(2);
        tmp.setPoids(25);
        tmp.setIdAnimal(3);
        GazellePOJO tmp2 = new GazellePOJO();
        tmp2.setLgCornes(10);
        tmp2.setIdAnimal(tmp.getIdAnimal());
        tmp2.setId(0);
        tmp.setGaz(tmp2);

        aRecup = Conversion.toCage(tmp);
        assertNotNull(aRecup);
        assertEquals(tmp.getX(), aRecup.getX());
        assertEquals(tmp.getY(), aRecup.getY());
        assertInstanceOf(Gazelle.class, aRecup.getOccupant());
        assertEquals(tmp.getGaz().getLgCornes(), ((Gazelle)aRecup.getOccupant()).getLgCornes());

    }
}