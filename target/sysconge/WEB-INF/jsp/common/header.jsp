<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${param.titre} - SysCongé</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <c:if test="${not empty sessionScope.utilisateur}">
    <nav class="navbar">
        <div class="nav-brand">
            <a href="${pageContext.request.contextPath}/dashboard">🏫 SysCongé</a>
        </div>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/dashboard">Tableau de bord</a>

            <c:if test="${sessionScope.utilisateur.role == 'PERSONNEL' || sessionScope.utilisateur.role == 'CHEF_DEPARTEMENT' || sessionScope.utilisateur.role == 'DRH'}">
                <a href="${pageContext.request.contextPath}/demande/liste">Mes demandes</a>
                <a href="${pageContext.request.contextPath}/demande/nouvelle">Nouvelle demande</a>
            </c:if>

            <c:if test="${sessionScope.utilisateur.role == 'CHEF_DEPARTEMENT' || sessionScope.utilisateur.role == 'DRH'}">
                <a href="${pageContext.request.contextPath}/approbation/liste">Approbations</a>
            </c:if>

            <c:if test="${sessionScope.utilisateur.role == 'ADMIN' || sessionScope.utilisateur.role == 'DRH'}">
                <a href="${pageContext.request.contextPath}/admin/utilisateurs">Administration</a>
            </c:if>

            <a href="${pageContext.request.contextPath}/notification/liste">
                Notifications
                <c:if test="${nbNotifications > 0}">
                    <span class="badge">${nbNotifications}</span>
                </c:if>
            </a>
        </div>
        <div class="nav-user">
            <span>${sessionScope.utilisateur.nomComplet} (${sessionScope.utilisateur.role})</span>
            <a href="${pageContext.request.contextPath}/profil" class="btn btn-sm">Profil</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-danger">Déconnexion</a>
        </div>
    </nav>
    </c:if>

    <main class="container">
