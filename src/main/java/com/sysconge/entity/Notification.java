package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entité Notification - Représente une notification envoyée à un utilisateur.
 */
@Entity
@Table(name = "notification")
@NamedQueries({
    @NamedQuery(name = "Notification.findByDestinataire",
                query = "SELECT n FROM Notification n WHERE n.destinataire.id = :utilisateurId ORDER BY n.dateCreation DESC"),
    @NamedQuery(name = "Notification.findNonLues",
                query = "SELECT n FROM Notification n WHERE n.destinataire.id = :utilisateurId AND n.lue = false ORDER BY n.dateCreation DESC"),
    @NamedQuery(name = "Notification.countNonLues",
                query = "SELECT COUNT(n) FROM Notification n WHERE n.destinataire.id = :utilisateurId AND n.lue = false")
})
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Utilisateur destinataire;

    @Column(nullable = false, length = 200)
    private String titre;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private boolean lue = false;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "demande_id")
    private DemandeConge demande;

    @PrePersist
    public void prePersist() {
        this.dateCreation = LocalDateTime.now();
    }

    // Constructeurs
    public Notification() {}

    public Notification(Utilisateur destinataire, String titre, String message) {
        this.destinataire = destinataire;
        this.titre = titre;
        this.message = message;
    }

    public Notification(Utilisateur destinataire, String titre, String message, DemandeConge demande) {
        this.destinataire = destinataire;
        this.titre = titre;
        this.message = message;
        this.demande = demande;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Utilisateur getDestinataire() { return destinataire; }
    public void setDestinataire(Utilisateur destinataire) { this.destinataire = destinataire; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isLue() { return lue; }
    public void setLue(boolean lue) { this.lue = lue; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public DemandeConge getDemande() { return demande; }
    public void setDemande(DemandeConge demande) { this.demande = demande; }

    @Override
    public String toString() {
        return "Notification{id=" + id + ", titre='" + titre + "', lue=" + lue + "}";
    }
}
