package com.sysconge.util;

import com.sysconge.entity.*;
import com.sysconge.entity.Utilisateur.Role;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton EJB - Initialise les données de démonstration au démarrage de l'application.
 * Amélioré avec gestion d'erreurs et logs.
 */
@Singleton
@Startup
public class DataInitializer {

    private static final Logger LOGGER = Logger.getLogger(DataInitializer.class.getName());

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        try {
            LOGGER.info("=== Initialisation des données SysConge ===");

            Long count = em.createQuery("SELECT COUNT(u) FROM Utilisateur u", Long.class).getSingleResult();
            if (count != null && count > 0) {
                LOGGER.info("Données déjà présentes, initialisation ignorée.");
                return;
            }

            // Départements
            Departement depInfo = new Departement("Informatique", "Département d'informatique et sciences du numérique");
            Departement depMaths = new Departement("Mathématiques", "Département de mathématiques appliquées");
            Departement depPhysique = new Departement("Physique", "Département de physique fondamentale et appliquée");
            em.persist(depInfo);
            em.persist(depMaths);
            em.persist(depPhysique);

            // Types de congé
            TypeConge congeAnnuel = new TypeConge("Congé annuel", "Congé annuel payé", 30);
            TypeConge congeMaladie = new TypeConge("Congé maladie", "Congé pour raison médicale", 15);
            congeMaladie.setNecessiteJustificatif(true);
            TypeConge congeMaternite = new TypeConge("Congé maternité", "Congé maternité légal", 98);
            TypeConge congeExceptionnel = new TypeConge("Congé exceptionnel", "Événement familial", 5);
            TypeConge congeSansSolde = new TypeConge("Congé sans solde", "Congé non rémunéré", 60);
            em.persist(congeAnnuel);
            em.persist(congeMaladie);
            em.persist(congeMaternite);
            em.persist(congeExceptionnel);
            em.persist(congeSansSolde);

            // Utilisateurs et personnels
            Utilisateur admin = new Utilisateur("Admin", "Système", "admin@univ.ma", "admin123", Role.ADMIN);
            em.persist(admin);

            Utilisateur drhUser = new Utilisateur("Benali", "Fatima", "drh@univ.ma", "drh123", Role.DRH);
            em.persist(drhUser);
            Personnel drhPersonnel = new Personnel(drhUser, "DRH001", "Directrice RH", LocalDate.of(2010, 1, 15));
            em.persist(drhPersonnel);

            Utilisateur chefInfo = new Utilisateur("Alami", "Mohammed", "chef.info@univ.ma", "chef123", Role.CHEF_DEPARTEMENT);
            em.persist(chefInfo);
            Personnel chefInfoPers = new Personnel(chefInfo, "PERS001", "Professeur", LocalDate.of(2012, 9, 1));
            chefInfoPers.setDepartement(depInfo);
            em.persist(chefInfoPers);
            depInfo.setChef(chefInfoPers);
            em.merge(depInfo);

            // Membres du personnel
            Utilisateur pers1 = new Utilisateur("Idrissi", "Ahmed", "ahmed.idrissi@univ.ma", "pers123", Role.PERSONNEL);
            em.persist(pers1);
            Personnel personnel1 = new Personnel(pers1, "PERS002", "Maître de conférences", LocalDate.of(2015, 9, 1));
            personnel1.setDepartement(depInfo);
            em.persist(personnel1);

            Utilisateur pers2 = new Utilisateur("Tazi", "Khadija", "khadija.tazi@univ.ma", "pers123", Role.PERSONNEL);
            em.persist(pers2);
            Personnel personnel2 = new Personnel(pers2, "PERS003", "Assistante", LocalDate.of(2018, 3, 15));
            personnel2.setDepartement(depInfo);
            em.persist(personnel2);

            Utilisateur pers3 = new Utilisateur("Fassi", "Omar", "omar.fassi@univ.ma", "pers123", Role.PERSONNEL);
            em.persist(pers3);
            Personnel personnel3 = new Personnel(pers3, "PERS004", "Professeur", LocalDate.of(2014, 9, 1));
            personnel3.setDepartement(depMaths);
            em.persist(personnel3);

            // Soldes de congé
            int annee = LocalDate.now().getYear();
            em.persist(new SoldeConge(personnel1, congeAnnuel, annee, 30));
            em.persist(new SoldeConge(personnel1, congeMaladie, annee, 15));
            em.persist(new SoldeConge(personnel1, congeExceptionnel, annee, 5));

            em.persist(new SoldeConge(personnel2, congeAnnuel, annee, 30));
            em.persist(new SoldeConge(personnel2, congeMaladie, annee, 15));
            em.persist(new SoldeConge(personnel2, congeExceptionnel, annee, 5));

            em.persist(new SoldeConge(personnel3, congeAnnuel, annee, 30));
            em.persist(new SoldeConge(personnel3, congeMaladie, annee, 15));
            em.persist(new SoldeConge(personnel3, congeExceptionnel, annee, 5));

            em.persist(new SoldeConge(chefInfoPers, congeAnnuel, annee, 30));
            em.persist(new SoldeConge(chefInfoPers, congeMaladie, annee, 15));

            em.persist(new SoldeConge(drhPersonnel, congeAnnuel, annee, 30));
            em.persist(new SoldeConge(drhPersonnel, congeMaladie, annee, 15));

            LOGGER.info("=== Initialisation terminée avec succès ===");

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'initialisation des données SysConge", e);
        }
    }
}
