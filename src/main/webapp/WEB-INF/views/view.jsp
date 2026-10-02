<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>iNotes - <c:out value="${note.title}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1><a href="${pageContext.request.contextPath}/notes">📝 iNotes</a></h1>
        <div class="storage">Lưu trữ: <strong><c:out value="${storage}"/></strong></div>
    </header>

    <div class="card">
        <h2 style="margin-top:0"><c:out value="${note.title}"/></h2>
        <span class="badge"><c:out value="${note.typeName}"/></span>
        <div class="content-box"><c:out value="${note.content}"/></div>
        <a class="btn" href="${pageContext.request.contextPath}/notes?action=edit&id=${note.id}">Sửa</a>
        <a class="btn secondary" href="${pageContext.request.contextPath}/notes">Quay lại</a>
    </div>
</div>
</body>
</html>
