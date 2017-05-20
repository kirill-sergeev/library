<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pagename" scope="request" value="admin-users"/>
<%@ include file="../jspf/header.jspf" %>
<%@ include file="../jspf/navbar.jspf" %>

<div class="container">
    <div id="confirm" class="modal hide fade">
        <div class="modal-body">
            Are you sure?
        </div>
        <div class="modal-footer">
            <button type="button" data-dismiss="modal" class="btn btn-primary" id="delete">Delete</button>
            <button type="button" data-dismiss="modal" class="btn">Cancel</button>
        </div>
    </div>
    <div class="row">
        <div class="col-8 col-offset-2">
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
                    <%--@elvariable id="users" type="java.util.List<ua.nure.serhieiev.library.model.User>"--%>
                    <c:forEach items="${users}" var="user">
                    <tr>
                        <th scope="row">${user.email}</th>
                        <td>${user.name}</td>
                        <td>${user.registrationDate}</td>
                        <td>${user.lastVisit}</td>
                        <td>${user.enabled}</td>
                        <td>
                            <div class="btn-group">
                            <form action="<c:url value="/admin/users.do"/>" method="post">
                                <input type="hidden" name="user" value = "${user.id}"/>
                                <button type="submit" name="button" value="info"
                                        class="btn btn-info"><i class="fa fa-info fa-lg"></i></button>
                                <button type="submit" name="button" value="block"
                                        data-toggle="confirmation" data-singleton="true"
                                        class="btn btn-warning"><i class="fa fa-ban fa-lg"></i></button>
                                <button type="submit" name="button" value="remove"
                                        data-toggle="confirmation" data-singleton="true"
                                        class="btn btn-danger"><i class="fa fa-times fa-lg"></i></button>
                            </form>
                            </div>
                        </td>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>
