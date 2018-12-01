package nl.jorith.blikje.websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.Validate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/service/coop/join")
public class JoinCoopServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        String coopKey = httpRequest.getParameter("key");
        Validate.notEmpty(coopKey);

        coopKey = coopKey.toUpperCase();

        List<Blikje> currentState = CoopMode.INSTANCE.joinCoop(coopKey);

        String returnJson = new Gson().toJson(currentState);

        httpResponse.setContentType("application/json");
        httpResponse.getWriter().write(returnJson);
        httpResponse.getWriter().close();
    }
}
