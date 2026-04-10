package com.sysconge.servlet;

import com.sysconge.ejb.ApprobationEJB;
import com.sysconge.ejb.DemandeCongeEJB;
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
 * Servlet contrôleur pour l'approbation des demandes de congé.
 * Gère les approbations par le chef de département et la DRH.
 */
@WebServlet(name = "ApprobationServlet", urlPatterns = {"/approbation/*"})
public class ApprobationServlet extends HttpServlet {

    @Inject
    private ApprobationEJB approbationEJB;

    @Inject
    private DemandeCongeEJB demandeCongeEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
        Personnel personnel = (Personnel) session.getAttribute("personnel");

        if (pathInfo == null || "/".equals(pathInfo) || "/liste".equals(pathInfo)) {
            listerDemandesATraiter(request, response, utilisateur, personnel);
        } else if (pathInfo.startsWith("/detail/")) {
            detailDemande(request, response, pathInfo);
        } else if ("/historique".equals(pathInfo)) {
            historique(request, response, utilisateur);
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

        if ("/traiter".equals(pathInfo)) {
            traiterDemande(request, response, utilisateur);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listerDemandesATraiter(HttpServletRequest request, HttpServletResponse response,
                                         Utilisateur utilisateur, Personnel personnel)
            throws ServletException, IOException {

        List<DemandeConge> demandes;

        if (utilisateur.getRole() == Role.CHEF_DEPARTEMENT && personnel != null
                && personnel.getDepartement() != null) {
            demandes = demandeCongeEJB.listerEnAttenteParDepartement(personnel.getDepartement().getId());
            request.setAttribute("niveauApprobation", "CHEF");
        } else if (utilisateur.getRole() == Role.DRH) {
            demandes = demandeCongeEJB.listerApprouveesChef();
            request.setAttribute("niveauApprobation", "DRH");
        } else {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        request.setAttribute("demandes", demandes);
        request.getRequestDispatcher("/WEB-INF/jsp/approbation/liste.jsp").forward(request, response);
    }

    private void detailDemande(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(pathInfo.substring("/detail/".length()));
            DemandeConge demande = demandeCongeEJB.trouverParId(id);
            if (demande == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            List<Approbation> approbations = approbationEJB.listerParDemande(id);
            request.setAttribute("demande", demande);
            request.setAttribute("approbations", approbations);
            request.getRequestDispatcher("/WEB-INF/jsp/approbation/detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void historique(HttpServletRequest request, HttpServletResponse response, Utilisateur utilisateur)
            throws ServletException, IOException {
        List<Approbation> approbations = approbationEJB.listerParApprobateur(utilisateur.getId());
        request.setAttribute("approbations", approbations);
        request.getRequestDispatcher("/WEB-INF/jsp/approbation/historique.jsp").forward(request, response);
    }

    private void traiterDemande(HttpServletRequest request, HttpServletResponse response, Utilisateur utilisateur)
            throws ServletException, IOException {

        String demandeIdStr = request.getParameter("demandeId");
        String action = request.getParameter("action");
        String commentaire = request.getParameter("commentaire");

        if (demandeIdStr == null || action == null) {
            response.sendRedirect(request.getContextPath() + "/approbation/liste");
            return;
        }

        try {
            Long demandeId = Long.parseLong(demandeIdStr);

            if (utilisateur.getRole() == Role.CHEF_DEPARTEMENT) {
                if ("approuver".equals(action)) {
                    approbationEJB.approuverParChef(demandeId, utilisateur, commentaire);
                } else if ("refuser".equals(action)) {
                    approbationEJB.refuserParChef(demandeId, utilisateur, commentaire);
                }
            } else if (utilisateur.getRole() == Role.DRH) {
                if ("approuver".equals(action)) {
                    approbationEJB.approuverParDRH(demandeId, utilisateur, commentaire);
                } else if ("refuser".equals(action)) {
                    approbationEJB.refuserParDRH(demandeId, utilisateur, commentaire);
                }
            }

            response.sendRedirect(request.getContextPath() + "/approbation/liste?success=processed");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/approbation/liste");
        } catch (IllegalStateException e) {
            request.setAttribute("erreur", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/jsp/approbation/liste.jsp").forward(request, response);
        }
    }
}
