package com.sysconge.servlet;

import com.sysconge.ejb.*;
import com.sysconge.entity.*;
import com.sysconge.entity.Utilisateur.Role;
import jakarta.ejb.EJB;
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
 * Servlet contrôleur pour l'administration du système.
 * Gestion des utilisateurs, départements, types de congé.
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin/*"})
public class AdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @EJB private UtilisateurEJB utilisateurEJB;
    @EJB private DepartementEJB departementEJB;
    @EJB private DemandeCongeEJB demandeCongeEJB;
    @EJB private SoldeCongeEJB soldeCongeEJB;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;

        if (utilisateur == null || utilisateur.getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo)) {
            response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
            return;
        }

        switch (pathInfo) {
            case "/utilisateurs":
                listerUtilisateurs(request, response);
                break;
            case "/utilisateur/nouveau":
                formulaireNouvelUtilisateur(request, response);
                break;
            case "/departements":
                listerDepartements(request, response);
                break;
            case "/departement/nouveau":
                request.getRequestDispatcher("/WEB-INF/jsp/admin/departement-form.jsp").forward(request, response);
                break;
            case "/types-conge":
                listerTypesConge(request, response);
                break;
            case "/type-conge/nouveau":
                request.getRequestDispatcher("/WEB-INF/jsp/admin/type-conge-form.jsp").forward(request, response);
                break;
            case "/demandes":
                listerToutesDemandes(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Utilisateur utilisateur = (session != null) ? (Utilisateur) session.getAttribute("utilisateurConnecte") : null;

        if (utilisateur == null || utilisateur.getRole() != Role.ADMIN) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        switch (pathInfo) {
            case "/utilisateur/creer":
                creerUtilisateur(request, response);
                break;
            case "/departement/creer":
                creerDepartement(request, response);
                break;
            case "/type-conge/creer":
                creerTypeConge(request, response);
                break;
            case "/utilisateur/desactiver":
                desactiverUtilisateur(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void listerUtilisateurs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Utilisateur> utilisateurs = utilisateurEJB.listerTous();
        request.setAttribute("utilisateurs", utilisateurs);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/utilisateurs.jsp").forward(request, response);
    }

    private void formulaireNouvelUtilisateur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Departement> departements = departementEJB.listerTous();
        request.setAttribute("departements", departements);
        request.setAttribute("roles", Role.values());
        request.getRequestDispatcher("/WEB-INF/jsp/admin/utilisateur-form.jsp").forward(request, response);
    }

    private void creerUtilisateur(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String motDePasse = request.getParameter("motDePasse");
        String roleStr = request.getParameter("role");
        String matricule = request.getParameter("matricule");
        String fonction = request.getParameter("fonction");
        String departementIdStr = request.getParameter("departementId");

        if (nom == null || prenom == null || email == null || motDePasse == null || roleStr == null ||
            nom.isBlank() || prenom.isBlank() || email.isBlank() || motDePasse.isBlank()) {
            request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
            formulaireNouvelUtilisateur(request, response);
            return;
        }

        if (utilisateurEJB.trouverParEmail(email.trim()) != null) {
            request.setAttribute("erreur", "Un utilisateur avec cet email existe déjà.");
            formulaireNouvelUtilisateur(request, response);
            return;
        }

        Role role = Role.valueOf(roleStr);
        Utilisateur utilisateur = new Utilisateur(nom.trim(), prenom.trim(), email.trim(), motDePasse, role);
        utilisateurEJB.creerUtilisateur(utilisateur);

        if (role != Role.ADMIN) {
            Personnel personnel = new Personnel();
            personnel.setUtilisateur(utilisateur);
            personnel.setMatricule(matricule);
            personnel.setFonction(fonction);
            personnel.setDateEmbauche(LocalDate.now());

            if (departementIdStr != null && !departementIdStr.isBlank()) {
                Departement dep = departementEJB.trouverParId(Long.parseLong(departementIdStr));
                personnel.setDepartement(dep);
            }

            utilisateurEJB.creerPersonnel(personnel);

            int annee = LocalDate.now().getYear();
            List<TypeConge> types = demandeCongeEJB.listerTousTypesConge();
            for (TypeConge type : types) {
                SoldeConge solde = new SoldeConge(personnel, type, annee, type.getNbJoursMax());
                soldeCongeEJB.creerSolde(solde);
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs?success=created");
    }

    private void listerDepartements(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Departement> departements = departementEJB.listerTous();
        request.setAttribute("departements", departements);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/departements.jsp").forward(request, response);
    }

    private void creerDepartement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nom = request.getParameter("nom");
        String description = request.getParameter("description");

        if (nom == null || nom.isBlank()) {
            request.setAttribute("erreur", "Le nom du département est obligatoire.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/departement-form.jsp").forward(request, response);
            return;
        }

        Departement departement = new Departement(nom.trim(), description);
        departementEJB.creer(departement);
        response.sendRedirect(request.getContextPath() + "/admin/departements?success=created");
    }

    private void listerTypesConge(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<TypeConge> types = demandeCongeEJB.listerTousTypesConge();
        request.setAttribute("typesConge", types);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/types-conge.jsp").forward(request, response);
    }

        private void creerTypeConge(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String libelle = request.getParameter("libelle");
        String description = request.getParameter("description");
        String nbJoursMaxStr = request.getParameter("nbJoursMax");
        String necessiteJustificatif = request.getParameter("necessiteJustificatif");

        if (libelle == null || libelle.isBlank() || nbJoursMaxStr == null || nbJoursMaxStr.isBlank()) {
            request.setAttribute("erreur", "Veuillez remplir tous les champs obligatoires.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/type-conge-form.jsp").forward(request, response);
            return;
        }

        try {
            int nbJoursMax = Integer.parseInt(nbJoursMaxStr);
            TypeConge typeConge = new TypeConge(libelle.trim(), description, nbJoursMax);
            typeConge.setNecessiteJustificatif("on".equals(necessiteJustificatif));
            demandeCongeEJB.creerTypeConge(typeConge);
            response.sendRedirect(request.getContextPath() + "/admin/types-conge?success=created");
        } catch (NumberFormatException e) {
            request.setAttribute("erreur", "Le nombre de jours doit être un entier valide.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/type-conge-form.jsp").forward(request, response);
        }
    }

    private void listerToutesDemandes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<DemandeConge> demandes = demandeCongeEJB.listerToutes();
        request.setAttribute("demandes", demandes);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/demandes.jsp").forward(request, response);
    }

    private void desactiverUtilisateur(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                utilisateurEJB.desactiverUtilisateur(Long.parseLong(idStr));
            } catch (NumberFormatException e) {
                // Ignorer erreur de format
            }
        }
        response.sendRedirect(request.getContextPath() + "/admin/utilisateurs");
    }
}
