<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Erreur serveur"/>
</jsp:include>

<div class="error-page">
    <h1>500 - Erreur interne du serveur</h1>
    <p>Une erreur inattendue s'est produite. Veuillez réessayer plus tard.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Retour au tableau de bord</a>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
