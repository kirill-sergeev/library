<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pagename" scope="request" value="error"/>
<%@ include file="../jspf/header.jspf" %>


<div class="ui center aligned grid basic segment">
    <div class="column form-column">
        <div class="masthead error segment">
            <div class="container">
                <h1 class="ui dividing header">
                    That happens not to be a page
                </h1>
                <p>Rewind and try another one</p>
            </div>
        </div>
    </div>
</div>

<%@ include file="../jspf/footer.jspf" %>