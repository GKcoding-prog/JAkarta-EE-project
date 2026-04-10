package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Personnel - Représente un membre du personnel universitaire.
 * Hérite logiquement de Utilisateur via une relation @OneToOne.
 */
@Entity
@Table(name = "personnel")
@NamedQueries({
    @NamedQuery(name = "Personnel.findByUtilisateur",
                query = "SELECT p FROM Personnel p WHERE p.utilisateur.id = :utilisateurId"),
    @NamedQuery(name = "Personnel.findByDepartement",
                query = "SELECT p FROM Personnel p WHERE p.departement.id = :departementId"),
    @NamedQuery(name = "Personnel.findAll",
                query = "SELECT p FROM Personnel p ORDER BY p.utilisateur.nom")
})
public class Personnel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    @Column(length = 50)
    private String matricule;

    @Column(length = 100)
    private String fonction;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departement_id")
    private Departement departement;

    @OneToMany(mappedBy = "demandeur", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DemandeConge> demandes = new ArrayList<>();

    @OneToMany(mappedBy = "personnel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SoldeConge> soldes = new ArrayList<>();

    // Constructeurs
    public Personnel() {}

    public Personnel(Utilisateur utilisateur, String matricule, String fonction, LocalDate dateEmbauche) {
        this.utilisateur = utilisateur;
        this.matricule = matricule;
        this.fonction = fonction;
        this.dateEmbauche = dateEmbauche;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getFonction() { return fonction; }
    public void setFonction(String fonction) { this.fonction = fonction; }

    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    public Departement getDepartement() { return departement; }
    public void setDepartement(Departement departement) { this.departement = departement; }

    public List<DemandeConge> getDemandes() { return demandes; }
    public void setDemandes(List<DemandeConge> demandes) { this.demandes = demandes; }

    public List<SoldeConge> getSoldes() { return soldes; }
    public void setSoldes(List<SoldeConge> soldes) { this.soldes = soldes; }

    @Override
    public String toString() {
        return "Personnel{id=" + id + ", matricule='" + matricule + "', fonction='" + fonction + "'}";
    }
}
