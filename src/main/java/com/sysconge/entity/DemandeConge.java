package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité DemandeConge - Représente une demande de congé soumise par un personnel.
 */
@Entity
@Table(name = "demande_conge")
@NamedQueries({
    @NamedQuery(name = "DemandeConge.findByDemandeur",
                query = "SELECT d FROM DemandeConge d WHERE d.demandeur.id = :personnelId ORDER BY d.dateCreation DESC"),
    @NamedQuery(name = "DemandeConge.findByStatut",
                query = "SELECT d FROM DemandeConge d WHERE d.statut = :statut ORDER BY d.dateCreation DESC"),
    @NamedQuery(name = "DemandeConge.findByDepartement",
                query = "SELECT d FROM DemandeConge d WHERE d.demandeur.departement.id = :departementId ORDER BY d.dateCreation DESC"),
    @NamedQuery(name = "DemandeConge.findEnAttente",
                query = "SELECT d FROM DemandeConge d WHERE d.statut = com.sysconge.entity.DemandeConge.Statut.EN_ATTENTE ORDER BY d.dateCreation ASC"),
    @NamedQuery(name = "DemandeConge.findAll",
                query = "SELECT d FROM DemandeConge d ORDER BY d.dateCreation DESC"),
    @NamedQuery(name = "DemandeConge.findEnAttenteByDepartement",
                query = "SELECT d FROM DemandeConge d WHERE d.statut = com.sysconge.entity.DemandeConge.Statut.EN_ATTENTE AND d.demandeur.departement.id = :departementId ORDER BY d.dateCreation ASC"),
    @NamedQuery(name = "DemandeConge.findApprouveeChefByDepartement",
                query = "SELECT d FROM DemandeConge d WHERE d.statut = com.sysconge.entity.DemandeConge.Statut.APPROUVEE_CHEF AND d.demandeur.departement.id = :departementId ORDER BY d.dateCreation ASC"),
    @NamedQuery(name = "DemandeConge.findApprouveeChef",
                query = "SELECT d FROM DemandeConge d WHERE d.statut = com.sysconge.entity.DemandeConge.Statut.APPROUVEE_CHEF ORDER BY d.dateCreation ASC")
})
public class DemandeConge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "demandeur_id", nullable = false)
    private Personnel demandeur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_conge_id", nullable = false)
    private TypeConge typeConge;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(length = 1000)
    private String motif;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Statut statut = Statut.EN_ATTENTE;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @OneToMany(mappedBy = "demande", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Approbation> approbations = new ArrayList<>();

    public enum Statut {
        EN_ATTENTE("En attente"),
        APPROUVEE_CHEF("Approuvée par le chef"),
        APPROUVEE_DRH("Approuvée par la DRH"),
        REFUSEE("Refusée"),
        ANNULEE("Annulée");

        private final String libelle;

        Statut(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
        this.dateModification = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dateModification = LocalDateTime.now();
    }

    // Constructeurs
    public DemandeConge() {}

    public DemandeConge(Personnel demandeur, TypeConge typeConge, LocalDate dateDebut, LocalDate dateFin, String motif) {
        this.demandeur = demandeur;
        this.typeConge = typeConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.motif = motif;
    }

    /**
     * Calcule le nombre de jours de congé demandés.
     */
    public long getNbJours() {
        if (dateDebut != null && dateFin != null) {
            return ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
        }
        return 0;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Personnel getDemandeur() { return demandeur; }
    public void setDemandeur(Personnel demandeur) { this.demandeur = demandeur; }

    public TypeConge getTypeConge() { return typeConge; }
    public void setTypeConge(TypeConge typeConge) { this.typeConge = typeConge; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public List<Approbation> getApprobations() { return approbations; }
    public void setApprobations(List<Approbation> approbations) { this.approbations = approbations; }

    @Override
    public String toString() {
        return "DemandeConge{id=" + id + ", statut=" + statut + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + "}";
    }
}
