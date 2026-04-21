package com.sysconge.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filtre pour forcer l'encodage UTF-8 sur toutes les requêtes et réponses.
 */
@WebFilter("/*") // Applique le filtre à toutes les requêtes
public class EncodingFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger(EncodingFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("EncodingFilter initialisé. Toutes les requêtes seront en UTF-8.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Forcer l'encodage UTF-8
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Ne pas écraser le content-type si déjà défini par un servlet/JSP
        if (response.getContentType() == null) {
            response.setContentType("text/html; charset=UTF-8");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOGGER.info("EncodingFilter détruit.");
    }
}

