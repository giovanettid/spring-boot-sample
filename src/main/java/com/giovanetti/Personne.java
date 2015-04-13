package com.giovanetti;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

@Entity
public class Personne {

    private Personne() {}

    public Personne(String prenom, String nom) {
        this.prenom = prenom;
        this.nom = nom;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @JsonIgnore
    private long id;

    private String prenom;

    @Size(min=2)
    private String nom;

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }
}
