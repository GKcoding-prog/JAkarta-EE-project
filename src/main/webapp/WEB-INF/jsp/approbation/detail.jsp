<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Traiter la demande"/>
</jsp:include>

<h1>🔍 Traiter la demande #${demande.id}</h1>

<div class="detail-card">
    <h2>Informations de la demande</h2>
    <div class="detail-grid">
        <div class="detail-item">
            <label>Demandeur :</label>
            <span>${demande.demandeur.utilisateur.nomComplet}</span>
        </div>
        <div class="detail-item">
            <label>Matricule :</label>
            <span>${demande.demandeur.matricule}</span>
        </div>
        <div class="detail-item">
            <label>Fonction :</label>
            <span>${demande.demandeur.fonction}</span>
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
            <label>Statut actuel :</label>
            <span class="status status-${demande.statut}">${demande.statut.libelle}</span>
        </div>
        <c:if test="${not empty demande.motif}">
        <div class="detail-item detail-full">
            <label>Motif :</label>
            <span>${demande.motif}</span>
        </div>
        </c:if>
    </div>
</div>

<%-- Historique des approbations --%>
<c:if test="${not empty approbations}">
<div class="section">
    <h2>📜 Historique des décisions</h2>
    <table class="data-table">
        <thead>
            <tr>
                <th>Niveau</th>
                <th>Approbateur</th>
                <th>Décision</th>
                <th>Commentaire</th>
                <th>Date</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="approbation" items="${approbations}">
                <tr>
                    <td>${approbation.niveauApprobation.libelle}</td>
                    <td>${approbation.approbateur.nomComplet}</td>
                    <td>
                        <span class="status status-${approbation.decision == 'APPROUVEE' ? 'APPROUVEE_DRH' : 'REFUSEE'}">
                            ${approbation.decision.libelle}
                        </span>
                    </td>
                    <td>${approbation.commentaire}</td>
                    <td>
                        <fmt:parseDate value="${approbation.dateDecision}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                        <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</c:if>

<%-- Formulaire de décision --%>
<c:if test="${(demande.statut == 'EN_ATTENTE' && sessionScope.utilisateur.role == 'CHEF_DEPARTEMENT') ||
              (demande.statut == 'APPROUVEE_CHEF' && sessionScope.utilisateur.role == 'DRH')}">
<div class="section">
    <h2>📝 Votre décision</h2>
    <form action="${pageContext.request.contextPath}/approbation/traiter" method="post" class="form">
        <input type="hidden" name="demandeId" value="${demande.id}">

        <div class="form-group">
            <label for="commentaire">Commentaire</label>
            <textarea id="commentaire" name="commentaire" rows="3"
                      placeholder="Ajoutez un commentaire (optionnel pour l'approbation, recommandé pour le refus)..."></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" name="action" value="approuver" class="btn btn-success"
                    onclick="return confirm('Confirmer l\'approbation de cette demande ?')">
                ✅ Approuver
            </button>
            <button type="submit" name="action" value="refuser" class="btn btn-danger"
                    onclick="return confirm('Confirmer le refus de cette demande ?')">
                ❌ Refuser
            </button>
        </div>
    </form>
</div>
</c:if>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/approbation/liste" class="btn btn-secondary">← Retour à la liste</a>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
