<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Nouvelle demande de congé"/>
</jsp:include>

<h1>📝 Nouvelle demande de congé</h1>

<c:if test="${not empty erreur}">
    <div class="alert alert-danger">${erreur}</div>
</c:if>

<div class="form-container">
    <form action="${pageContext.request.contextPath}/demande/nouvelle" method="post" class="form">
        <div class="form-group">
            <label for="typeCongeId">Type de congé *</label>
            <select id="typeCongeId" name="typeCongeId" required>
                <option value="">-- Sélectionner un type de congé --</option>
                <c:forEach var="type" items="${typesConge}">
                    <option value="${type.id}">${type.libelle} (max: ${type.nbJoursMax} jours)</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="dateDebut">Date de début *</label>
                <input type="date" id="dateDebut" name="dateDebut" required>
            </div>

            <div class="form-group">
                <label for="dateFin">Date de fin *</label>
                <input type="date" id="dateFin" name="dateFin" required>
            </div>
        </div>

        <div class="form-group">
            <label for="motif">Motif / Commentaire</label>
            <textarea id="motif" name="motif" rows="4"
                      placeholder="Décrivez le motif de votre demande de congé..."></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Soumettre la demande</button>
            <a href="${pageContext.request.contextPath}/demande/liste" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
