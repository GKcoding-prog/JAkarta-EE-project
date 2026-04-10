<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Tableau de bord"/>
</jsp:include>

<h1>Tableau de bord</h1>
<p class="welcome-msg">Bienvenue, <strong>${sessionScope.utilisateur.nomComplet}</strong>
    <span class="role-badge role-${sessionScope.utilisateur.role}">${sessionScope.utilisateur.role}</span>
</p>

<%-- ============ Dashboard Personnel ============ --%>
<c:if test="${sessionScope.utilisateur.role == 'PERSONNEL' || sessionScope.utilisateur.role == 'CHEF_DEPARTEMENT' || sessionScope.utilisateur.role == 'DRH'}">

    <%-- Soldes de congé --%>
    <c:if test="${not empty mesSoldes}">
    <div class="section">
        <h2>📊 Mes soldes de congé</h2>
        <div class="cards-grid">
            <c:forEach var="solde" items="${mesSoldes}">
                <div class="card card-solde">
                    <h3>${solde.typeConge.libelle}</h3>
                    <div class="solde-info">
                        <span class="solde-restant">${solde.soldeRestant}</span>
                        <span class="solde-label">jours restants</span>
                    </div>
                    <div class="solde-detail">
                        <span>Acquis: ${solde.joursAcquis}j</span> |
                        <span>Pris: ${solde.joursPris}j</span>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
    </c:if>

    <%-- Dernières demandes --%>
    <c:if test="${not empty mesDemandes}">
    <div class="section">
        <h2>📋 Mes dernières demandes</h2>
        <table class="data-table">
            <thead>
                <tr>
                    <th>Type</th>
                    <th>Date début</th>
                    <th>Date fin</th>
                    <th>Jours</th>
                    <th>Statut</th>
                    <th>Date demande</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="demande" items="${mesDemandes}" end="4">
                    <tr>
                        <td>${demande.typeConge.libelle}</td>
                        <td>${demande.dateDebut}</td>
                        <td>${demande.dateFin}</td>
                        <td>${demande.nbJours}</td>
                        <td>
                            <span class="status status-${demande.statut}">${demande.statut.libelle}</span>
                        </td>
                        <td><fmt:parseDate value="${demande.dateCreation}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <a href="${pageContext.request.contextPath}/demande/liste" class="btn btn-primary">Voir toutes mes demandes</a>
        <a href="${pageContext.request.contextPath}/demande/nouvelle" class="btn btn-success">Nouvelle demande</a>
    </div>
    </c:if>

    <c:if test="${empty mesDemandes}">
    <div class="section">
        <div class="empty-state">
            <p>Vous n'avez pas encore de demandes de congé.</p>
            <a href="${pageContext.request.contextPath}/demande/nouvelle" class="btn btn-success">Faire une demande</a>
        </div>
    </div>
    </c:if>
</c:if>

<%-- ============ Dashboard Chef de département ============ --%>
<c:if test="${sessionScope.utilisateur.role == 'CHEF_DEPARTEMENT'}">
    <div class="section section-highlight">
        <h2>👔 Demandes à approuver</h2>
        <c:choose>
            <c:when test="${not empty demandesEnAttente}">
                <p class="alert alert-info">${nbDemandesEnAttente} demande(s) en attente de votre approbation.</p>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Demandeur</th>
                            <th>Type</th>
                            <th>Dates</th>
                            <th>Jours</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="demande" items="${demandesEnAttente}">
                            <tr>
                                <td>${demande.demandeur.utilisateur.nomComplet}</td>
                                <td>${demande.typeConge.libelle}</td>
                                <td>${demande.dateDebut} → ${demande.dateFin}</td>
                                <td>${demande.nbJours}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/approbation/detail/${demande.id}"
                                       class="btn btn-sm btn-primary">Traiter</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p class="empty-state">Aucune demande en attente d'approbation.</p>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<%-- ============ Dashboard DRH ============ --%>
<c:if test="${sessionScope.utilisateur.role == 'DRH'}">
    <div class="section section-highlight">
        <h2>🏢 Demandes à valider (DRH)</h2>
        <div class="stats-grid">
            <div class="stat-card">
                <span class="stat-number">${totalDemandes}</span>
                <span class="stat-label">Total demandes</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">${nbDemandesAValider}</span>
                <span class="stat-label">À valider</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">${totalUtilisateurs}</span>
                <span class="stat-label">Utilisateurs actifs</span>
            </div>
        </div>

        <c:if test="${not empty demandesAValider}">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Demandeur</th>
                        <th>Département</th>
                        <th>Type</th>
                        <th>Dates</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="demande" items="${demandesAValider}">
                        <tr>
                            <td>${demande.demandeur.utilisateur.nomComplet}</td>
                            <td>${demande.demandeur.departement.nom}</td>
                            <td>${demande.typeConge.libelle}</td>
                            <td>${demande.dateDebut} → ${demande.dateFin}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/approbation/detail/${demande.id}"
                                   class="btn btn-sm btn-primary">Traiter</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</c:if>

<%-- ============ Dashboard Admin ============ --%>
<c:if test="${sessionScope.utilisateur.role == 'ADMIN'}">
    <div class="section">
        <h2>⚙️ Administration</h2>
        <div class="stats-grid">
            <div class="stat-card">
                <span class="stat-number">${totalDemandes}</span>
                <span class="stat-label">Total demandes</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">${demandesEnAttente}</span>
                <span class="stat-label">En attente</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">${demandesApprouvees}</span>
                <span class="stat-label">Approuvées</span>
            </div>
            <div class="stat-card">
                <span class="stat-number">${totalUtilisateurs}</span>
                <span class="stat-label">Utilisateurs actifs</span>
            </div>
        </div>

        <div class="admin-links">
            <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="btn btn-primary">Gérer les utilisateurs</a>
            <a href="${pageContext.request.contextPath}/admin/departements" class="btn btn-primary">Gérer les départements</a>
            <a href="${pageContext.request.contextPath}/admin/types-conge" class="btn btn-primary">Gérer les types de congé</a>
            <a href="${pageContext.request.contextPath}/admin/demandes" class="btn btn-primary">Voir toutes les demandes</a>
        </div>
    </div>
</c:if>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
