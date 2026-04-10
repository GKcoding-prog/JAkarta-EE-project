<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Historique des décisions"/>
</jsp:include>

<h1>📜 Historique de mes décisions</h1>

<c:choose>
    <c:when test="${not empty approbations}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>Demande #</th>
                    <th>Demandeur</th>
                    <th>Type de congé</th>
                    <th>Dates</th>
                    <th>Décision</th>
                    <th>Commentaire</th>
                    <th>Date décision</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="approbation" items="${approbations}">
                    <tr>
                        <td>${approbation.demande.id}</td>
                        <td>${approbation.demande.demandeur.utilisateur.nomComplet}</td>
                        <td>${approbation.demande.typeConge.libelle}</td>
                        <td>${approbation.demande.dateDebut} → ${approbation.demande.dateFin}</td>
                        <td>
                            <span class="status status-${approbation.decision == 'APPROUVEE' ? 'APPROUVEE_DRH' : 'REFUSEE'}">
                                ${approbation.decision.libelle}
                            </span>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty approbation.commentaire}">${approbation.commentaire}</c:when>
                                <c:otherwise><em>-</em></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:parseDate value="${approbation.dateDecision}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="empty-state">
            <p>Aucune décision enregistrée.</p>
        </div>
    </c:otherwise>
</c:choose>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/approbation/liste" class="btn btn-secondary">← Retour aux demandes</a>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
