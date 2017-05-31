<%@ page contentType="text/javascript" %>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    <fmt:setLocale value="${language}"/>
    <fmt:setBundle basename="text"/>

    $(document)
        .ready(function () {
            $('.ui.form')
                .form({
                    fields: {
                        title: {
                            identifier: 'title',
                            rules: [
                                {
                                    type: 'length[3]',
                                    prompt: '<fmt:message key="validation.title.length"/>'
                                }
                            ]
                        },
                        bookTitle: {
                            identifier: 'bookTitle',
                            rules: [
                                {
                                    type: 'length[3]',
                                    prompt: '<fmt:message key="validation.title.length"/>'
                                }
                            ]
                        },
                        publisherTitle: {
                            identifier: 'publisherTitle',
                            rules: [
                                {
                                    type: 'length[3]',
                                    prompt: '<fmt:message key="validation.title.length"/>'
                                }
                            ]
                        },
                        isbn: {
                            identifier: 'isbn',
                            rules: [
                                {
                                    type: 'regExp',
                                    value: /^(\d{13})?$/i,
                                    prompt: '<fmt:message key="validation.isbn"/>'
                                }
                            ]
                        },
                        quantity: {
                            identifier: 'quantity',
                            rules: [
                                {
                                    type: 'integer[0..1000]',
                                    prompt: '<fmt:message key="validation.quantity"/>'
                                }
                            ]
                        },
                        available: {
                            identifier: 'available',
                            rules: [
                                {
                                    type: 'integer[0..1000]',
                                    prompt: '<fmt:message key="validation.quantity"/>'
                                }
                            ]
                        },
                        description: {
                            identifier: 'description',
                            rules: [
                                {
                                    type: 'length[20]',
                                    prompt: '<fmt:message key="validation.description"/>'
                                }
                            ]
                        },
                        publicationDate: {
                            identifier: 'publicationDate',
                            rules: [
                                {
                                    type: 'length[8]',
                                    prompt: '<fmt:message key="validation.date"/>'
                                }
                            ]
                        },
                        author: {
                            identifier: 'authorName1',
                            rules: [
                                {
                                    type: 'length[3]',
                                    prompt: '<fmt:message key="validation.author"/>'
                                }
                            ]
                        },
                        genre: {
                            identifier: 'genreTitle1',
                            rules: [
                                {
                                    type: 'length[3]',
                                    prompt: '<fmt:message key="validation.genre"/>'
                                }
                            ]
                        },
                        password: {
                            identifier: 'password',
                            rules: [
                                {
                                    type: 'length[6]',
                                    prompt: '<fmt:message key="validation.password.length"/>'
                                },
                                {
                                    type: 'different[email]',
                                    prompt: '<fmt:message key="validation.password.different"/>'
                                }
                            ]
                        },
                        confirmation: {
                            identifier: 'retype-password',
                            rules: [
                                {
                                    type: 'match[password]',
                                    prompt: '<fmt:message key="validation.password.match"/>'
                                }
                            ]
                        },
                        name: {
                            identifier: 'name',
                            rules: [
                                {
                                    type: 'length[3]',
                                    prompt: '<fmt:message key="validation.name.length"/>'
                                }
                            ]
                        },
                        email: {
                            identifier: 'email',
                            rules: [
                                {
                                    type: 'email',
                                    prompt: '<fmt:message key="validation.email"/>'
                                }
                            ]
                        },
                        terms: {
                            identifier: 'terms',
                            rules: [
                                {
                                    type: 'checked',
                                    prompt: '<fmt:message key="validation.terms"/>'
                                }
                            ]
                        }
                    }
                })
            ;
        })
    ;
