package org.formation.labbe.zoo.zoobackend.entites;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.context.properties.bind.Name;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "animal")
@NamedQuery(name = "findAll", query = "SELECT cage FROM CagePOJO as cage")
@NamedQuery(name = "findOne", query = "SELECT cage FROM CagePOJO cage WHERE cage.idAnimal= :id")
public class CagePOJO implements Serializable {
    @Id
    private int idAnimal;
    private int x;
    private int y;
    private String codeAnimal;
    private String nom;
    private int age;
    private double poids;
    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE})
    @JoinColumn(name = "idAnimal", referencedColumnName = "idAnimal", insertable=false, updatable=false)
    private GazellePOJO gaz;

}
