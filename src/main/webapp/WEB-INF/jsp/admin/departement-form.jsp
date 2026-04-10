<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Nouveau département"/>
</jsp:include>

<h1>➕ Nouveau département</h1>

<c:if test="${not empty erreur}">
    <div class="alert alert-danger">${erreur}</div>
</c:if>

<div class="form-container">
    <form action="${pageContext.request.contextPath}/admin/departement/creer" method="post" class="form">
        <div class="form-group">
            <label for="nom">Nom du département *</label>
            <input type="text" id="nom" name="nom" required>
        </div>

        <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="3"></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Créer le département</button>
            <a href="${pageContext.request.contextPath}/admin/departements" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
