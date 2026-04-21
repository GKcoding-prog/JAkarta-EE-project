package com.sysconge.util;

import com.sysconge.entity.Utilisateur;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filtre d'authentification - Vérifie que l'utilisateur est connecté
 * avant d'accéder aux pages protégées.
 */
public class AuthFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("AuthFilter initialisé.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;

        // Vérifier si l'utilisateur est connecté
        if (utilisateur == null) {
            LOGGER.warning("Accès non autorisé à : " + httpRequest.getRequestURI());
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Vérifier les droits d'accès aux pages admin
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/admin/") &&
                utilisateur.getRole() != Utilisateur.Role.ADMIN &&
                utilisateur.getRole() != Utilisateur.Role.DRH) {
            LOGGER.warning("Accès refusé pour l'utilisateur " + utilisateur.getEmail() + " à : " + requestURI);
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            return;
        }

        // Continuer la chaîne de filtres
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("AuthFilter détruit.");
    }
}
