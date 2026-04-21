package com.sysconge.ejb;

import com.sysconge.entity.Departement;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB Stateless - Gestion des départements.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DepartementEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    /**
     * Liste tous les départements.
     */
    public List<Departement> listerTous() {
        return em.createNamedQuery("Departement.findAll", Departement.class)
                 .getResultList();
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
        if (departement == null) {
            throw new IllegalArgumentException("Le département ne peut pas être nul");
        }
        em.persist(departement);
        return departement;
    }

    /**
     * Met à jour un département existant.
     */
    public Departement mettreAJour(Departement departement) {
        if (departement == null || departement.getId() == null) {
            throw new IllegalArgumentException("Département invalide pour mise à jour");
        }
        return em.merge(departement);
    }

    /**
     * Supprime un département par ID.
     */
    public void supprimer(Long id) {
        Departement departement = em.find(Departement.class, id);
        if (departement != null) {
            em.remove(departement);
        } else {
            throw new IllegalStateException("Département introuvable pour suppression");
        }
    }

    /**
     * Compte le nombre total de départements.
     */
    public long compterTous() {
        return em.createQuery("SELECT COUNT(d) FROM Departement d", Long.class)
                 .getSingleResult();
    }
}
