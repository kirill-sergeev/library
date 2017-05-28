<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="authors"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="ui middle aligned center aligned grid segment">
    <div class="container">
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <table class="table table-sm table-bordered sortable">
                    <thead>
                    <tr>
                        <th>Name</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%--@elvariable id="authors" type="java.util.List<ua.nure.serhieiev.library.model.entities.Book>"--%>
                    <c:forEach items="${authors}" var="author">
                        <tr>
                            <th scope="row">
                                <a href="<c:url value="books.do?author=${author.id}"/>">${author.name}</a>
                            </th>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <c:set var="action" scope="page" value="authors.do"/>
        <%@ include file="../jspf/pagination.jspf" %>
    </div>
</div>
<%@ include file="../jspf/footer.jspf" %>