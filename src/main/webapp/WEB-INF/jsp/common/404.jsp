<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Page non trouvée"/>
</jsp:include>

<div class="error-page">
    <h1>404 - Page non trouvée</h1>
    <p>La page que vous recherchez n'existe pas.</p>
    <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-primary">Retour au tableau de bord</a>
</div>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
