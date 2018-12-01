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

@WebServlet(urlPatterns = "/service/coop/start")
public class StartCoopServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ServletException, IOException {
        String coopKey = httpRequest.getParameter("key");
        Validate.notEmpty(coopKey);

        coopKey = coopKey.toUpperCase();

        String body = httpRequest.getReader().lines().collect(Collectors.joining());

        Type typeOfObjectsList = new TypeToken<ArrayList<Blikje>>() {}.getType();
        List<Blikje> blikjes = new Gson().fromJson(body, typeOfObjectsList);

        CoopMode.INSTANCE.startNewCoop(coopKey, blikjes);
    }
}
