package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité Approbation - Représente une décision d'approbation/refus sur une demande de congé.
 * Permet de tracer le workflow d'approbation (Chef → DRH).
 */
@Entity
@Table(name = "approbation")
@NamedQueries({
    @NamedQuery(name = "Approbation.findByDemande",
                query = "SELECT a FROM Approbation a WHERE a.demande.id = :demandeId ORDER BY a.dateDecision DESC"),
    @NamedQuery(name = "Approbation.findByApprobateur",
                query = "SELECT a FROM Approbation a WHERE a.approbateur.id = :utilisateurId ORDER BY a.dateDecision DESC")
})
public class Approbation implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identifiant unique de l'approbation */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Demande de congé concernée */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "demande_id", nullable = false)
    private DemandeConge demande;

    /** Utilisateur qui approuve ou refuse */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "approbateur_id", nullable = false)
    private Utilisateur approbateur;

    /** Décision prise (APPROUVEE ou REFUSEE) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Decision decision;

    /** Commentaire facultatif */
    @Column(length = 500)
    private String commentaire;

    /** Date de la décision */
    @Column(name = "date_decision", nullable = false)
    private LocalDateTime dateDecision;

    /** Niveau d'approbation (Chef ou DRH) */
    @Enumerated(EnumType.STRING)
    @Column(name = "niveau_approbation", nullable = false, length = 20)
    private NiveauApprobation niveauApprobation;

    // --- ENUMS ---
    public enum Decision {
        APPROUVEE("Approuvée"),
        REFUSEE("Refusée");

        private final String libelle;
        Decision(String libelle) { this.libelle = libelle; }
        public String getLibelle() { return libelle; }
    }

    public enum NiveauApprobation {
        CHEF_DEPARTEMENT("Chef de département"),
        DRH("Direction des Ressources Humaines");

        private final String libelle;
        NiveauApprobation(String libelle) { this.libelle = libelle; }
        public String getLibelle() { return libelle; }
    }

    // --- Hooks JPA ---
    @PrePersist
    public void prePersist() {
        this.dateDecision = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.dateDecision = LocalDateTime.now();
    }

    // --- Constructeurs ---
    public Approbation() {}

    public Approbation(DemandeConge demande, Utilisateur approbateur, Decision decision,
                       String commentaire, NiveauApprobation niveauApprobation) {
        if (demande == null || approbateur == null || decision == null || niveauApprobation == null) {
            throw new IllegalArgumentException("Les champs obligatoires ne peuvent pas être nuls");
        }
        this.demande = demande;
        this.approbateur = approbateur;
        this.decision = decision;
        this.commentaire = commentaire;
        this.niveauApprobation = niveauApprobation;
        this.dateDecision = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public DemandeConge getDemande() { return demande; }
    public void setDemande(DemandeConge demande) { this.demande = demande; }

    public Utilisateur getApprobateur() { return approbateur; }
    public void setApprobateur(Utilisateur approbateur) { this.approbateur = approbateur; }

    public Decision getDecision() { return decision; }
    public void setDecision(Decision decision) { this.decision = decision; }

    public String getCommentaire() { return commentaire; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    public LocalDateTime getDateDecision() { return dateDecision; }
    public void setDateDecision(LocalDateTime dateDecision) { this.dateDecision = dateDecision; }

    public NiveauApprobation getNiveauApprobation() { return niveauApprobation; }
    public void setNiveauApprobation(NiveauApprobation niveauApprobation) { this.niveauApprobation = niveauApprobation; }

    @Override
    public String toString() {
        return "Approbation{id=" + id +
               ", decision=" + (decision != null ? decision.getLibelle() : "N/A") +
               ", niveau=" + (niveauApprobation != null ? niveauApprobation.getLibelle() : "N/A") +
               ", date=" + dateDecision + "}";
    }
}

