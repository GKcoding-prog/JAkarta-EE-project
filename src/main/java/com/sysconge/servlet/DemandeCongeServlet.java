package com.sysconge.servlet;

import com.sysconge.ejb.DemandeCongeEJB;
import com.sysconge.ejb.SoldeCongeEJB;
import com.sysconge.entity.*;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet contrôleur pour la gestion des demandes de congé.
 */
@WebServlet(name = "DemandeCongeServlet", urlPatterns = {"/demande/*"})
public class DemandeCongeServlet extends HttpServlet {

    @Inject
    private DemandeCongeEJB demandeCongeEJB;

    @Inject
    private SoldeCongeEJB soldeCongeEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Personnel personnel = (Personnel) session.getAttribute("personnel");

        if (pathInfo == null || "/".equals(pathInfo) || "/liste".equals(pathInfo)) {
            // Liste des demandes
            listerDemandes(request, response, personnel);
        } else if ("/nouvelle".equals(pathInfo)) {
            // Formulaire de nouvelle demande
            formulaireNouvelleDemande(request, response);
        } else if (pathInfo.startsWith("/detail/")) {
            // Détail d'une demande
            detailDemande(request, response, pathInfo);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        Personnel personnel = (Personnel) session.getAttribute("personnel");

        if ("/nouvelle".equals(pathInfo)) {
            creerDemande(request, response, personnel);
        } else if ("/annuler".equals(pathInfo)) {
            annulerDemande(request, response, personnel);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listerDemandes(HttpServletRequest request, HttpServletResponse response, Personnel personnel)
            throws ServletException, IOException {
        if (personnel != null) {
            List<DemandeConge> demandes = demandeCongeEJB.listerParDemandeur(personnel.getId());
            request.setAttribute("demandes", demandes);
        }
        request.getRequestDispatcher("/WEB-INF/jsp/demande/liste.jsp").forward(request, response);
    }

    private void formulaireNouvelleDemande(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TypeConge> typesConge = demandeCongeEJB.listerTypesCongeActifs();
        request.setAttribute("typesConge", typesConge);
        request.getRequestDispatcher("/WEB-INF/jsp/demande/nouvelle.jsp").forward(request, response);
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
            request.setAttribute("demande", demande);
            request.getRequestDispatcher("/WEB-INF/jsp/demande/detail.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void creerDemande(HttpServletRequest request, HttpServletResponse response, Personnel personnel)
            throws ServletException, IOException {

        String typeCongeIdStr = request.getParameter("typeCongeId");
        String dateDebutStr = request.getParameter("dateDebut");
        String dateFinStr = request.getParameter("dateFin");
        String motif = request.getParameter("motif");

        // Validation
        if (typeCongeIdStr == null || dateDebutStr == null || dateFinStr == null ||
                typeCongeIdStr.trim().isEmpty() || dateDebutStr.trim().isEmpty() || dateFinStr.trim().isEmpty()) {
            request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
            formulaireNouvelleDemande(request, response);
            return;
        }

        try {
            Long typeCongeId = Long.parseLong(typeCongeIdStr);
            LocalDate dateDebut = LocalDate.parse(dateDebutStr);
            LocalDate dateFin = LocalDate.parse(dateFinStr);

            // Vérifications
            if (dateFin.isBefore(dateDebut)) {
                request.setAttribute("erreur", "La date de fin doit être après la date de début.");
                formulaireNouvelleDemande(request, response);
                return;
            }

            if (dateDebut.isBefore(LocalDate.now())) {
                request.setAttribute("erreur", "La date de début ne peut pas être dans le passé.");
                formulaireNouvelleDemande(request, response);
                return;
            }

            // Vérifier le solde
            long nbJours = java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
            if (!soldeCongeEJB.verifierDisponibilite(personnel.getId(), typeCongeId, (int) nbJours)) {
                request.setAttribute("erreur", "Solde de congé insuffisant pour ce type de congé.");
                formulaireNouvelleDemande(request, response);
                return;
            }

            demandeCongeEJB.creerDemande(personnel, typeCongeId, dateDebut, dateFin, motif);
            response.sendRedirect(request.getContextPath() + "/demande/liste?success=created");

        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            request.setAttribute("erreur", "Format de données invalide.");
            formulaireNouvelleDemande(request, response);
        }
    }

    private void annulerDemande(HttpServletRequest request, HttpServletResponse response, Personnel personnel)
            throws ServletException, IOException {
        String demandeIdStr = request.getParameter("demandeId");
        if (demandeIdStr != null) {
            try {
                Long demandeId = Long.parseLong(demandeIdStr);
                demandeCongeEJB.annulerDemande(demandeId, personnel.getId());
            } catch (NumberFormatException e) {
                // Ignorer
            }
        }
        response.sendRedirect(request.getContextPath() + "/demande/liste");
    }
}
