<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="books"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div class="row">
        <div class="col-8 col-offset-2">
            <table class="table table-sm table-bordered sortable">
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Authors</th>
                    <th>Genres</th>
                    <th>Publisher</th>
                    <th>Publication</th>
                    <th>ISBN</th>
                    <th>Available</th>
                </tr>
                </thead>
                <tbody>
                <%--@elvariable id="books" type="java.util.List<ua.nure.serhieiev.library.model.Book>"--%>
                <c:forEach items="${books}" var="book">
                    <tr>
                        <th scope="row">${book.title}</th>
                        <td>
                            <c:forEach items="${book.authors}" var="author" varStatus="status">
                                ${author.name}<c:if test="${not status.last}">,</c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <c:forEach items="${book.genres}" var="genre" varStatus="status">
                                ${genre.title}<c:if test="${not status.last}">,</c:if>
                            </c:forEach>
                        </td>
                        <td>${book.publisher.title}</td>
                        <td>${book.publicationDate}</td>
                        <td>${book.isbn}</td>
                        <td>${book.available}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <nav>
        <div class="text-center">
        <ul class="pagination justify-content-center">
            <c:if test="${page > 2}">
                <li>
                    <a href="<c:url value="books.do?page=${1}&sort=${sort}&order=${order}"/>">
                        <i class="fa fa-angle-double-left" aria-hidden="true"></i></a>
                </li>
            </c:if>
            <c:if test="${page > 1}">
                <li>
                    <a href="<c:url value="books.do?page=${page-1}&sort=${sort}&order=${order}"/>">
                        <i class="fa fa-angle-left" aria-hidden="true"></i></a>
                </li>
            </c:if>
            <c:if test="${page > 2}">
                <li>
                    <a href="<c:url value="books.do?page=${page-2}&sort=${sort}&order=${order}"/>">${page-2}</a>
                </li>
            </c:if>
            <c:if test="${page > 1}">
                <li>
                    <a href="<c:url value="books.do?page=${page-1}&sort=${sort}&order=${order}"/>">${page-1}</a>
                </li>
            </c:if>
            <li class="active page-item disabled">
                <a href="#">${page}</a>
            </li>
            <c:if test="${nOfPages > page}">
                <li>
                    <a href="<c:url value="books.do?page=${page+1}&sort=${sort}&order=${order}"/>">${page+1}</a>
                </li>
            </c:if>
            <c:if test="${nOfPages > page+1}">
                <li>
                    <a href="<c:url value="books.do?page=${page+2}&sort=${sort}&order=${order}"/>">${page+2}</a>
                </li>
            </c:if>
            <c:if test="${page < nOfPages}">
                <li>
                    <a href="<c:url value="books.do?page=${page+1}&sort=${sort}&order=${order}"/>">
                        <i class="fa fa-angle-right" aria-hidden="true"></i></a>
                </li>
            </c:if>
            <c:if test="${page + 1 < nOfPages}">
                <li>
                    <a href="<c:url value="books.do?page=${nOfPages}&sort=${sort}&order=${order}"/>">
                        <i class="fa fa-angle-double-right" aria-hidden="true"></i></a>
                </li>
            </c:if>
        </ul>
        </div>
    </nav>
</div>

<%@ include file="../jspf/footer.jspf" %>
