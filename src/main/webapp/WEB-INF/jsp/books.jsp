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
    <c:set var="action" scope="page" value="books.do"/>
    <%@ include file="../jspf/pagination.jspf" %>
</div>

<%@ include file="../jspf/footer.jspf" %>
