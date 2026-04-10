<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Nouvel utilisateur"/>
</jsp:include>

<h1>➕ Nouvel utilisateur</h1>

<c:if test="${not empty erreur}">
    <div class="alert alert-danger">${erreur}</div>
</c:if>

<div class="form-container">
    <form action="${pageContext.request.contextPath}/admin/utilisateur/creer" method="post" class="form">
        <h2>Informations de connexion</h2>
        <div class="form-row">
            <div class="form-group">
                <label for="nom">Nom *</label>
                <input type="text" id="nom" name="nom" required>
            </div>
            <div class="form-group">
                <label for="prenom">Prénom *</label>
                <input type="text" id="prenom" name="prenom" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="email">Email *</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="motDePasse">Mot de passe *</label>
                <input type="password" id="motDePasse" name="motDePasse" required minlength="4">
            </div>
        </div>

        <div class="form-group">
            <label for="role">Rôle *</label>
            <select id="role" name="role" required>
                <option value="">-- Sélectionner un rôle --</option>
                <c:forEach var="role" items="${roles}">
                    <option value="${role}">${role}</option>
                </c:forEach>
            </select>
        </div>

        <h2>Informations du personnel</h2>
        <div class="form-row">
            <div class="form-group">
                <label for="matricule">Matricule</label>
                <input type="text" id="matricule" name="matricule">
            </div>
            <div class="form-group">
                <label for="fonction">Fonction</label>
                <input type="text" id="fonction" name="fonction">
            </div>
        </div>

        <div class="form-group">
            <label for="departementId">Département</label>
            <select id="departementId" name="departementId">
                <option value="">-- Aucun département --</option>
                <c:forEach var="dep" items="${departements}">
                    <option value="${dep.id}">${dep.nom}</option>
                </c:forEach>
            </select>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Créer l'utilisateur</button>
            <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="btn btn-secondary">Annuler</a>
        </div>
    </form>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
