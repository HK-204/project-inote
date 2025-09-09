<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Danh sách ghi chú</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="container mt-4">
    <h2 class="mb-4">Danh sách ghi chú</h2>

    <form action="note" method="get" class="d-flex mb-3">
        <input type="hidden" name="action" value="search"/>
        <input type="text" name="keyword" class="form-control me-2" placeholder="Tìm kiếm..." value="${keyword}"/>
        <button type="submit" class="btn btn-primary">Tìm</button>
    </form>

    <a href="note?action=createForm" class="btn btn-success mb-3">+ Thêm ghi chú</a>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Tiêu đề</th>
                <th>Nội dung</th>
                <th>Ngày tạo</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
        <c:forEach var="note" items="${notes}">
            <tr>
                <td>${note.noteId}</td>
                <td>${note.title}</td>
                <td>${note.content}</td>
                <td>${note.createAt}</td>
                <td>
                    <a href="note?action=editForm&id=${note.noteId}" class="btn btn-warning btn-sm">Sửa</a>
                    <a href="note?action=delete&id=${note.noteId}" class="btn btn-danger btn-sm"
                       onclick="return confirm('Bạn có chắc chắn muốn xóa?')">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>
