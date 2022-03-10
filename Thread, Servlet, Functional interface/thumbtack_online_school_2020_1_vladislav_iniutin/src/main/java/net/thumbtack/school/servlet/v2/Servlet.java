package net.thumbtack.school.servlet.v2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.server.Server;

import com.google.gson.Gson;

class Request {
    private int guess;

    public int getGuess() {
        return guess;
    }
}

class Response {
    private String result;

    public Response(String result) {
        this.result = result;
    }
}

public class Servlet {
    private static final Gson gson = new Gson();
    private static final int answer = (int) (Math.random() * 10000);

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(MyServlet.class, "/servlet");
        server.start();
        server.join();
    }

    public static class MyServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            Request guess = gson.fromJson(req.getReader().readLine(), Request.class);
            PrintWriter writer = resp.getWriter();
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");
            String result = null;
            if (guess.getGuess() < answer) {
                result = "Мало";
            }
            if (guess.getGuess() > answer) {
                result = "Много";
            }
            if (guess.getGuess() > answer) {
                result = "Угадал";
            }
            writer.println(gson.toJson(new Response(result)));
        }
    }
}
