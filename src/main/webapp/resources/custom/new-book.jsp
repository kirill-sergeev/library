<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

    $(document)
        .ready(function() {
            $('.ui.form')
                .form({
                    fields: {
                        title: {
                            identifier  : 'title',
                            rules: [
                                {
                                    type   : 'length[3]',
                                    prompt : '<fmt:message key="validation.title.length"/>'
                                }
                            ]
                        },
                        publisherTitle: {
                            identifier  : 'publisherTitle',
                            rules: [
                                {
                                    type   : 'length[3]',
                                    prompt : '<fmt:message key="validation.title.length"/>'
                                }
                            ]
                        },
                        isbn: {
                            identifier  : 'isbn',
                            rules: [
                                {
                                    type: 'regExp',
                                    value: /^\d{13}$/gi,
                                    prompt : '<fmt:message key="validation.isbn"/>'
                                }
                            ]
                        },
                        quantity: {
                            identifier  : 'quantity',
                            rules: [
                                {
                                    type   : 'integer[0..1000]',
                                    prompt : '<fmt:message key="validation.quantity"/>'
                                }
                            ]
                        },
                        description: {
                            identifier  : 'description',
                            rules: [
                                {
                                    type   : 'length[20]',
                                    prompt : '<fmt:message key="validation.description"/>'
                                }
                            ]
                        }
                    }
                })
            ;
        })
    ;

    $('.calendar').calendar();
