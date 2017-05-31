<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="books"/>
<%@ include file="../jspf/header.jspf" %>


<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <c:choose>
            <%--@elvariable id="book" type="java.util.List<ua.nure.serhieiev.library.model.entities.Book>"--%>
            <c:when test="${not empty books}">
                <div class="ui center aligned segment">
                    <div class="right floated left aligned four wide column">
                        <form action="<c:url value="/books.do"/>" method="get" class="ui form">
                            <div class="ui action input">
                                <input type="text" name="search" placeholder="Search...">
                                <button class="ui icon button"><i class="search icon"></i></button>
                            </div>
                        </form>
                    </div>
                    <table class="ui red striped table sortable">
                        <thead>
                        <tr>
                            <th ${sort != 'title' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Title</th>
                            <c:if test="${empty author && empty genre}">
                            <th>Authors</th>
                            <th>Genres</th>
                            </c:if>
                            <th ${sort != 'publisher_id' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Publisher</th>
                            <th ${sort != 'publication_date' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Publication</th>
                            <th ${sort != 'isbn' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>ISBN</th>
                            <c:if test="${user.role == 'ADMIN'}">
                            <th ${sort != 'quantity' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Quantity</th>
                            </c:if>
                            <th ${sort != 'available' ? '' : order == 'desc' ? 'class="sorted descending"' : 'class="sorted ascending"'}>Available</th>
                            <c:if test="${user.role == 'READER' && user.enabled}">
                            <th>To cart</th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${books}" var="book">
                            <tr>
                                <td class="selectable"><a
                                        href="<c:url value="/book.do?id=${book.id}"/>">${book.title}</a></td>
                                <c:if test="${empty author && empty genre}">
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
                                </c:if>
                                <td>${book.publisher.title}</td>
                                <td>${book.publicationDate}</td>
                                <td>${book.isbn}</td>
                                <c:if test="${user.role == 'ADMIN'}">
                                <td>${book.quantity}</td>
                                </c:if>
                                <td>${book.available}</td>
                                <c:if test="${user.role == 'READER' && user.enabled}">
                                <td>
                                    <jsp:useBean id="localCart" scope="session"
                                                 type="java.util.Map<java.lang.Integer, java.time.LocalDateTime>"/>
                                    <c:if test="${book.available > 0 || localCart.get(book.id) != null}">
                                        <form action="<c:url value="/cart.do"/>" method="post">
                                            <input type="hidden" name="book" value="${book.id}"/>
                                            <div class="ui fitted slider checkbox">
                                                <input
                                                    <c:out value="${localCart.get(book.id) == null ? 'value=add' : 'checked=checked'}"/>
                                                        <c:if test="${localCart.size() >= 10 && localCart.get(book.id) == null}">disabled="disabled"</c:if>
                                                        type="checkbox" name="button" onchange="this.form.submit();">
                                                <label></label>
                                                <input type="hidden" name="button" value="remove"/>
                                            </div>
                                        </form>
                                    </c:if>
                                </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <th colspan="2">
                                <c:if test="${not empty author}"><c:set var="group" scope="page" value="author=${author}&"/></c:if>
                                <c:if test="${not empty genre}"><c:set var="group" scope="page" value="genre=${genre}&"/></c:if>
                                <c:if test="${not empty publisher}"><c:set var="group" scope="page" value="publisher=${publisher}&"/></c:if>
                                <c:set var="action" scope="page" value="books.do"/>
                                <%@ include file="../jspf/pagination.jspf" %>
                            </th>
                            <th colspan="1"></th>
                            <th colspan="5">
                                <form action="<c:url value="/books.do?"/>" method="get" class="ui form">
                                    <c:choose>
                                        <c:when test="${not empty author}">
                                            <input type="hidden" name="author" value="${author}">
                                        </c:when>
                                        <c:when test="${not empty genre}">
                                        <input type="hidden" name="genre" value="${genre}">
                                    </c:when>
                                        <c:when test="${not empty publisher}">
                                            <input type="hidden" name="publisher" value="${publisher}">
                                        </c:when>
                                    </c:choose>
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
                                                <c:if test="${user.role == 'ADMIN'}">
                                                <option value="quantity">Quantity</option>
                                                </c:if>
                                                <option value="available">Available</option>
                                            </select>
                                        </div>
                                        <div class="field">
                                            <div class="ui toggle checkbox">
                                                <input
                                                    <c:out value="${order == 'desc' ? 'checked=checked' : 'value=desc'}"/>
                                                        type="checkbox" name="order">
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
            </c:when>
            <c:otherwise>
                <div class="ui message info">
                    <div class="header">Not found</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
<%--<script type="text/javascript">
    function init() {
        var titles = document.getElementsByTagName("th");
        for (var i = 0, len = titles.length; i < len; i++) {
            titles[i].addEventListener("click", function () {
                location.href = window.location.href.concat(this.innerHTML.toLowerCase());
            }, false);
        }
    }
    window.onload = init;
</script>--%>
<%@ include file="../jspf/footer.jspf" %>