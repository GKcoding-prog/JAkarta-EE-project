package com.sysconge.servlet;

import com.sysconge.ejb.NotificationEJB;
import com.sysconge.entity.Notification;
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
 * Servlet contrôleur pour la gestion des notifications.
 */
@WebServlet(name = "NotificationServlet", urlPatterns = {"/notification/*"})
public class NotificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB
    private NotificationEJB notificationEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;

        if (utilisateur == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo) || "/liste".equals(pathInfo)) {
            List<Notification> notifications = notificationEJB.listerParDestinataire(utilisateur.getId());
            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("/WEB-INF/jsp/notification/liste.jsp").forward(request, response);
        } else if ("/marquer-lue".equals(pathInfo)) {
            // Redirection vers POST
            response.sendRedirect(request.getContextPath() + "/notification/liste");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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

        String pathInfo = request.getPathInfo();
        try {
            if ("/marquer-lue".equals(pathInfo)) {
                String notifId = request.getParameter("notificationId");
                if (notifId != null && !notifId.isBlank()) {
                    try {
                        notificationEJB.marquerCommeLue(Long.parseLong(notifId));
                    } catch (NumberFormatException e) {
                        request.setAttribute("erreur", "Identifiant de notification invalide.");
                    }
                }
            } else if ("/marquer-toutes-lues".equals(pathInfo)) {
                notificationEJB.marquerToutesCommeLues(utilisateur.getId());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } catch (Exception e) {
            throw new ServletException("Erreur lors du traitement des notifications", e);
        }

        response.sendRedirect(request.getContextPath() + "/notification/liste");
    }
}
