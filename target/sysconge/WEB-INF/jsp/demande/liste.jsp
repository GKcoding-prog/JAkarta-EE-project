<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Mes demandes de congé"/>
</jsp:include>

<h1>📋 Mes demandes de congé</h1>

<c:if test="${param.success == 'created'}">
    <div class="alert alert-success">Votre demande de congé a été soumise avec succès.</div>
</c:if>

<div class="actions-bar">
    <a href="${pageContext.request.contextPath}/demande/nouvelle" class="btn btn-success">+ Nouvelle demande</a>
</div>

<c:choose>
    <c:when test="${not empty demandes}">
        <table class="data-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Type de congé</th>
                    <th>Date début</th>
                    <th>Date fin</th>
                    <th>Nb jours</th>
                    <th>Motif</th>
                    <th>Statut</th>
                    <th>Date demande</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="demande" items="${demandes}">
                    <tr>
                        <td>${demande.id}</td>
                        <td>${demande.typeConge.libelle}</td>
                        <td>${demande.dateDebut}</td>
                        <td>${demande.dateFin}</td>
                        <td>${demande.nbJours}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty demande.motif}">
                                    ${demande.motif}
                                </c:when>
                                <c:otherwise>
                                    <em>-</em>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <span class="status status-${demande.statut}">${demande.statut.libelle}</span>
                        </td>
                        <td>
                            <fmt:parseDate value="${demande.dateCreation}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/demande/detail/${demande.id}"
                               class="btn btn-sm btn-primary">Détail</a>
                            <c:if test="${demande.statut == 'EN_ATTENTE'}">
                                <form action="${pageContext.request.contextPath}/demande/annuler" method="post" style="display:inline;">
                                    <input type="hidden" name="demandeId" value="${demande.id}">
                                    <button type="submit" class="btn btn-sm btn-danger"
                                            onclick="return confirm('Êtes-vous sûr de vouloir annuler cette demande ?')">
                                        Annuler
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
            <p>Vous n'avez pas encore de demandes de congé.</p>
            <a href="${pageContext.request.contextPath}/demande/nouvelle" class="btn btn-success">Faire une demande</a>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
