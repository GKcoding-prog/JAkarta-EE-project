package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité Département - Représente un département universitaire.
 */
@Entity
@Table(name = "departement")
@NamedQueries({
    @NamedQuery(name = "Departement.findAll",
                query = "SELECT d FROM Departement d ORDER BY d.nom"),
    @NamedQuery(name = "Departement.findByNom",
                query = "SELECT d FROM Departement d WHERE d.nom = :nom")
})
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    private String nom;

    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Personnel> personnels = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chef_id")
    private Personnel chef;

    // Constructeurs
    public Departement() {}

    public Departement(String nom, String description) {
        this.nom = nom;
        this.description = description;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Personnel> getPersonnels() { return personnels; }
    public void setPersonnels(List<Personnel> personnels) { this.personnels = personnels; }

    public Personnel getChef() { return chef; }
    public void setChef(Personnel chef) { this.chef = chef; }

    @Override
    public String toString() {
        return "Departement{id=" + id + ", nom='" + nom + "'}";
    }
}
