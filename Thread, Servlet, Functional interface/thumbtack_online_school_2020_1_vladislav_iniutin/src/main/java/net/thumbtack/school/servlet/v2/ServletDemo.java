package net.thumbtack.school.servlet.v2;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ServletDemo {

    private static List<Person> persons = new ArrayList<>();
    private static Gson gson = new Gson();

    public static void main(String[] args) throws Exception {
        int port = 8080;
        Server server = new Server(port);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(PersonServlet.class, "/servlet");
        server.start();
        server.join();
    }

    public static class PersonServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(gson.toJson(persons));
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
                throws IOException {
            BufferedReader reader = request.getReader();
            String line = reader.readLine();
            Person person = gson.fromJson(line, Person.class);
            persons.add(person);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("added Person " + gson.toJson(person));
        }

        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
            persons.clear();
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println("all Persons deleted");
        }
    }
}