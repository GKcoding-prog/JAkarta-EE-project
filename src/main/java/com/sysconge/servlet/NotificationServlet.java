package com.sysconge.servlet;

import com.sysconge.ejb.NotificationEJB;
import com.sysconge.entity.Notification;
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
 * Servlet contrôleur pour la gestion des notifications.
 */
@WebServlet(name = "NotificationServlet", urlPatterns = {"/notification/*"})
public class NotificationServlet extends HttpServlet {

    @Inject
    private NotificationEJB notificationEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if (pathInfo == null || "/".equals(pathInfo) || "/liste".equals(pathInfo)) {
            List<Notification> notifications = notificationEJB.listerParDestinataire(utilisateur.getId());
            request.setAttribute("notifications", notifications);
            request.getRequestDispatcher("/WEB-INF/jsp/notification/liste.jsp").forward(request, response);
        } else if ("/marquer-lue".equals(pathInfo)) {
            // Géré par POST
            response.sendRedirect(request.getContextPath() + "/notification/liste");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");

        if ("/marquer-lue".equals(pathInfo)) {
            String notifId = request.getParameter("notificationId");
            if (notifId != null) {
                try {
                    notificationEJB.marquerCommeLue(Long.parseLong(notifId));
                } catch (NumberFormatException e) {
                    // Ignorer
                }
            }
        } else if ("/marquer-toutes-lues".equals(pathInfo)) {
            notificationEJB.marquerToutesCommeLues(utilisateur.getId());
        }

        response.sendRedirect(request.getContextPath() + "/notification/liste");
    }
}
