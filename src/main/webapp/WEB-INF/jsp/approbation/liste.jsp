<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Demandes à traiter"/>
</jsp:include>

<h1>
    <c:choose>
        <c:when test="${niveauApprobation == 'CHEF'}">👔 Demandes en attente d'approbation</c:when>
        <c:when test="${niveauApprobation == 'DRH'}">🏢 Demandes à valider (DRH)</c:when>
    </c:choose>
</h1>

<c:if test="${param.success == 'processed'}">
    <div class="alert alert-success">La demande a été traitée avec succès.</div>
</c:if>

<c:if test="${not empty erreur}">
    <div class="alert alert-danger">${erreur}</div>
</c:if>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/approbation/historique" class="btn btn-secondary">📜 Historique des décisions</a>
</div>

<c:choose>
    <c:when test="${not empty demandes}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Demandeur</th>
                    <th>Département</th>
                    <th>Type de congé</th>
                    <th>Dates</th>
                    <th>Nb jours</th>
                    <th>Motif</th>
                    <th>Actions</th>
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
                        <td>
                            <c:choose>
                                <c:when test="${not empty demande.motif}">${demande.motif}</c:when>
                                <c:otherwise><em>-</em></c:otherwise>
                            </c:choose>
                        </td>
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
        <div class="empty-state">
            <p>Aucune demande en attente de traitement.</p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
