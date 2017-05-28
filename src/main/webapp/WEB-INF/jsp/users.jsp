<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="users"/>
<%@ include file="../jspf/header.jspf" %>

<div class="ui middle aligned center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="ui center aligned segment">

            <table class="ui red striped table sortable">
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
                        <td>${user.enabled}</td>

                        <td>
                            <button class="ui left attached button">Left</button>
                            <button class="right attached ui button">Right</button>

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



                        </td>
                    </tr>
                </c:forEach>
                </tbody>
                <tfoot>
                <tr>
                    <th colspan="3">
                        <c:if test="${not empty author}"><c:set var="group" scope="page" value="author=${author}&"/></c:if>
                        <c:if test="${not empty genre}"><c:set var="group" scope="page" value="genre=${genre}&"/></c:if>
                        <c:if test="${not empty publisher}"><c:set var="group" scope="page" value="publisher=${publisher}&"/></c:if>
                        <c:set var="action" scope="page" value="books.do"/>
                        <%@ include file="../jspf/pagination.jspf" %>
                    </th>
                    <th colspan="1"></th>
                    <th colspan="4">
                        <form action="<c:url value="/books.do"/>" method="get" class="ui form right floated">
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
                                        <option value="title">Title</option>
                                        <option value="publisher_id">Publisher</option>
                                        <option value="publication_date">Publication Date</option>
                                        <option value="isbn">ISBN</option>
                                        <option value="available">Available</option>
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
        </div>
    </div>
</div>