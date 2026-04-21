package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité Utilisateur - Représente un utilisateur du système.
 * Classe de base pour l'authentification et les rôles.
 */
@Entity
@Table(name = "utilisateur")
@NamedQueries({
    @NamedQuery(name = "Utilisateur.findByEmail",
                query = "SELECT u FROM Utilisateur u WHERE u.email = :email"),
    @NamedQuery(name = "Utilisateur.findAll",
                query = "SELECT u FROM Utilisateur u ORDER BY u.nom, u.prenom"),
    @NamedQuery(name = "Utilisateur.findByRole",
                query = "SELECT u FROM Utilisateur u WHERE u.role = :role")
})
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identifiant unique de l'utilisateur */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de famille */
    @Column(nullable = false, length = 100)
    private String nom;

    /** Prénom */
    @Column(nullable = false, length = 100)
    private String prenom;

    /** Adresse email (unique et obligatoire) */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Mot de passe (hashé recommandé) */
    @Column(nullable = false, length = 255)
    private String motDePasse;

    /** Rôle de l'utilisateur */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /** Indique si l'utilisateur est actif */
    @Column(nullable = false)
    private boolean actif = true;

    /** Date de création du compte */
    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    // --- ENUM Role ---
    public enum Role {
        PERSONNEL,
        CHEF_DEPARTEMENT,
        DRH,
        ADMIN
    }

    // --- Hooks JPA ---
    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    // --- Constructeurs ---
    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String email, String motDePasse, Role role) {
        if (nom == null || prenom == null || email == null || motDePasse == null || role == null) {
            throw new IllegalArgumentException("Les champs obligatoires ne peuvent pas être nuls");
        }
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
        this.actif = true;
        this.dateCreation = LocalDateTime.now();
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { 
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        this.nom = nom; 
    }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { 
        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        this.prenom = prenom; 
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { 
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        this.email = email; 
    }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { 
        if (motDePasse == null || motDePasse.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }
        this.motDePasse = motDePasse; 
    }

    public Role getRole() { return role; }
    public void setRole(Role role) { 
        if (role == null) {
            throw new IllegalArgumentException("Le rôle ne peut pas être nul");
        }
        this.role = role; 
    }

    public boolean isActif() { return actif; }
    public void setActif(boolean actif) { this.actif = actif; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    /** Retourne le nom complet de l'utilisateur */
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return "Utilisateur{id=" + id +
               ", nom='" + nom + '\'' +
               ", prenom='" + prenom + '\'' +
               ", email='" + email + '\'' +
               ", role=" + role +
               ", actif=" + actif +
               ", dateCreation=" + dateCreation +
               "}";
    }
}
