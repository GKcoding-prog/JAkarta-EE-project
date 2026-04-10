package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité TypeConge - Représente un type de congé disponible.
 * Exemples : Congé annuel, Congé maladie, Congé maternité, etc.
 */
@Entity
@Table(name = "type_conge")
@NamedQueries({
    @NamedQuery(name = "TypeConge.findAll",
                query = "SELECT t FROM TypeConge t ORDER BY t.libelle"),
    @NamedQuery(name = "TypeConge.findActifs",
                query = "SELECT t FROM TypeConge t WHERE t.actif = true ORDER BY t.libelle")
})
public class TypeConge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @Column(length = 500)
    private String description;

    @Column(name = "nb_jours_max")
    private int nbJoursMax;

    @Column(nullable = false)
    private boolean actif = true;

    @Column(name = "necessite_justificatif")
    private boolean necessiteJustificatif = false;

    @OneToMany(mappedBy = "typeConge", fetch = FetchType.LAZY)
    private List<DemandeConge> demandes = new ArrayList<>();

    @OneToMany(mappedBy = "typeConge", fetch = FetchType.LAZY)
    private List<SoldeConge> soldes = new ArrayList<>();

    // Constructeurs
    public TypeConge() {}

    public TypeConge(String libelle, String description, int nbJoursMax) {
        this.libelle = libelle;
        this.description = description;
        this.nbJoursMax = nbJoursMax;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getNbJoursMax() { return nbJoursMax; }
    public void setNbJoursMax(int nbJoursMax) { this.nbJoursMax = nbJoursMax; }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public boolean isNecessiteJustificatif() { return necessiteJustificatif; }
    public void setNecessiteJustificatif(boolean necessiteJustificatif) { this.necessiteJustificatif = necessiteJustificatif; }

    public List<DemandeConge> getDemandes() { return demandes; }
    public void setDemandes(List<DemandeConge> demandes) { this.demandes = demandes; }

    public List<SoldeConge> getSoldes() { return soldes; }
    public void setSoldes(List<SoldeConge> soldes) { this.soldes = soldes; }

    @Override
    public String toString() {
        return "TypeConge{id=" + id + ", libelle='" + libelle + "', nbJoursMax=" + nbJoursMax + "}";
    }
}
