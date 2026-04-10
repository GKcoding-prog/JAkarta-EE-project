package com.sysconge.util;

import com.sysconge.entity.Utilisateur;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filtre d'authentification - Vérifie que l'utilisateur est connecté
 * avant d'accéder aux pages protégées.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        Utilisateur utilisateur = null;
        if (session != null) {
            utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        }

        if (utilisateur == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }

        // Vérifier les droits d'accès admin
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.contains("/admin/") &&
                utilisateur.getRole() != Utilisateur.Role.ADMIN &&
                utilisateur.getRole() != Utilisateur.Role.DRH) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/dashboard");
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nettoyage
    }
}
