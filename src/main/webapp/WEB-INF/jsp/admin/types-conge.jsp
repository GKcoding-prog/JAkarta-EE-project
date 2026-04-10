<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Gestion des types de congé"/>
</jsp:include>

<h1>📑 Gestion des types de congé</h1>

<c:if test="${param.success == 'created'}">
    <div class="alert alert-success">Type de congé créé avec succès.</div>
</c:if>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/admin/type-conge/nouveau" class="btn btn-success">+ Nouveau type de congé</a>
    <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="btn btn-secondary">Utilisateurs</a>
</div>

<c:choose>
    <c:when test="${not empty typesConge}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Libellé</th>
                    <th>Description</th>
                    <th>Jours max</th>
                    <th>Justificatif</th>
                    <th>Actif</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="type" items="${typesConge}">
                    <tr>
                        <td>${type.id}</td>
                        <td>${type.libelle}</td>
                        <td>${type.description}</td>
                        <td>${type.nbJoursMax}</td>
                        <td>
                            <c:choose>
                                <c:when test="${type.necessiteJustificatif}">Oui</c:when>
                                <c:otherwise>Non</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${type.actif}"><span class="text-success">Oui</span></c:when>
                                <c:otherwise><span class="text-danger">Non</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="empty-state">
            <p>Aucun type de congé trouvé.</p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
