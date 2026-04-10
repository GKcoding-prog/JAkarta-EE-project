package com.sysconge.servlet;

import com.sysconge.ejb.UtilisateurEJB;
import com.sysconge.entity.Personnel;
import com.sysconge.entity.Utilisateur;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet contrôleur pour l'authentification (login/logout).
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/login", "/logout"})
public class AuthServlet extends HttpServlet {

    @Inject
    private UtilisateurEJB utilisateurEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Vérifier si déjà connecté
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("utilisateur") != null) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");

        if (email == null || email.trim().isEmpty() || motDePasse == null || motDePasse.trim().isEmpty()) {
            request.setAttribute("erreur", "Veuillez remplir tous les champs.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        Utilisateur utilisateur = utilisateurEJB.authentifier(email.trim(), motDePasse);

        if (utilisateur == null) {
            request.setAttribute("erreur", "Email ou mot de passe incorrect.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }

        // Stocker l'utilisateur et le personnel en session
        HttpSession session = request.getSession(true);
        session.setAttribute("utilisateur", utilisateur);

        Personnel personnel = utilisateurEJB.trouverPersonnelParUtilisateur(utilisateur.getId());
        if (personnel != null) {
            session.setAttribute("personnel", personnel);
        }

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
