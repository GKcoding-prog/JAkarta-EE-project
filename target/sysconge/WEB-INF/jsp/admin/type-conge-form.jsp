<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Nouveau type de congé"/>
</jsp:include>

<h1>➕ Nouveau type de congé</h1>

<c:if test="${not empty erreur}">
    <div class="alert alert-danger">${erreur}</div>
</c:if>

<div class="form-container">
    <form action="${pageContext.request.contextPath}/admin/type-conge/creer" method="post" class="form">
        <div class="form-group">
            <label for="libelle">Libellé *</label>
            <input type="text" id="libelle" name="libelle" required>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="3"></textarea>
        </div>

        <div class="form-group">
            <label for="nbJoursMax">Nombre de jours maximum *</label>
            <input type="number" id="nbJoursMax" name="nbJoursMax" min="1" required>
        </div>

        <div class="form-group">
            <label>
                <input type="checkbox" name="necessiteJustificatif">
                Nécessite un justificatif
            </label>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Créer le type de congé</button>
            <a href="${pageContext.request.contextPath}/admin/types-conge" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
