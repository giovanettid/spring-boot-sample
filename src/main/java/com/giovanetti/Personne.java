package com.giovanetti;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "USER")
public class Personne {

    private Personne() {}

    public Personne(String prenom, String nom) {
        this.prenom = prenom;
        this.nom = nom;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private long id;

    @Column(name = "PRENOM")
    private String prenom;

    @Size(min=2)
    @Column(name = "NOM")
    private String nom;

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }
}
