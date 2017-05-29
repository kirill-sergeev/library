<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="users"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="ui center aligned segment">
            <c:choose>
            <c:when test="${not empty users}">
            <table class="ui purple celled table sortable">
                <thead>
                <tr>
                    <th ${sort != 'email' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Email</th>
                    <th ${sort != 'name' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Name</th>
                    <th ${sort != 'registration_date' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Registration Date</th>
                    <th ${sort != 'last_visit' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Last Visit</th>
                    <th ${sort != 'enabled' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Active</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%--@elvariable id="users" type="java.util.List<ua.nure.serhieiev.library.model.entities.User>"--%>
                <c:forEach items="${users}" var="user">
                    <tr>
                        <td class="selectable"><a href="<c:url value="/user.do?id=${user.id}"/>">${user.email}</a></td>
                        <td>${user.name}</td>
                        <td>${user.registrationDate}</td>
                        <td>${user.lastVisit}</td>
                        <td class="center aligned">${user.enabled? '<i class="large green checkmark icon"></i>' : '<i class="large red minus icon"></i>'}</td>
                        <td class="center aligned">
                                <form action="${requestUri}" method="post">
                                <input type="hidden" name="user" value="${user.id}"/>
                                <c:if test="${user.enabled}">
                                <button class="ui left attached button submit orange icon" name="button" value="block"><i class="minus circle icon"></i></button>
                                </c:if>
                                <c:if test="${not user.enabled}">
                                    <button class="ui left attached button submit positive icon" name="button" value="activate"><i class="check circle outline icon"></i></button>
                                </c:if>
                                <button class="right attached ui button submit negative icon" name="button" value="remove"><i class="remove circle icon"></i></button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                <tr>
                    <th colspan="2">
                        <c:set var="action" scope="page" value="${requestUri.replace('/', '')}"/>
                        <%@ include file="../jspf/pagination.jspf" %>
                    </th>
                    <th colspan="4">
                        <form action="${requestUri}" method="get" class="ui form right floated">
                            <div class="inline fields">
                                <div class="field">
                                    <select class="ui dropdown" name="items">
                                        <option value="10">10</option>
                                        <option value="15">15</option>
                                        <option value="20">20</option>
                                        <option value="25">25</option>
                                        <option value="50">50</option>
                                    </select>
                                </div>
                                <div class="field">
                                    <select class="ui dropdown" name="sort">
                                        <option value="name">Name</option>
                                        <option value="email">Email</option>
                                        <option value="registration_date">Registration Date</option>
                                        <option value="last_visit">Last Visit</option>
                                        <option value="enabled">Active</option>
                                    </select>
                                </div>
                                <div class="field">
                                    <div class="ui toggle checkbox">
                                        <input <c:out value="${order == 'desc' ? 'checked=checked' : 'value=desc'}"/> type="checkbox" name="order">
                                        <label>DESC</label>
                                    </div>
                                </div>
                                <div class="field">
                                    <div class="ui submit button">Submit</div>
                                </div>
                            </div>
                        </form>
                    </th>
                </tr>
                </tfoot>
            </table>
            </c:when>
                <c:otherwise>
                    <div class="ui message info"><div class="header">No users</div></div>
                </c:otherwise>
            </c:choose>
            <c:if test="${requestUri == '/librarians.do'}">
                <div class="ui right floated small primary labeled icon button" onclick="location.href='<c:url value="/new-librarian.do"/>'">
                    <i class="user icon"></i> Add Librarian
                </div>
            </c:if>
        </div>
    </div>
</div>