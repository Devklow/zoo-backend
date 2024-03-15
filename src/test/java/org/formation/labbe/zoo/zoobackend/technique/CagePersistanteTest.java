package org.formation.labbe.zoo.zoobackend.technique;

import org.formation.labbe.zoo.modele.Animal;
import org.formation.labbe.zoo.modele.Lion;
import org.formation.labbe.zoo.modele.technique.CagePleineException;
import org.formation.labbe.zoo.modele.technique.PorteException;
import org.formation.labbe.zoo.zoobackend.services.CageService;
import org.formation.labbe.zoo.zoobackend.services.CageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CagePersistanteTest {

    @Autowired
    private CageService service;

    @Test
    void getCageInfo() {
        String pancarte = "je suis un(e) Lion je m'appelle clarence j'ai 10 an(s) et je pèse 170.4 kg";
        String image = "images/lion.gif";
        CagePersistante c = new CagePersistante(1, service);
        assertEquals(image, c.getCageInfo().image());
        assertEquals(pancarte, c.getCageInfo().pancarte());
        assertEquals(1, c.getCageInfo().id());
        assertEquals(800, c.getCageInfo().x());
        assertEquals(400, c.getCageInfo().y());
    }

    @Test
    void estVide() {
        CagePersistante c = new CagePersistante(2, service);
        assertFalse(c.estVide());
        c = new CagePersistante(1, service);
        assertFalse(c.estVide());
    }

    @Test
    void contientProie() {
        CagePersistante c = new CagePersistante(2, service);
        assertTrue(c.contientProie());
        c = new CagePersistante(3, service);
        assertFalse(c.estVide());
    }

    @Test
    void ouvrir() {
        CagePersistante c = new CagePersistante(2, service);
        c.ouvrir();
        assertTrue(c.isOuvert());
    }

    @Test
    void fermer() {
        CagePersistante c = new CagePersistante(2, service);
        c.fermer();
        assertFalse(c.isOuvert());
    }

    @Test
    void isOuvert() {
        CagePersistante c = new CagePersistante(2, service);
        c.fermer();
        assertFalse(c.isOuvert());
        c.ouvrir();
        assertTrue(c.isOuvert());
    }

    @Test
    void getIdAnimal() {
        CagePersistante c = new CagePersistante(2, service);
        assertEquals(2, c.getIdAnimal());
    }

    @Test
    void nourrir() {
        CagePersistante c = new CagePersistante(2, service);
        Lion l = new Lion();
        c.ouvrir();
        Animal old;
        try {
            old = c.sortir();
            c.entrer(l, 99);
        } catch (PorteException e) {
            throw new RuntimeException(e);
        } catch (CagePleineException e) {
            throw new RuntimeException(e);
        }
        c.fermer();
        assertEquals("je suis un(e) Lion je m'appelle Leo j'ai 34 an(s) et je pèse 156.0 kg", c.getCageInfo().pancarte());
        c.nourrir();
        assertEquals("je suis un(e) Lion je m'appelle Leo j'ai 34 an(s) et je pèse 157.9 kg", c.getCageInfo().pancarte());
        c.ouvrir();
        try {
            c.sortir();
            c.entrer(old, 2);
            c.fermer();
        } catch (PorteException e) {
            throw new RuntimeException(e);
        } catch (CagePleineException e) {
            throw new RuntimeException(e);
        }

    }
}