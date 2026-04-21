package com.sysconge.ejb;

import com.sysconge.entity.*;
import com.sysconge.entity.Approbation.Decision;
import com.sysconge.entity.Approbation.NiveauApprobation;
import com.sysconge.entity.DemandeConge.Statut;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB Stateless - Gestion du workflow d'approbation des demandes de congé.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ApprobationEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    @Inject
    private NotificationEJB notificationEJB;

    @Inject
    private SoldeCongeEJB soldeCongeEJB;

    /**
     * Approuve une demande au niveau chef de département.
     */
    public Approbation approuverParChef(Long demandeId, Utilisateur approbateur, String commentaire) {
        DemandeConge demande = em.find(DemandeConge.class, demandeId);
        if (demande == null || demande.getStatut() != Statut.EN_ATTENTE) {
            throw new IllegalStateException("Demande non valide pour approbation");
        }

        Approbation approbation = new Approbation(demande, approbateur, Decision.APPROUVEE,
                commentaire, NiveauApprobation.CHEF_DEPARTEMENT);
        em.persist(approbation);

        demande.setStatut(Statut.APPROUVEE_CHEF);
        em.merge(demande);

        // Notifier le demandeur
        notificationEJB.creerNotification(
            demande.getDemandeur().getUtilisateur(),
            "Demande approuvée par le chef",
            "Votre demande de congé du " + demande.getDateDebut() + " au " + demande.getDateFin()
                + " a été approuvée par le chef de département. En attente de validation DRH.",
            demande
        );

        // Notifier la DRH
        List<Utilisateur> drhUsers = em.createNamedQuery("Utilisateur.findByRole", Utilisateur.class)
                .setParameter("role", Utilisateur.Role.DRH)
                .getResultList();
        for (Utilisateur drh : drhUsers) {
            notificationEJB.creerNotification(
                drh,
                "Demande à valider",
                "Une demande de congé de " + demande.getDemandeur().getUtilisateur().getNomComplet()
                    + " a été approuvée par le chef et nécessite votre validation.",
                demande
            );
        }

        return approbation;
    }

    /**
     * Refuse une demande au niveau chef de département.
     */
    public Approbation refuserParChef(Long demandeId, Utilisateur approbateur, String commentaire) {
        DemandeConge demande = em.find(DemandeConge.class, demandeId);
        if (demande == null || demande.getStatut() != Statut.EN_ATTENTE) {
            throw new IllegalStateException("Demande non valide pour refus");
        }

        Approbation approbation = new Approbation(demande, approbateur, Decision.REFUSEE,
                commentaire, NiveauApprobation.CHEF_DEPARTEMENT);
        em.persist(approbation);

        demande.setStatut(Statut.REFUSEE);
        em.merge(demande);

        // Notifier le demandeur
        notificationEJB.creerNotification(
            demande.getDemandeur().getUtilisateur(),
            "Demande refusée",
            "Votre demande de congé du " + demande.getDateDebut() + " au " + demande.getDateFin()
                + " a été refusée par le chef de département. Motif : " + commentaire,
            demande
        );

        return approbation;
    }

    /**
     * Approuve une demande au niveau DRH (validation finale).
     */
    public Approbation approuverParDRH(Long demandeId, Utilisateur approbateur, String commentaire) {
        DemandeConge demande = em.find(DemandeConge.class, demandeId);
        if (demande == null || demande.getStatut() != Statut.APPROUVEE_CHEF) {
            throw new IllegalStateException("Demande non valide pour validation DRH");
        }

        Approbation approbation = new Approbation(demande, approbateur, Decision.APPROUVEE,
                commentaire, NiveauApprobation.DRH);
        em.persist(approbation);

        demande.setStatut(Statut.APPROUVEE_DRH);
        em.merge(demande);

        // Décrémenter le solde de congé
        soldeCongeEJB.decrementerSolde(
            demande.getDemandeur().getId(),
            demande.getTypeConge().getId(),
            (int) demande.getNbJours()
        );

        // Notifier le demandeur
        notificationEJB.creerNotification(
            demande.getDemandeur().getUtilisateur(),
            "Demande validée",
            "Votre demande de congé du " + demande.getDateDebut() + " au " + demande.getDateFin()
                + " a été validée par la DRH. Bon congé !",
            demande
        );

        return approbation;
    }

    /**
     * Refuse une demande au niveau DRH.
     */
    public Approbation refuserParDRH(Long demandeId, Utilisateur approbateur, String commentaire) {
        DemandeConge demande = em.find(DemandeConge.class, demandeId);
        if (demande == null || demande.getStatut() != Statut.APPROUVEE_CHEF) {
            throw new IllegalStateException("Demande non valide pour refus DRH");
        }

        Approbation approbation = new Approbation(demande, approbateur, Decision.REFUSEE,
                commentaire, NiveauApprobation.DRH);
        em.persist(approbation);

        demande.setStatut(Statut.REFUSEE);
        em.merge(demande);

        // Notifier le demandeur
        notificationEJB.creerNotification(
            demande.getDemandeur().getUtilisateur(),
            "Demande refusée par la DRH",
            "Votre demande de congé du " + demande.getDateDebut() + " au " + demande.getDateFin()
                + " a été refusée par la DRH. Motif : " + commentaire,
            demande
        );

        return approbation;
    }

    /**
     * Liste les approbations d'une demande.
     */
    public List<Approbation> listerParDemande(Long demandeId) {
        return em.createNamedQuery("Approbation.findByDemande", Approbation.class)
                .setParameter("demandeId", demandeId)
                .getResultList();
    }

    /**
     * Liste les approbations effectuées par un approbateur.
     */
    public List<Approbation> listerParApprobateur(Long utilisateurId) {
        return em.createNamedQuery("Approbation.findByApprobateur", Approbation.class)
                .setParameter("utilisateurId", utilisateurId)
                .getResultList();
    }
}
