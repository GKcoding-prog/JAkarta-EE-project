package com.sysconge.entity;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * Entité SoldeConge - Représente le solde de congés d'un personnel pour un type de congé donné.
 */
@Entity
@Table(name = "solde_conge",
       uniqueConstraints = @UniqueConstraint(columnNames = {"personnel_id", "type_conge_id", "annee"}))
@NamedQueries({
    @NamedQuery(name = "SoldeConge.findByPersonnel",
                query = "SELECT s FROM SoldeConge s WHERE s.personnel.id = :personnelId AND s.annee = :annee"),
    @NamedQuery(name = "SoldeConge.findByPersonnelAndType",
                query = "SELECT s FROM SoldeConge s WHERE s.personnel.id = :personnelId AND s.typeConge.id = :typeCongeId AND s.annee = :annee")
})
public class SoldeConge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personnel_id", nullable = false)
    private Personnel personnel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_conge_id", nullable = false)
    private TypeConge typeConge;

    @Column(nullable = false)
    private int annee;

    @Column(name = "jours_acquis", nullable = false)
    private int joursAcquis;

    @Column(name = "jours_pris", nullable = false)
    private int joursPris = 0;

    // Constructeurs
    public SoldeConge() {}

    public SoldeConge(Personnel personnel, TypeConge typeConge, int annee, int joursAcquis) {
        this.personnel = personnel;
        this.typeConge = typeConge;
        this.annee = annee;
        this.joursAcquis = joursAcquis;
    }

    /**
     * Calcule le solde restant.
     */
    public int getSoldeRestant() {
        return joursAcquis - joursPris;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Personnel getPersonnel() { return personnel; }
    public void setPersonnel(Personnel personnel) { this.personnel = personnel; }

    public TypeConge getTypeConge() { return typeConge; }
    public void setTypeConge(TypeConge typeConge) { this.typeConge = typeConge; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public int getJoursAcquis() { return joursAcquis; }
    public void setJoursAcquis(int joursAcquis) { this.joursAcquis = joursAcquis; }

    public int getJoursPris() { return joursPris; }
    public void setJoursPris(int joursPris) { this.joursPris = joursPris; }

    @Override
    public String toString() {
        return "SoldeConge{id=" + id + ", annee=" + annee + ", acquis=" + joursAcquis + ", pris=" + joursPris + "}";
    }
}
