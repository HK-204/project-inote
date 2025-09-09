package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;

import com.example.util.DBConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = DBConnection.getConnection();
            userDAO = new UserDAO(conn);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) action = "loginForm";

        switch (action) {
            case "registerForm":
                request.getRequestDispatcher("users/register.jsp").forward(request, response);
                break;
            case "logout":
                HttpSession session = request.getSession(false);
                if (session != null) session.invalidate();
                response.sendRedirect("index.jsp");
                break;
            default:
                request.getRequestDispatcher("users/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) action = "";

        switch (action) {
            case "register":
                handleRegister(request, response);
                break;
            case "login":
                handleLogin(request, response);
                break;
            default:
                response.sendRedirect("user?action=loginForm");
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        User u = new User();
        u.setUsername(username);
        u.setPassword(password);

        if (userDAO.addUser(u)) {
            response.sendRedirect("user?action=loginForm");
        } else {
            request.setAttribute("error", "Đăng ký thất bại, vui lòng thử lại!");
            request.getRequestDispatcher("users/register.jsp").forward(request, response);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        User u = userDAO.getUserByUsername(username);

        if (u != null && u.getPassword().trim().equals(password.trim())) {
            HttpSession session = request.getSession();
            session.setAttribute("user", u);
            response.sendRedirect("note?action=list");
        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
            request.getRequestDispatcher("users/login.jsp").forward(request, response);
        }
    }
}
