package com.sysconge.ejb;

import com.sysconge.entity.SoldeConge;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * EJB Stateless - Gestion des soldes de congé du personnel.
 */
@Stateless
public class SoldeCongeEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    /**
     * Récupère les soldes d'un personnel pour l'année courante.
     */
    public List<SoldeConge> listerSoldes(Long personnelId) {
        int anneeActuelle = LocalDate.now().getYear();
        return em.createNamedQuery("SoldeConge.findByPersonnel", SoldeConge.class)
                .setParameter("personnelId", personnelId)
                .setParameter("annee", anneeActuelle)
                .getResultList();
    }

    /**
     * Récupère le solde pour un personnel, un type de congé et l'année courante.
     */
    public SoldeConge trouverSolde(Long personnelId, Long typeCongeId) {
        int anneeActuelle = LocalDate.now().getYear();
        try {
            return em.createNamedQuery("SoldeConge.findByPersonnelAndType", SoldeConge.class)
                    .setParameter("personnelId", personnelId)
                    .setParameter("typeCongeId", typeCongeId)
                    .setParameter("annee", anneeActuelle)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Décrémente le solde après approbation finale d'une demande.
     */
    public void decrementerSolde(Long personnelId, Long typeCongeId, int nbJours) {
        SoldeConge solde = trouverSolde(personnelId, typeCongeId);
        if (solde != null) {
            solde.setJoursPris(solde.getJoursPris() + nbJours);
            em.merge(solde);
        }
    }

    /**
     * Vérifie si un personnel a suffisamment de jours de congé disponibles.
     */
    public boolean verifierDisponibilite(Long personnelId, Long typeCongeId, int nbJours) {
        SoldeConge solde = trouverSolde(personnelId, typeCongeId);
        if (solde == null) {
            return false;
        }
        return solde.getSoldeRestant() >= nbJours;
    }

    /**
     * Crée ou met à jour un solde de congé.
     */
    public SoldeConge creerOuMettreAJour(SoldeConge soldeConge) {
        SoldeConge existant = trouverSolde(
            soldeConge.getPersonnel().getId(),
            soldeConge.getTypeConge().getId()
        );
        if (existant != null) {
            existant.setJoursAcquis(soldeConge.getJoursAcquis());
            return em.merge(existant);
        } else {
            em.persist(soldeConge);
            return soldeConge;
        }
    }

    /**
     * Crée un solde de congé.
     */
    public SoldeConge creerSolde(SoldeConge solde) {
        em.persist(solde);
        return solde;
    }
}
