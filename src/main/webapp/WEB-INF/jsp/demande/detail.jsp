<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Détail de la demande"/>
</jsp:include>

<h1>📄 Détail de la demande #${demande.id}</h1>

<div class="detail-card">
    <div class="detail-grid">
        <div class="detail-item">
            <label>Demandeur :</label>
            <span>${demande.demandeur.utilisateur.nomComplet}</span>
        </div>
        <div class="detail-item">
            <label>Département :</label>
            <span>${demande.demandeur.departement.nom}</span>
        </div>
        <div class="detail-item">
            <label>Type de congé :</label>
            <span>${demande.typeConge.libelle}</span>
        </div>
        <div class="detail-item">
            <label>Date de début :</label>
            <span>${demande.dateDebut}</span>
        </div>
        <div class="detail-item">
            <label>Date de fin :</label>
            <span>${demande.dateFin}</span>
        </div>
        <div class="detail-item">
            <label>Nombre de jours :</label>
            <span><strong>${demande.nbJours} jour(s)</strong></span>
        </div>
        <div class="detail-item">
            <label>Statut :</label>
            <span class="status status-${demande.statut}">${demande.statut.libelle}</span>
        </div>
        <div class="detail-item">
            <label>Date de création :</label>
            <span>
                <fmt:parseDate value="${demande.dateCreation}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
            </span>
        </div>
        <c:if test="${not empty demande.motif}">
        <div class="detail-item detail-full">
            <label>Motif :</label>
            <span>${demande.motif}</span>
        </div>
        </c:if>
    </div>
</div>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/demande/liste" class="btn btn-secondary">← Retour à la liste</a>
    <c:if test="${demande.statut == 'EN_ATTENTE' && demande.demandeur.utilisateur.id == sessionScope.utilisateur.id}">
        <form action="${pageContext.request.contextPath}/demande/annuler" method="post" style="display:inline;">
            <input type="hidden" name="demandeId" value="${demande.id}">
            <button type="submit" class="btn btn-danger"
                    onclick="return confirm('Êtes-vous sûr de vouloir annuler cette demande ?')">
                Annuler la demande
            </button>
        </form>
    </c:if>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
