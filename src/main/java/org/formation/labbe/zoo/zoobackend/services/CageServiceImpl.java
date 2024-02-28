package org.formation.labbe.zoo.zoobackend.services;

import org.formation.labbe.zoo.zoobackend.entites.CagePOJO;
import org.formation.labbe.zoo.zoobackend.repositories.CageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CageServiceImpl implements CageService{

    @Autowired
    private CageRepo repo;
    @Override
    public List<CagePOJO> lireTous() {
        return (List<CagePOJO>) repo.findAll();
    }

    @Override
    public Optional<CagePOJO> lire(int cle) {
        return repo.findById(cle);
    }

    @Override
    public void ajouter(CagePOJO obj) {
        repo.save(obj);
    }

    @Override
    public void modifier(CagePOJO obj) {
        repo.save(obj);
    }

    @Override
    public void supprimer(int cle) {
        repo.deleteById(cle);
    }
}
