package ru.job4j.job4j_hibernate.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.job4j.job4j_hibernate.models.Item;
import ru.job4j.job4j_hibernate.persistent.DaoItem;
import ru.job4j.job4j_hibernate.persistent.DaoItemHibernate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ItemServlet extends HttpServlet {
    private final DaoItem dao = DaoItemHibernate.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=utf-8");
        ObjectMapper mapper = new ObjectMapper();
        List<Item> res = null;
        if (req.getParameter("flag") != null
                && req.getParameter("flag").equals("checked")) {
            res = dao.getAll();
        } else {
            res = dao.getAllIsNotDone();
        }
        mapper.writeValue(resp.getOutputStream(), res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder res = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            reader.lines().forEach(res::append);
        }
        ObjectMapper mapper = new ObjectMapper();
        Item item = (Item) mapper.readValue(res.toString(), Item.class);
        dao.add(item);
    }
}
