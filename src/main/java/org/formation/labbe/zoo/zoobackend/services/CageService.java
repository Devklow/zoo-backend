package org.formation.labbe.zoo.zoobackend.services;

import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;

import java.util.List;
import java.util.Optional;

public interface CageService {
    public List<CagePOJO> lireTous();
    public Optional<CagePOJO> lire(int cle);
    public void ajouter(CagePOJO obj);
    public void modifier(CagePOJO obj);
    public void supprimer(int cle);
}
