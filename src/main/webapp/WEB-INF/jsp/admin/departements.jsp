<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Gestion des départements"/>
</jsp:include>

<h1>🏛️ Gestion des départements</h1>

<c:if test="${param.success == 'created'}">
    <div class="alert alert-success">Département créé avec succès.</div>
</c:if>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/admin/departement/nouveau" class="btn btn-success">+ Nouveau département</a>
    <a href="${pageContext.request.contextPath}/admin/utilisateurs" class="btn btn-secondary">Utilisateurs</a>
</div>

<c:choose>
    <c:when test="${not empty departements}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nom</th>
                    <th>Description</th>
                    <th>Chef</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="dep" items="${departements}">
                    <tr>
                        <td>${dep.id}</td>
                        <td>${dep.nom}</td>
                        <td>${dep.description}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty dep.chef}">
                                    ${dep.chef.utilisateur.nomComplet}
                                </c:when>
                                <c:otherwise><em>Non assigné</em></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="empty-state">
            <p>Aucun département trouvé.</p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
