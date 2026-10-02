<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>iNotes - Danh sách ghi chú</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="container">
    <header>
        <h1><a href="${pageContext.request.contextPath}/notes">📝 iNotes</a></h1>
        <div class="storage">
            Lưu trữ hiện tại: <strong><c:out value="${storage}"/></strong>
            <form method="post" action="${pageContext.request.contextPath}/notes">
                <input type="hidden" name="action" value="storage">
                <select name="type">
                    <option value="file" ${storage == 'File' ? 'selected' : ''}>File (txt)</option>
                    <option value="db" ${storage == 'MySQL' ? 'selected' : ''}>MySQL</option>
                </select>
                <button class="btn secondary" type="submit">Chuyển</button>
            </form>
        </div>
    </header>

    <div class="card">
        <h2 style="margin-top:0">Danh Sách Ghi Chú</h2>

        <c:if test="${not empty success}"><div class="alert success"><c:out value="${success}"/></div></c:if>
        <c:if test="${not empty error}"><div class="alert error"><c:out value="${error}"/></div></c:if>

        <div class="toolbar">
            <form method="get" action="${pageContext.request.contextPath}/notes">
                <input type="text" name="q" value="<c:out value='${q}'/>" placeholder="Tìm theo tiêu đề hoặc nội dung...">
                <button class="btn" type="submit">Tìm kiếm</button>
            </form>
            <a class="btn" href="${pageContext.request.contextPath}/notes?action=new">+ Thêm Mới</a>
        </div>

        <table>
            <thead>
            <tr><th>#</th><th>Tiêu đề</th><th>Phân loại</th><th>Thao tác</th></tr>
            </thead>
            <tbody>
            <c:forEach var="n" items="${notes}" varStatus="s">
                <tr>
                    <td>${s.index + 1}</td>
                    <td><c:out value="${n.title}"/></td>
                    <td><span class="badge"><c:out value="${n.typeName}"/></span></td>
                    <td>
                        <div class="actions">
                            <a href="${pageContext.request.contextPath}/notes?action=view&id=${n.id}">Xem</a>
                            <a href="${pageContext.request.contextPath}/notes?action=edit&id=${n.id}">Sửa</a>
                            <form method="post" action="${pageContext.request.contextPath}/notes"
                                  onsubmit="return confirm('Bạn có chắc muốn xóa ghi chú này?');">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${n.id}">
                                <button type="submit">Xóa</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty notes}">
                <tr><td colspan="4" class="empty">Chưa có ghi chú nào.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
