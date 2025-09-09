<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="vi">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>iNote - Ứng dụng ghi chú</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <a class="navbar-brand" href="${pageContext.request.contextPath}/">iNote</a>

      <div class="collapse navbar-collapse">
        <ul class="navbar-nav me-auto">
          <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/note?action=list">Ghi chú</a></li>
        </ul>

        <ul class="navbar-nav">
          <c:choose>
            <c:when test="${not empty sessionScope.user}">
              <li class="nav-item">
                <span class="navbar-text me-3">Xin chào, ${sessionScope.user.username}</span>
              </li>
              <li class="nav-item">
                <a class="btn btn-sm btn-outline-light" href="${pageContext.request.contextPath}/user?action=logout">Đăng xuất</a>
              </li>
            </c:when>
            <c:otherwise>
              <li class="nav-item me-2">
                <a class="btn btn-sm btn-outline-light" href="${pageContext.request.contextPath}/user?action=loginForm">Đăng nhập</a>
              </li>
              <li class="nav-item">
                <a class="btn btn-sm btn-light text-primary" href="${pageContext.request.contextPath}/user?action=registerForm">Đăng ký</a>
              </li>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>
    </div>
  </nav>

  <main class="container py-5">
    <div class="row">
      <div class="col-md-8 mx-auto text-center">
        <h1 class="mb-3">iNote — Ứng dụng quản lý ghi chú</h1>
        <p class="lead mb-4">Tạo, sửa, xóa và tìm kiếm ghi chú của bạn</p>

        <div class="d-flex justify-content-center gap-2">
          <a class="btn btn-primary" href="${pageContext.request.contextPath}/note?action=list">Xem Ghi chú</a>
          <c:if test="${empty sessionScope.user}">
            <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/user?action=loginForm">Đăng nhập / Đăng ký</a>
          </c:if>
        </div>
      </div>
    </div>
  </main>

  <footer class="text-center py-3">
    <small class="text-muted">© iNote</small>
  </footer>

</body>
</html>
