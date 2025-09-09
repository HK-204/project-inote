<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sửa ghi chú</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="container mt-4">
    <h2 class="mb-4">Sửa ghi chú</h2>

    <form action="note" method="post">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" name="id" value="${note.noteId}"/>

        <div class="mb-3">
            <label class="form-label">Tiêu đề</label>
            <input type="text" name="title" class="form-control" value="${note.title}" required/>
        </div>

        <div class="mb-3">
            <label class="form-label">Nội dung</label>
            <textarea name="content" class="form-control" rows="5" required>${note.content}</textarea>
        </div>

        <button type="submit" class="btn btn-primary">Cập nhật</button>
        <a href="note?action=list" class="btn btn-secondary">Hủy</a>
    </form>
</body>
</html>
