<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="genres"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <div class="col-sm-4 col-sm-offset-4">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Title</th>
                </tr>
                </thead>
                <tbody>
                <%--@elvariable id="genres" type="java.util.List<ua.nure.serhieiev.library.model.Book>"--%>
                <c:forEach items="${genres}" var="genre">
                    <tr>
                        <th scope="row">
                            <a href="<c:url value="books.do?genre=${genre.id}"/>">${genre.title}</a>
                        </th>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <c:set var="action" scope="page" value="genres.do"/>
    <%@ include file="../jspf/pagination.jspf" %>
</div>

<%@ include file="../jspf/footer.jspf" %>
