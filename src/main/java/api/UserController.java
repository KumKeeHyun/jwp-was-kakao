package api;

import db.DataBase;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.Session;
import webserver.mvc.TemplateRenderer;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class UserController {

    public static final String Field_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";

    private final TemplateRenderer templateRenderer;

    public UserController(TemplateRenderer templateRenderer) {
        this.templateRenderer = templateRenderer;
    }

    public HttpResponse createUser(HttpRequest request) {
        User newUser = parseToUser(request.getBodyContent());
        DataBase.addUser(newUser);

        HttpResponse response = new HttpResponse();
        response.redirectUrl("/index.html");
        return response;
    }

    private User parseToUser(byte[] content) {
        Map<String, String> params = parseForm(content);
        return new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
    }

    private Map<String, String> parseForm(byte[] content) {
        String body = new String(content, StandardCharsets.UTF_8);
        return Arrays.stream(body.split(Field_DELIMITER))
                .map(elem -> elem.split(KEY_VALUE_DELIMITER, 2))
                .collect(Collectors.toMap(tokens -> tokens[0], tokens -> tokens[1]));
    }

    public HttpResponse listUser(HttpRequest request) {
        // TODO: 세션으로 로그인 유무 판단

        Collection<User> users = DataBase.findAll();

        HttpResponse response = new HttpResponse();
        templateRenderer.render(response, "user/list", users);
        return response;
    }

    public HttpResponse loginPage(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        templateRenderer.render(response, "user/login", null);
        return response;
    }

    public HttpResponse loginUser(HttpRequest request) {
        Map<String, String> params = parseForm(request.getBodyContent());
        String userId = params.get("userId");
        String password = params.get("password");

        HttpResponse response = new HttpResponse();

        User user = DataBase.findUserById(userId);
        boolean loginFailed = Optional.ofNullable(user)
                .map(User::getPassword)
                .filter(pw -> pw.equals(password))
                .isEmpty();
        if (loginFailed) {
            response.redirectUrl("/user/login_failed.html");
            return response;
        }

        Session session = new Session(UUID.randomUUID().toString());
        session.setAttribute("user", user);
        response.setSession(session);
        response.redirectUrl("/index.html");
        return response;
    }

    public HttpResponse profilePage(HttpRequest request) {
        Session session = request.getSession();
        User curUser = (User) session.getAttribute("user");

        HttpResponse response = new HttpResponse();
        templateRenderer.render(response, "user/profile", curUser);
        return response;
    }
}
