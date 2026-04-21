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

    /** Identifiant unique du type de congé */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Libellé du type de congé (ex: Congé annuel) */
    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    /** Description facultative */
    @Column(length = 500)
    private String description;

    /** Nombre maximum de jours autorisés pour ce type de congé */
    @Column(name = "nb_jours_max", nullable = false)
    private int nbJoursMax;

    /** Indique si le type de congé est actif */
    @Column(nullable = false)
    private boolean actif = true;

    /** Indique si un justificatif est nécessaire */
    @Column(name = "necessite_justificatif", nullable = false)
    private boolean necessiteJustificatif = false;

    /** Liste des demandes associées à ce type de congé */
    @OneToMany(mappedBy = "typeConge", fetch = FetchType.LAZY)
    private List<DemandeConge> demandes = new ArrayList<>();

    /** Liste des soldes associés à ce type de congé */
    @OneToMany(mappedBy = "typeConge", fetch = FetchType.LAZY)
    private List<SoldeConge> soldes = new ArrayList<>();

    // --- Constructeurs ---
    public TypeConge() {}

    public TypeConge(String libelle, String description, int nbJoursMax) {
        if (libelle == null || libelle.isBlank()) {
            throw new IllegalArgumentException("Le libellé du type de congé ne peut pas être vide");
        }
        if (nbJoursMax < 0) {
            throw new IllegalArgumentException("Le nombre de jours maximum doit être positif");
        }
        this.libelle = libelle;
        this.description = description;
        this.nbJoursMax = nbJoursMax;
        this.actif = true;
        this.necessiteJustificatif = false;
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { 
        if (libelle == null || libelle.isBlank()) {
            throw new IllegalArgumentException("Le libellé du type de congé ne peut pas être vide");
        }
        this.libelle = libelle; 
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getNbJoursMax() { return nbJoursMax; }
    public void setNbJoursMax(int nbJoursMax) { 
        if (nbJoursMax < 0) {
            throw new IllegalArgumentException("Le nombre de jours maximum doit être positif");
        }
        this.nbJoursMax = nbJoursMax; 
    }

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
        return "TypeConge{id=" + id +
               ", libelle='" + libelle + '\'' +
               ", nbJoursMax=" + nbJoursMax +
               ", actif=" + actif +
               ", necessiteJustificatif=" + necessiteJustificatif +
               "}";
    }
}
