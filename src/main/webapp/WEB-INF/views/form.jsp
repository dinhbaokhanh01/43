<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>iNotes - ${empty note || note.id == 0 ? 'Thêm mới' : 'Sửa'} ghi chú</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1><a href="${pageContext.request.contextPath}/notes">📝 iNotes</a></h1>
        <div class="storage">Lưu trữ: <strong><c:out value="${storage}"/></strong></div>
    </header>

    <div class="card">
        <h2 style="margin-top:0">${empty note || note.id == 0 ? 'Thêm Mới Ghi Chú' : 'Sửa Ghi Chú'}</h2>

        <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>

        <form method="post" action="${pageContext.request.contextPath}/notes">
            <input type="hidden" name="action" value="save">
            <input type="hidden" name="id" value="${empty note ? 0 : note.id}">

            <div class="field">
                <label for="title">Tiêu đề</label>
                <input type="text" id="title" name="title" maxlength="255" required
                       value="<c:out value='${note.title}'/>">
            </div>

            <div class="field">
                <label for="typeId">Phân loại</label>
                <select id="typeId" name="typeId">
                    <c:forEach var="t" items="${types}">
                        <option value="${t.id}" ${note.typeId == t.id ? 'selected' : ''}>
                            <c:out value="${t.name}"/>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="field">
                <label for="content">Nội dung</label>
                <textarea id="content" name="content"><c:out value="${note.content}"/></textarea>
            </div>

            <button class="btn" type="submit">Lưu Ghi Chú</button>
            <a class="btn secondary" href="${pageContext.request.contextPath}/notes">Hủy bỏ</a>
        </form>
    </div>
</div>
</body>
</html>
