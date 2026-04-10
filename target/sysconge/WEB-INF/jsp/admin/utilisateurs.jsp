<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Gestion des utilisateurs"/>
</jsp:include>

<h1>👥 Gestion des utilisateurs</h1>

<c:if test="${param.success == 'created'}">
    <div class="alert alert-success">Utilisateur créé avec succès.</div>
</c:if>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/admin/utilisateur/nouveau" class="btn btn-success">+ Nouvel utilisateur</a>
    <a href="${pageContext.request.contextPath}/admin/departements" class="btn btn-secondary">Départements</a>
    <a href="${pageContext.request.contextPath}/admin/types-conge" class="btn btn-secondary">Types de congé</a>
    <a href="${pageContext.request.contextPath}/admin/demandes" class="btn btn-secondary">Toutes les demandes</a>
</div>

<c:choose>
    <c:when test="${not empty utilisateurs}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Rôle</th>
                    <th>Actif</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${utilisateurs}">
                    <tr class="${user.actif ? '' : 'row-inactive'}">
                        <td>${user.id}</td>
                        <td>${user.nom}</td>
                        <td>${user.prenom}</td>
                        <td>${user.email}</td>
                        <td><span class="role-badge role-${user.role}">${user.role}</span></td>
                        <td>
                            <c:choose>
                                <c:when test="${user.actif}"><span class="text-success">Oui</span></c:when>
                                <c:otherwise><span class="text-danger">Non</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${user.actif && user.id != sessionScope.utilisateur.id}">
                                <form action="${pageContext.request.contextPath}/admin/utilisateur/desactiver" method="post" style="display:inline;">
                                    <input type="hidden" name="id" value="${user.id}">
                                    <button type="submit" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Désactiver cet utilisateur ?')">
                                        Désactiver
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:when>
    <c:otherwise>
        <div class="empty-state">
            <p>Aucun utilisateur trouvé.</p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
