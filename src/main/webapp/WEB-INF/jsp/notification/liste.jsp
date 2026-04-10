<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<jsp:include page="/WEB-INF/jsp/common/header.jsp">
    <jsp:param name="titre" value="Notifications"/>
</jsp:include>

<h1>🔔 Mes notifications</h1>

<c:if test="${not empty notifications}">
    <div class="actions-bar">
        <form action="${pageContext.request.contextPath}/notification/marquer-toutes-lues" method="post">
            <button type="submit" class="btn btn-secondary">Marquer toutes comme lues</button>
        </form>
    </div>
</c:if>

<c:choose>
    <c:when test="${not empty notifications}">
        <div class="notifications-list">
            <c:forEach var="notif" items="${notifications}">
                <div class="notification-item ${notif.lue ? '' : 'notification-unread'}">
                    <div class="notification-header">
                        <h3>
                            <c:if test="${!notif.lue}">🔵 </c:if>
                            ${notif.titre}
                        </h3>
                        <span class="notification-date">
                            <fmt:parseDate value="${notif.dateCreation}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDate" type="both"/>
                            <fmt:formatDate value="${parsedDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </span>
                    </div>
                    <p>${notif.message}</p>
                    <c:if test="${!notif.lue}">
                        <form action="${pageContext.request.contextPath}/notification/marquer-lue" method="post" style="display:inline;">
                            <input type="hidden" name="notificationId" value="${notif.id}">
                            <button type="submit" class="btn btn-sm btn-secondary">Marquer comme lue</button>
                        </form>
                    </c:if>
                    <c:if test="${not empty notif.demande}">
                        <a href="${pageContext.request.contextPath}/demande/detail/${notif.demande.id}"
                           class="btn btn-sm btn-primary">Voir la demande</a>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </c:when>
    <c:otherwise>
        <div class="empty-state">
            <p>Aucune notification.</p>
        </div>
    </c:otherwise>
</c:choose>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp"/>
