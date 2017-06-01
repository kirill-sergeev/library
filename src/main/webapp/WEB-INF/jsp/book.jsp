<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="book"/>
<%@ include file="../jspf/header.jspf" %>

<%--@elvariable id="alert" type="ua.nure.serhieiev.library.controller.util.Alert"--%>
<%--@elvariable id="book" type="ua.nure.serhieiev.library.model.entities.Book"--%>

<div class="ui center aligned grid basic segment">
    <div class="ui grid centered">
        <div class="ten wide column">
            <c:if test="${not empty alert}">
                <div class="row">
                    <div class="ui message ${alert.type}">
                        <div class="header"><fmt:message key="${alert.description}"/></div>
                    </div>
                </div>
            </c:if>
            <div class="ui left aligned segment">
                <div class="ui items">
                    <div class="item">
                        <div class="content">
                            <a class="header">${book.title}</a>
                            <div class="meta">
                                <span>
                                    <p>Authors:
                                     <c:forEach items="${book.authors}" var="author" varStatus="status">
                                         ${author.name}<c:if test="${not status.last}">,</c:if>
                                     </c:forEach>
                                    </p>
                                    <p>Genres:
                                      <c:forEach items="${book.genres}" var="genre" varStatus="status">
                                          ${genre.title}<c:if test="${not status.last}">,</c:if>
                                      </c:forEach>
                                    </p>
                                    <p>Publication Date: ${book.publicationDate}</p>
                                    <p>Description:</p>
                                </span>
                            </div>
                            <div class="description">
                                <p>${book.description}</p>
                            </div>
                            <div class="extra">
                                <c:if test="${user.role == 'ADMIN'}">
                                    <p> Quantity: ${book.quantity}</p>
                                </c:if>
                                <p>Available: ${book.available}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <c:if test="${user.role == 'READER' && user.enabled}">
                <label>Add to cart</label>
                <form action="<c:url value="/cart.do"/>" method="post">
                    <input type="hidden" name="book" value="${book.id}"/>
                    <div class="ui fitted slider checkbox">
                        <input
                            <c:out value="${localCart.get(book.id) == null ? 'value=add' : 'checked=checked'}"/>
                                <c:if test="${localCart.size() >= 10 && localCart.get(book.id) == null}">disabled="disabled"</c:if>
                                type="checkbox" name="button"
                                onchange="this.form.submit();">
                        <input type="hidden" name="button" value="remove"/>
                        <label></label>
                    </div>
                </form>
            </c:if>
            <c:if test="${user.role == 'ADMIN'}">
                <a href="<c:url value="/change-book.do?id=${book.id}"/>" class="ui button"><fmt:message key="book.button.change"/></a>
            </c:if>
        </div>
    </div>
</div>
<%@ include file="../jspf/footer.jspf" %>
