package com.sysconge.servlet;

import com.sysconge.ejb.SoldeCongeEJB;
import com.sysconge.ejb.UtilisateurEJB;
import com.sysconge.entity.Personnel;
import com.sysconge.entity.SoldeConge;
import com.sysconge.entity.Utilisateur;
import jakarta.ejb.EJB;
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

    private static final long serialVersionUID = 1L;

    @EJB private UtilisateurEJB utilisateurEJB;
    @EJB private SoldeCongeEJB soldeCongeEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;
        Personnel personnel = (session != null) ? (Personnel) session.getAttribute("personnelConnecte") : null;

        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

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

        HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;

        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String ancienMotDePasse = request.getParameter("ancienMotDePasse");
        String nouveauMotDePasse = request.getParameter("nouveauMotDePasse");
        String confirmMotDePasse = request.getParameter("confirmMotDePasse");

        if (ancienMotDePasse != null && !ancienMotDePasse.isBlank()) {
            if (!utilisateur.getMotDePasse().equals(ancienMotDePasse)) {
                request.setAttribute("erreur", "L'ancien mot de passe est incorrect.");
            } else if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 4) {
                request.setAttribute("erreur", "Le nouveau mot de passe doit contenir au moins 4 caractères.");
            } else if (!nouveauMotDePasse.equals(confirmMotDePasse)) {
                request.setAttribute("erreur", "Les mots de passe ne correspondent pas.");
            } else {
                try {
                    utilisateur.setMotDePasse(nouveauMotDePasse);
                    utilisateurEJB.mettreAJour(utilisateur);
                    session.setAttribute("utilisateurConnecte", utilisateur);
                    request.setAttribute("success", "Mot de passe modifié avec succès.");
                } catch (Exception e) {
                    request.setAttribute("erreur", "Erreur lors de la mise à jour du mot de passe.");
                }
            }
        }

        doGet(request, response);
    }
}
