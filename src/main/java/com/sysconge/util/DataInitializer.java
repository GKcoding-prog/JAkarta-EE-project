package com.sysconge.util;

import com.sysconge.entity.*;
import com.sysconge.entity.Utilisateur.Role;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;

/**
 * Singleton EJB - Initialise les données de démonstration au démarrage de l'application.
 */
@Singleton
@Startup
public class DataInitializer {

    @PersistenceContext(unitName = "SysCongePU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        // Vérifier si les données existent déjà
        Long count = em.createQuery("SELECT COUNT(u) FROM Utilisateur u", Long.class).getSingleResult();
        if (count > 0) {
            return;
        }

        // Créer les départements
        Departement depInformatique = new Departement("Informatique", "Département d'informatique et sciences du numérique");
        Departement depMaths = new Departement("Mathématiques", "Département de mathématiques appliquées");
        Departement depPhysique = new Departement("Physique", "Département de physique fondamentale et appliquée");
        em.persist(depInformatique);
        em.persist(depMaths);
        em.persist(depPhysique);

        // Créer les types de congé
        TypeConge congeAnnuel = new TypeConge("Congé annuel", "Congé annuel payé", 30);
        TypeConge congeMaladie = new TypeConge("Congé maladie", "Congé pour raison médicale", 15);
        congeMaladie.setNecessiteJustificatif(true);
        TypeConge congeMaternite = new TypeConge("Congé maternité", "Congé maternité légal", 98);
        TypeConge congeExceptionnel = new TypeConge("Congé exceptionnel", "Congé pour événement familial (mariage, décès, naissance)", 5);
        TypeConge congeSansSolde = new TypeConge("Congé sans solde", "Congé non rémunéré", 60);
        em.persist(congeAnnuel);
        em.persist(congeMaladie);
        em.persist(congeMaternite);
        em.persist(congeExceptionnel);
        em.persist(congeSansSolde);

        // Créer l'administrateur
        Utilisateur admin = new Utilisateur("Admin", "Système", "admin@univ.ma", "admin123", Role.ADMIN);
        em.persist(admin);

        // Créer la DRH
        Utilisateur drhUser = new Utilisateur("Benali", "Fatima", "drh@univ.ma", "drh123", Role.DRH);
        em.persist(drhUser);
        Personnel drhPersonnel = new Personnel(drhUser, "DRH001", "Directrice RH", LocalDate.of(2010, 1, 15));
        em.persist(drhPersonnel);

        // Créer le chef du département informatique
        Utilisateur chefInfo = new Utilisateur("Alami", "Mohammed", "chef.info@univ.ma", "chef123", Role.CHEF_DEPARTEMENT);
        em.persist(chefInfo);
        Personnel chefInfoPersonnel = new Personnel(chefInfo, "PERS001", "Professeur", LocalDate.of(2012, 9, 1));
        chefInfoPersonnel.setDepartement(depInformatique);
        em.persist(chefInfoPersonnel);
        depInformatique.setChef(chefInfoPersonnel);
        em.merge(depInformatique);

        // Créer des membres du personnel
        Utilisateur pers1 = new Utilisateur("Idrissi", "Ahmed", "ahmed.idrissi@univ.ma", "pers123", Role.PERSONNEL);
        em.persist(pers1);
        Personnel personnel1 = new Personnel(pers1, "PERS002", "Maître de conférences", LocalDate.of(2015, 9, 1));
        personnel1.setDepartement(depInformatique);
        em.persist(personnel1);

        Utilisateur pers2 = new Utilisateur("Tazi", "Khadija", "khadija.tazi@univ.ma", "pers123", Role.PERSONNEL);
        em.persist(pers2);
        Personnel personnel2 = new Personnel(pers2, "PERS003", "Assistante", LocalDate.of(2018, 3, 15));
        personnel2.setDepartement(depInformatique);
        em.persist(personnel2);

        Utilisateur pers3 = new Utilisateur("Fassi", "Omar", "omar.fassi@univ.ma", "pers123", Role.PERSONNEL);
        em.persist(pers3);
        Personnel personnel3 = new Personnel(pers3, "PERS004", "Professeur", LocalDate.of(2014, 9, 1));
        personnel3.setDepartement(depMaths);
        em.persist(personnel3);

        // Créer les soldes de congé pour l'année courante
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

        em.persist(new SoldeConge(chefInfoPersonnel, congeAnnuel, annee, 30));
        em.persist(new SoldeConge(chefInfoPersonnel, congeMaladie, annee, 15));

        em.persist(new SoldeConge(drhPersonnel, congeAnnuel, annee, 30));
        em.persist(new SoldeConge(drhPersonnel, congeMaladie, annee, 15));
    }
}
