package com.example.servlet;

import com.example.dao.NoteDAO;
import com.example.model.Note;
import com.example.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/note")
public class NoteServlet extends HttpServlet {
    private NoteDAO noteDAO;

    @Override
    public void init() throws ServletException {
        noteDAO = new NoteDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "createForm":
                request.getRequestDispatcher("/notes/create.jsp").forward(request, response);
                break;
            case "editForm":
                showEditForm(request, response);
                break;
            case "delete":
                deleteNote(request, response);
                break;
            case "search":
                searchNotes(request, response);
                break;
            default:
                listNotes(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "";

        switch (action) {
            case "create":
                createNote(request, response);
                break;
            case "update":
                updateNote(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/note?action=list");
        }
    }

    private void listNotes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = (User) request.getSession().getAttribute("user");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        List<Note> notes = noteDAO.findAllByUser(u.getUserId());
        int noteCount = noteDAO.countByUser(u.getUserId());

        request.setAttribute("notes", notes);
        request.setAttribute("noteCount", noteCount);
        request.getRequestDispatcher("/notes/list.jsp").forward(request, response);
    }

    private void createNote(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User u = (User) request.getSession().getAttribute("user");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        String title = safeTrim(request.getParameter("title"));
        String content = safeTrim(request.getParameter("content"));

        Note n = new Note();
        n.setUserId(u.getUserId());
        n.setTitle(title);
        n.setContent(content);

        noteDAO.insertNote(n);
        response.sendRedirect(request.getContextPath() + "/note?action=list");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // kiểm tra session user
        HttpSession session = request.getSession(false);
        User u = session == null ? null : (User) session.getAttribute("user");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        int id = parseIntParam(request.getParameter("id"), -1);
        if (id < 0) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai id");
            return;
        }

        Note n = noteDAO.findById(id);
        if (n == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Note không tồn tại");
            return;
        }

        // kiểm tra quyền sở hữu
        if (n.getUserId() != u.getUserId()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Không có quyền truy cập note này");
            return;
        }

        request.setAttribute("note", n);
        request.getRequestDispatcher("/notes/edit.jsp").forward(request, response);
    }

    private void updateNote(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User u = (User) request.getSession().getAttribute("user");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        int id = parseIntParam(request.getParameter("id"), -1);
        String title = safeTrim(request.getParameter("title"));
        String content = safeTrim(request.getParameter("content"));

        if (id < 0) {
            response.sendRedirect(request.getContextPath() + "/note?action=list");
            return;
        }

        Note n = new Note();
        n.setNoteId(id);
        n.setUserId(u.getUserId());
        n.setTitle(title);
        n.setContent(content);

        noteDAO.updateNote(n);
        response.sendRedirect(request.getContextPath() + "/note?action=list");
    }

    private void deleteNote(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        User u = (User) request.getSession().getAttribute("user");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        int id = parseIntParam(request.getParameter("id"), -1);
        if (id >= 0) {
            noteDAO.deleteNote(id, u.getUserId());
        }
        response.sendRedirect(request.getContextPath() + "/note?action=list");
    }

    private void searchNotes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User u = (User) request.getSession().getAttribute("user");
        if (u == null) {
            response.sendRedirect(request.getContextPath() + "/user?action=loginForm");
            return;
        }

        String keyword = safeTrim(request.getParameter("keyword"));
        List<Note> notes = noteDAO.searchByUserLike(u.getUserId(), keyword);

        request.setAttribute("notes", notes);
        request.setAttribute("keyword", keyword);
        request.getRequestDispatcher("/notes/list.jsp").forward(request, response);
    }

    // utility
    private int parseIntParam(String p, int defaultValue) {
        if (p == null) return defaultValue;
        try {
            return Integer.parseInt(p);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private String safeTrim(String s) {
        return s == null ? null : s.trim();
    }
}
