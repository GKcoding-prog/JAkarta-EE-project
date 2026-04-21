package com.sysconge.ejb;

import com.sysconge.entity.DemandeConge;
import com.sysconge.entity.Notification;
import com.sysconge.entity.Utilisateur;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB Stateless - Gestion des notifications.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class NotificationEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    /**
     * Crée une notification simple.
     */
    public Notification creerNotification(Utilisateur destinataire, String titre, String message) {
        if (destinataire == null) {
            throw new IllegalArgumentException("Le destinataire ne peut pas être nul");
        }
        Notification notification = new Notification(destinataire, titre, message);
        em.persist(notification);
        return notification;
    }

    /**
     * Crée une notification liée à une demande de congé.
     */
    public Notification creerNotification(Utilisateur destinataire, String titre, String message, DemandeConge demande) {
        if (destinataire == null) {
            throw new IllegalArgumentException("Le destinataire ne peut pas être nul");
        }
        Notification notification = new Notification(destinataire, titre, message, demande);
        em.persist(notification);
        return notification;
    }

    /**
     * Liste les notifications d'un utilisateur.
     */
    public List<Notification> listerParDestinataire(Long utilisateurId) {
        return em.createNamedQuery("Notification.findByDestinataire", Notification.class)
                 .setParameter("utilisateurId", utilisateurId)
                 .getResultList();
    }

    /**
     * Liste les notifications non lues d'un utilisateur.
     */
    public List<Notification> listerNonLues(Long utilisateurId) {
        return em.createNamedQuery("Notification.findNonLues", Notification.class)
                 .setParameter("utilisateurId", utilisateurId)
                 .getResultList();
    }

    /**
     * Compte les notifications non lues.
     */
    public long compterNonLues(Long utilisateurId) {
        return em.createNamedQuery("Notification.countNonLues", Long.class)
                 .setParameter("utilisateurId", utilisateurId)
                 .getSingleResult();
    }

    /**
     * Marque une notification comme lue.
     */
    public void marquerCommeLue(Long notificationId) {
        Notification notification = em.find(Notification.class, notificationId);
        if (notification != null) {
            notification.setLue(true);
            em.merge(notification);
        } else {
            throw new IllegalStateException("Notification introuvable pour mise à jour");
        }
    }

    /**
     * Marque toutes les notifications d'un utilisateur comme lues.
     */
    public void marquerToutesCommeLues(Long utilisateurId) {
        em.createQuery("UPDATE Notification n SET n.lue = true WHERE n.destinataire.id = :userId AND n.lue = false")
          .setParameter("userId", utilisateurId)
          .executeUpdate();
    }
}

