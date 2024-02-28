package org.formation.labbe.zoo.zoobackend.entites;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "gazelle")
public class GazellePOJO implements Serializable {
    @Id
    private int id;
    private int idAnimal;
    private int lgCornes;
}
