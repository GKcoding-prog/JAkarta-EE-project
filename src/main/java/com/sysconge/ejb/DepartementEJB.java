package com.sysconge.ejb;

import com.sysconge.entity.Departement;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB Stateless - Gestion des départements.
 */
@Stateless
public class DepartementEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    /**
     * Liste tous les départements.
     */
    public List<Departement> listerTous() {
        return em.createNamedQuery("Departement.findAll", Departement.class).getResultList();
    }

    /**
     * Trouve un département par ID.
     */
    public Departement trouverParId(Long id) {
        return em.find(Departement.class, id);
    }

    /**
     * Crée un nouveau département.
     */
    public Departement creer(Departement departement) {
        em.persist(departement);
        return departement;
    }

    /**
     * Met à jour un département.
     */
    public Departement mettreAJour(Departement departement) {
        return em.merge(departement);
    }

    /**
     * Compte le nombre total de départements.
     */
    public long compterTous() {
        return em.createQuery("SELECT COUNT(d) FROM Departement d", Long.class).getSingleResult();
    }
}
