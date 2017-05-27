<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

$(document)
    .ready(function() {
        $('.ui.form')
            .form({
                fields: {
                    name: {
                        identifier  : 'name',
                        rules: [
                            {
                                type   : 'length[3]',
                                prompt : '<fmt:message key="validation.name.length"/>'
                            }
                        ]
                    },
                    email: {
                        identifier  : 'email',
                        rules: [
                            {
                                type   : 'email',
                                prompt : '<fmt:message key="validation.email"/>'
                            }
                        ]
                    },
                    password: {
                        identifier  : 'password',
                        rules: [
                            {
                                type   : 'length[6]',
                                prompt : '<fmt:message key="validation.password.length"/>'
                            }
                        ]
                    },
                    terms: {
                        identifier: 'terms',
                        rules: [
                            {
                                type   : 'checked',
                                prompt : '<fmt:message key="validation.terms"/>'
                            }
                        ]
                    }
                }
            })
        ;
    })
;
$('.ui.checkbox')
    .checkbox()
;