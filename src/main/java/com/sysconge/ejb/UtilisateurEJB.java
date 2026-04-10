package com.sysconge.ejb;

import com.sysconge.entity.Personnel;
import com.sysconge.entity.Utilisateur;
import com.sysconge.entity.Utilisateur.Role;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 * EJB Stateless - Gestion des utilisateurs et de l'authentification.
 */
@Stateless
public class UtilisateurEJB {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    /**
     * Authentifie un utilisateur par email et mot de passe.
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        try {
            Utilisateur user = em.createNamedQuery("Utilisateur.findByEmail", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
            if (user.getMotDePasse().equals(motDePasse) && user.isActif()) {
                return user;
            }
        } catch (NoResultException e) {
            // Utilisateur non trouvé
        }
        return null;
    }

    /**
     * Crée un nouvel utilisateur.
     */
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        em.persist(utilisateur);
        return utilisateur;
    }

    /**
     * Met à jour un utilisateur existant.
     */
    public Utilisateur mettreAJour(Utilisateur utilisateur) {
        return em.merge(utilisateur);
    }

    /**
     * Recherche un utilisateur par ID.
     */
    public Utilisateur trouverParId(Long id) {
        return em.find(Utilisateur.class, id);
    }

    /**
     * Recherche un utilisateur par email.
     */
    public Utilisateur trouverParEmail(String email) {
        try {
            return em.createNamedQuery("Utilisateur.findByEmail", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Liste tous les utilisateurs.
     */
    public List<Utilisateur> listerTous() {
        return em.createNamedQuery("Utilisateur.findAll", Utilisateur.class).getResultList();
    }

    /**
     * Liste les utilisateurs par rôle.
     */
    public List<Utilisateur> listerParRole(Role role) {
        return em.createNamedQuery("Utilisateur.findByRole", Utilisateur.class)
                .setParameter("role", role)
                .getResultList();
    }

    /**
     * Trouve le personnel associé à un utilisateur.
     */
    public Personnel trouverPersonnelParUtilisateur(Long utilisateurId) {
        try {
            return em.createNamedQuery("Personnel.findByUtilisateur", Personnel.class)
                    .setParameter("utilisateurId", utilisateurId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Crée un personnel.
     */
    public Personnel creerPersonnel(Personnel personnel) {
        em.persist(personnel);
        return personnel;
    }

    /**
     * Met à jour un personnel.
     */
    public Personnel mettreAJourPersonnel(Personnel personnel) {
        return em.merge(personnel);
    }

    /**
     * Liste tout le personnel.
     */
    public List<Personnel> listerToutPersonnel() {
        return em.createNamedQuery("Personnel.findAll", Personnel.class).getResultList();
    }

    /**
     * Liste le personnel d'un département.
     */
    public List<Personnel> listerPersonnelParDepartement(Long departementId) {
        return em.createNamedQuery("Personnel.findByDepartement", Personnel.class)
                .setParameter("departementId", departementId)
                .getResultList();
    }

    /**
     * Supprime un utilisateur (désactivation logique).
     */
    public void desactiverUtilisateur(Long id) {
        Utilisateur u = em.find(Utilisateur.class, id);
        if (u != null) {
            u.setActif(false);
            em.merge(u);
        }
    }

    /**
     * Compte le nombre total d'utilisateurs actifs.
     */
    public long compterUtilisateursActifs() {
        return em.createQuery("SELECT COUNT(u) FROM Utilisateur u WHERE u.actif = true", Long.class)
                .getSingleResult();
    }
}
