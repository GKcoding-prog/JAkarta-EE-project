<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Toutes les demandes"/>
</jsp:include>

<h1>📋 Toutes les demandes de congé</h1>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="btn btn-secondary">Utilisateurs</a>
    <a href="${pageContext.request.contextPath}/admin/departements" class="btn btn-secondary">Départements</a>
    <a href="${pageContext.request.contextPath}/admin/types-conge" class="btn btn-secondary">Types de congé</a>
</div>

<c:choose>
    <c:when test="${not empty demandes}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Demandeur</th>
                    <th>Département</th>
                    <th>Type</th>
                    <th>Dates</th>
                    <th>Jours</th>
                    <th>Statut</th>
                    <th>Date demande</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="demande" items="${demandes}">
                    <tr>
                        <td>${demande.id}</td>
                        <td>${demande.demandeur.utilisateur.nomComplet}</td>
                        <td>${demande.demandeur.departement.nom}</td>
                        <td>${demande.typeConge.libelle}</td>
                        <td>${demande.dateDebut} → ${demande.dateFin}</td>
                        <td>${demande.nbJours}</td>
                        <td><span class="status status-${demande.statut}">${demande.statut.libelle}</span></td>
                        <td>
                            <fmt:parseDate value="${demande.dateCreation}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="empty-state">
            <p>Aucune demande enregistrée.</p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
