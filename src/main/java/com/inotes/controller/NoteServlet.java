package com.inotes.controller;

import com.inotes.model.Note;
import com.inotes.service.NoteManagement;
import com.inotes.strategy.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Collections;

@WebServlet(urlPatterns = {"/notes"})
public class NoteServlet extends HttpServlet {

    private static final String VIEWS = "/WEB-INF/views/";
    private final NoteManagement manager = NoteManagement.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "new":
                    req.setAttribute("types", manager.getTypes());
                    forward(req, resp, "form.jsp");
                    break;
                case "edit": {
                    Note note = manager.getNote(intParam(req, "id"));
                    if (note == null) { resp.sendRedirect("notes"); return; }
                    req.setAttribute("note", note);
                    req.setAttribute("types", manager.getTypes());
                    forward(req, resp, "form.jsp");
                    break;
                }
                case "view": {
                    Note note = manager.getNote(intParam(req, "id"));
                    if (note == null) { resp.sendRedirect("notes"); return; }
                    req.setAttribute("note", note);
                    forward(req, resp, "view.jsp");
                    break;
                }
                default: {
                    String q = req.getParameter("q");
                    req.setAttribute("q", q == null ? "" : q);
                    req.setAttribute("notes", manager.searchNotes(q));
                    showList(req, resp);
                }
            }
        } catch (NoteException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("notes", Collections.emptyList());
            showList(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        String action = req.getParameter("action");
        if (action == null) action = "";

        try {
            switch (action) {
                case "save": {
                    String title = trim(req.getParameter("title"));
                    String content = req.getParameter("content");
                    int typeId = intParam(req, "typeId");
                    int id = intParam(req, "id");

                    if (title.isEmpty()) {
                        Note n = new Note(id, title, content, typeId);
                        req.setAttribute("note", n);
                        req.setAttribute("types", manager.getTypes());
                        req.setAttribute("error", "Tiêu đề không được để trống");
                        forward(req, resp, "form.jsp");
                        return;
                    }
                    if (id == 0) {
                        manager.addNote(title, content, typeId);
                        session.setAttribute("flashOk", "Đã thêm ghi chú mới");
                    } else {
                        manager.updateNote(id, title, content, typeId);
                        session.setAttribute("flashOk", "Đã cập nhật ghi chú");
                    }
                    break;
                }
                case "delete":
                    manager.deleteNote(intParam(req, "id"));
                    session.setAttribute("flashOk", "Đã xóa ghi chú");
                    break;
                case "storage": {
                    String type = req.getParameter("type");
                    if ("db".equals(type)) {
                        NoteDBStrategy db = new NoteDBStrategy();
                        db.testConnection(); // loi -> NoteException, giu nguyen chien luoc cu
                        manager.setStrategy(db);
                    } else {
                        manager.setStrategy(new NoteFileStrategy());
                    }
                    session.setAttribute("flashOk",
                            "Đã chuyển nơi lưu trữ sang: " + manager.getStrategyName());
                    break;
                }
                default:
                    break;
            }
        } catch (NoteException e) {
            session.setAttribute("flashError", e.getMessage());
        }
        resp.sendRedirect("notes");
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        moveFlash(session, req, "flashOk", "success");
        moveFlash(session, req, "flashError", "error");
        req.setAttribute("storage", manager.getStrategyName());
        forward(req, resp, "list.jsp");
    }

    private void moveFlash(HttpSession s, HttpServletRequest req, String key, String attr) {
        Object v = s.getAttribute(key);
        if (v != null) {
            req.setAttribute(attr, v);
            s.removeAttribute(key);
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String view)
            throws ServletException, IOException {
        req.setAttribute("storage", manager.getStrategyName());
        req.getRequestDispatcher(VIEWS + view).forward(req, resp);
    }

    private int intParam(HttpServletRequest req, String name) {
        try {
            return Integer.parseInt(req.getParameter(name));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private String trim(String s) { return s == null ? "" : s.trim(); }
}
