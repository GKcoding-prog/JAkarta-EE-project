package com.sysconge.servlet;

import com.sysconge.ejb.SoldeCongeEJB;
import com.sysconge.ejb.UtilisateurEJB;
import com.sysconge.entity.Personnel;
import com.sysconge.entity.SoldeConge;
import com.sysconge.entity.Utilisateur;
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
 * Servlet contrôleur pour la gestion du profil utilisateur.
 */
@WebServlet(name = "ProfilServlet", urlPatterns = {"/profil"})
public class ProfilServlet extends HttpServlet {

    @Inject
    private UtilisateurEJB utilisateurEJB;

    @Inject
    private SoldeCongeEJB soldeCongeEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Personnel personnel = (Personnel) session.getAttribute("personnel");

        request.setAttribute("utilisateur", utilisateur);
        request.setAttribute("personnel", personnel);

        if (personnel != null) {
            List<SoldeConge> soldes = soldeCongeEJB.listerSoldes(personnel.getId());
            request.setAttribute("soldes", soldes);
        }

        request.getRequestDispatcher("/WEB-INF/jsp/profil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        String ancienMotDePasse = request.getParameter("ancienMotDePasse");
        String nouveauMotDePasse = request.getParameter("nouveauMotDePasse");
        String confirmMotDePasse = request.getParameter("confirmMotDePasse");

        if (ancienMotDePasse != null && !ancienMotDePasse.isEmpty()) {
            if (!utilisateur.getMotDePasse().equals(ancienMotDePasse)) {
                request.setAttribute("erreur", "L'ancien mot de passe est incorrect.");
            } else if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 4) {
                request.setAttribute("erreur", "Le nouveau mot de passe doit contenir au moins 4 caractères.");
            } else if (!nouveauMotDePasse.equals(confirmMotDePasse)) {
                request.setAttribute("erreur", "Les mots de passe ne correspondent pas.");
            } else {
                utilisateur.setMotDePasse(nouveauMotDePasse);
                utilisateurEJB.mettreAJour(utilisateur);
                session.setAttribute("utilisateur", utilisateur);
                request.setAttribute("success", "Mot de passe modifié avec succès.");
            }
        }

        doGet(request, response);
    }
}
