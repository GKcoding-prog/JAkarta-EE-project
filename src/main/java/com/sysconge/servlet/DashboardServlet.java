package com.sysconge.servlet;

import com.sysconge.ejb.*;
import com.sysconge.entity.*;
import com.sysconge.entity.Utilisateur.Role;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Servlet contrôleur pour le tableau de bord.
 * Affiche des informations différentes selon le rôle de l'utilisateur.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    @Inject
    private DemandeCongeEJB demandeCongeEJB;

    @Inject
    private SoldeCongeEJB soldeCongeEJB;

    @Inject
    private NotificationEJB notificationEJB;

    @Inject
    private UtilisateurEJB utilisateurEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Personnel personnel = (Personnel) session.getAttribute("personnel");

        // Notifications non lues
        long nbNotifications = notificationEJB.compterNonLues(utilisateur.getId());
        request.setAttribute("nbNotifications", nbNotifications);

        switch (utilisateur.getRole()) {
            case PERSONNEL:
                chargerDashboardPersonnel(request, personnel);
                break;
            case CHEF_DEPARTEMENT:
                chargerDashboardChef(request, personnel);
                break;
            case DRH:
                chargerDashboardDRH(request, personnel);
                break;
            case ADMIN:
                chargerDashboardAdmin(request);
                break;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
    }

    private void chargerDashboardPersonnel(HttpServletRequest request, Personnel personnel) {
        if (personnel != null) {
            List<DemandeConge> mesDemandes = demandeCongeEJB.listerParDemandeur(personnel.getId());
            List<SoldeConge> mesSoldes = soldeCongeEJB.listerSoldes(personnel.getId());
            request.setAttribute("mesDemandes", mesDemandes);
            request.setAttribute("mesSoldes", mesSoldes);
        }
    }

    private void chargerDashboardChef(HttpServletRequest request, Personnel personnel) {
        // Afficher aussi les infos personnelles
        chargerDashboardPersonnel(request, personnel);

        if (personnel != null && personnel.getDepartement() != null) {
            List<DemandeConge> demandesEnAttente = demandeCongeEJB
                    .listerEnAttenteParDepartement(personnel.getDepartement().getId());
            request.setAttribute("demandesEnAttente", demandesEnAttente);
            request.setAttribute("nbDemandesEnAttente", demandesEnAttente.size());
        }
    }

    private void chargerDashboardDRH(HttpServletRequest request, Personnel personnel) {
        chargerDashboardPersonnel(request, personnel);

        List<DemandeConge> demandesApprouvees = demandeCongeEJB.listerApprouveesChef();
        request.setAttribute("demandesAValider", demandesApprouvees);
        request.setAttribute("nbDemandesAValider", demandesApprouvees.size());

        // Statistiques
        request.setAttribute("totalDemandes", demandeCongeEJB.compterToutes());
        request.setAttribute("totalUtilisateurs", utilisateurEJB.compterUtilisateursActifs());
    }

    private void chargerDashboardAdmin(HttpServletRequest request) {
        request.setAttribute("totalDemandes", demandeCongeEJB.compterToutes());
        request.setAttribute("totalUtilisateurs", utilisateurEJB.compterUtilisateursActifs());
        request.setAttribute("demandesEnAttente", demandeCongeEJB.compterParStatut(DemandeConge.Statut.EN_ATTENTE));
        request.setAttribute("demandesApprouvees", demandeCongeEJB.compterParStatut(DemandeConge.Statut.APPROUVEE_DRH));
    }
}
