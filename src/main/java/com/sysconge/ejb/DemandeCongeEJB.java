package com.sysconge.ejb;

import com.sysconge.entity.*;
import com.sysconge.entity.DemandeConge.Statut;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * EJB Stateless - Gestion des demandes de congé.
 */
@Stateless
public class DemandeCongeEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    @Inject
    private NotificationEJB notificationEJB;

    /**
     * Crée une nouvelle demande de congé.
     */
    public DemandeConge creerDemande(Personnel demandeur, Long typeCongeId,
                                     LocalDate dateDebut, LocalDate dateFin, String motif) {
        TypeConge typeConge = em.find(TypeConge.class, typeCongeId);
        if (typeConge == null) {
            throw new IllegalArgumentException("Type de congé invalide");
        }

        DemandeConge demande = new DemandeConge(demandeur, typeConge, dateDebut, dateFin, motif);
        em.persist(demande);

        // Notifier le chef de département
        if (demandeur.getDepartement() != null && demandeur.getDepartement().getChef() != null) {
            notificationEJB.creerNotification(
                demandeur.getDepartement().getChef().getUtilisateur(),
                "Nouvelle demande de congé",
                "Une nouvelle demande de congé a été soumise par " + demandeur.getUtilisateur().getNomComplet(),
                demande
            );
        }

        return demande;
    }

    /**
     * Trouve une demande par ID.
     */
    public DemandeConge trouverParId(Long id) {
        return em.find(DemandeConge.class, id);
    }

    /**
     * Liste les demandes d'un personnel.
     */
    public List<DemandeConge> listerParDemandeur(Long personnelId) {
        return em.createNamedQuery("DemandeConge.findByDemandeur", DemandeConge.class)
                .setParameter("personnelId", personnelId)
                .getResultList();
    }

    /**
     * Liste les demandes en attente pour un département (pour le chef).
     */
    public List<DemandeConge> listerEnAttenteParDepartement(Long departementId) {
        return em.createNamedQuery("DemandeConge.findEnAttenteByDepartement", DemandeConge.class)
                .setParameter("departementId", departementId)
                .getResultList();
    }

    /**
     * Liste les demandes approuvées par le chef (pour la DRH).
     */
    public List<DemandeConge> listerApprouveesChef() {
        return em.createNamedQuery("DemandeConge.findApprouveeChef", DemandeConge.class)
                .getResultList();
    }

    /**
     * Liste toutes les demandes.
     */
    public List<DemandeConge> listerToutes() {
        return em.createNamedQuery("DemandeConge.findAll", DemandeConge.class)
                .getResultList();
    }

    /**
     * Liste les demandes par statut.
     */
    public List<DemandeConge> listerParStatut(Statut statut) {
        return em.createNamedQuery("DemandeConge.findByStatut", DemandeConge.class)
                .setParameter("statut", statut)
                .getResultList();
    }

    /**
     * Annule une demande de congé (par le demandeur).
     */
    public void annulerDemande(Long demandeId, Long personnelId) {
        DemandeConge demande = em.find(DemandeConge.class, demandeId);
        if (demande != null && demande.getDemandeur().getId().equals(personnelId)
                && demande.getStatut() == Statut.EN_ATTENTE) {
            demande.setStatut(Statut.ANNULEE);
            em.merge(demande);
        }
    }

    /**
     * Met à jour une demande.
     */
    public DemandeConge mettreAJour(DemandeConge demande) {
        return em.merge(demande);
    }

    /**
     * Compte les demandes par statut.
     */
    public long compterParStatut(Statut statut) {
        return em.createQuery(
                "SELECT COUNT(d) FROM DemandeConge d WHERE d.statut = :statut", Long.class)
                .setParameter("statut", statut)
                .getSingleResult();
    }

    /**
     * Compte toutes les demandes.
     */
    public long compterToutes() {
        return em.createQuery("SELECT COUNT(d) FROM DemandeConge d", Long.class)
                .getSingleResult();
    }

    /**
     * Liste les types de congé actifs.
     */
    public List<TypeConge> listerTypesCongeActifs() {
        return em.createNamedQuery("TypeConge.findActifs", TypeConge.class).getResultList();
    }

    /**
     * Liste tous les types de congé.
     */
    public List<TypeConge> listerTousTypesConge() {
        return em.createNamedQuery("TypeConge.findAll", TypeConge.class).getResultList();
    }

    /**
     * Crée un type de congé.
     */
    public TypeConge creerTypeConge(TypeConge typeConge) {
        em.persist(typeConge);
        return typeConge;
    }

    /**
     * Trouve un type de congé par ID.
     */
    public TypeConge trouverTypeCongeParId(Long id) {
        return em.find(TypeConge.class, id);
    }

    /**
     * Met à jour un type de congé.
     */
    public TypeConge mettreAJourTypeConge(TypeConge typeConge) {
        return em.merge(typeConge);
    }
}
