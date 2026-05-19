package com.expense.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet("/events")
public class ExpenseSSEServlet extends HttpServlet {

    private static final CopyOnWriteArrayList<PrintWriter> clients = new CopyOnWriteArrayList<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/event-stream");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setHeader("Connection", "keep-alive");

        PrintWriter writer = resp.getWriter();
        clients.add(writer);

        // Keep the connection open
        writer.write("data: Connected\n\n");
        writer.flush();
    }

    public static void broadcast(String message) {
        for (PrintWriter client : clients) {
            try {
                client.write("data: " + message + "\n\n");
                client.flush();
            } catch (Exception e) {
                clients.remove(client);
            }
        }
    }
}