<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="admin-users"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <c:if test="${not empty alert}">
        <div class="alert ${alert.type} alert-dismissable">
            <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
            <fmt:message key="${alert.description}"/>
        </div>
    </c:if>
    <div class="row">
        <div class="col-8 col-offset-2">
            <c:choose>
                <c:when test="${not empty users}">
                    <table class="table table-sm table-bordered">
                        <thead>
                        <tr>
                            <th>Email</th>
                            <th>Name</th>
                            <th>Registration Date</th>
                            <th>Last visit</th>
                            <th>Active</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                            <%--@elvariable id="users" type="java.util.List<ua.nure.serhieiev.library.model.entities.User>"--%>
                        <c:forEach items="${users}" var="user">
                            <tr>
                                <th scope="row">${user.email}</th>
                                <td>${user.name}</td>
                                <td>${user.registrationDate}</td>
                                <td>${user.lastVisit}</td>
                                <td>${user.enabled}</td>
                                <td>
                                    <div class="btn-group">
                                        <form
                                                <c:if test="${pageType == 'reader'}">
                                                    action="<c:url value="/readers.do"/>" method="post">
                                                </c:if>
                                                <c:if test="${pageType == 'librarian'}">
                                                    action="<c:url value="/librarians.do"/>" method="post">
                                                </c:if>
                                        <input type="hidden" name="user" value="${user.id}"/>
                                            <c:if test="${user.enabled}">
                                                <button type="submit" name="button" value="block"
                                                        class="btn btn-warning"><i class="fa fa-ban fa-lg"></i></button>
                                            </c:if>
                                            <c:if test="${not user.enabled}">
                                                <button type="submit" name="button" value="activate"
                                                        class="btn btn-success"><i class="fa fa-circle-o fa-lg"></i>
                                                </button>
                                            </c:if>
                                            <button type="submit" name="button" value="remove"
                                                    class="btn btn-danger"><i class="fa fa-times fa-lg"></i></button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:when>
                <c:otherwise>
                    <div class="alert alert-info">
                        No users!
                    </div>
                </c:otherwise>
            </c:choose>

            <c:if test="${pageType == 'librarian'}">
                <button class="btn btn-info btn-secondary" type="button"
                        onclick="location.href='<c:url value="/new-librarian.do"/>'">
                    New librarian
                </button>
            </c:if>

            <c:if test="${pageType == 'reader'}">
                <c:set var="action" scope="page" value="readers.do"/>
            </c:if>
            <c:if test="${pageType == 'librarian'}">
                <c:set var="action" scope="page" value="librarians.do"/>
            </c:if>
            <%@ include file="../jspf/pagination.jspf" %>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>
