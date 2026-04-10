<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Connexion - SysCongé</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="login-body">
    <div class="login-container">
        <div class="login-card">
            <div class="login-header">
                <h1>🏫 SysCongé</h1>
                <p>Système de Gestion des Demandes de Congés</p>
                <p class="subtitle">Personnel Universitaire</p>
            </div>

            <c:if test="${not empty erreur}">
                <div class="alert alert-danger">${erreur}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
                <div class="form-group">
                    <label for="email">Adresse email</label>
                    <input type="email" id="email" name="email" value="${email}"
                           placeholder="votre.email@univ.ma" required>
                </div>

                <div class="form-group">
                    <label for="motDePasse">Mot de passe</label>
                    <input type="password" id="motDePasse" name="motDePasse"
                           placeholder="Votre mot de passe" required>
                </div>

                <button type="submit" class="btn btn-primary btn-block">Se connecter</button>
            </form>

            <div class="login-footer">
                <h3>Comptes de démonstration :</h3>
                <table class="demo-accounts">
                    <tr><td><strong>Admin</strong></td><td>admin@univ.ma / admin123</td></tr>
                    <tr><td><strong>DRH</strong></td><td>drh@univ.ma / drh123</td></tr>
                    <tr><td><strong>Chef Dept.</strong></td><td>chef.info@univ.ma / chef123</td></tr>
                    <tr><td><strong>Personnel</strong></td><td>ahmed.idrissi@univ.ma / pers123</td></tr>
                </table>
            </div>
        </div>
    </div>
</body>
</html>
