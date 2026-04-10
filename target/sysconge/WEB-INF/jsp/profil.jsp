<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Mon profil"/>
</jsp:include>

<h1>👤 Mon profil</h1>

<c:if test="${not empty erreur}">
    <div class="alert alert-danger">${erreur}</div>
</c:if>
<c:if test="${not empty success}">
    <div class="alert alert-success">${success}</div>
</c:if>

<div class="detail-card">
    <h2>Informations personnelles</h2>
    <div class="detail-grid">
        <div class="detail-item">
            <label>Nom complet :</label>
            <span>${utilisateur.nomComplet}</span>
        </div>
        <div class="detail-item">
            <label>Email :</label>
            <span>${utilisateur.email}</span>
        </div>
        <div class="detail-item">
            <label>Rôle :</label>
            <span class="role-badge role-${utilisateur.role}">${utilisateur.role}</span>
        </div>
        <c:if test="${not empty personnel}">
            <div class="detail-item">
                <label>Matricule :</label>
                <span>${personnel.matricule}</span>
            </div>
            <div class="detail-item">
                <label>Fonction :</label>
                <span>${personnel.fonction}</span>
            </div>
            <div class="detail-item">
                <label>Département :</label>
                <span>${personnel.departement.nom}</span>
            </div>
            <div class="detail-item">
                <label>Date d'embauche :</label>
                <span>${personnel.dateEmbauche}</span>
            </div>
        </c:if>
    </div>
</div>

<%-- Soldes de congé --%>
<c:if test="${not empty soldes}">
<div class="section">
    <h2>📊 Mes soldes de congé</h2>
    <table class="data-table">
        <thead>
            <tr>
                <th>Type de congé</th>
                <th>Jours acquis</th>
                <th>Jours pris</th>
                <th>Solde restant</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="solde" items="${soldes}">
                <tr>
                    <td>${solde.typeConge.libelle}</td>
                    <td>${solde.joursAcquis}</td>
                    <td>${solde.joursPris}</td>
                    <td><strong>${solde.soldeRestant}</strong></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</c:if>

<%-- Changement de mot de passe --%>
<div class="section">
    <h2>🔒 Changer le mot de passe</h2>
    <div class="form-container">
        <form action="${pageContext.request.contextPath}/profil" method="post" class="form">
            <div class="form-group">
                <label for="ancienMotDePasse">Ancien mot de passe</label>
                <input type="password" id="ancienMotDePasse" name="ancienMotDePasse" required>
            </div>
            <div class="form-group">
                <label for="nouveauMotDePasse">Nouveau mot de passe</label>
                <input type="password" id="nouveauMotDePasse" name="nouveauMotDePasse" required minlength="4">
            </div>
            <div class="form-group">
                <label for="confirmMotDePasse">Confirmer le mot de passe</label>
                <input type="password" id="confirmMotDePasse" name="confirmMotDePasse" required minlength="4">
            </div>
            <button type="submit" class="btn btn-primary">Changer le mot de passe</button>
        </form>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
