package api;

import db.DataBase;
import model.User;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class UserController {

    public static final String Field_DELIMITER = "&";
    public static final String KEY_VALUE_DELIMITER = "=";

    public HttpResponse createUser(HttpRequest request) {
        User newUser = parseToUser(request.getBodyContent());
        DataBase.addUser(newUser);

        HttpResponse response = new HttpResponse();
        response.redirectUrl("/index.html");
        return response;
    }

    private User parseToUser(byte[] content) {
        String body = new String(content, StandardCharsets.UTF_8);
        Map<String, String> params = Arrays.stream(body.split(Field_DELIMITER))
                .map(elem -> elem.split(KEY_VALUE_DELIMITER, 2))
                .collect(Collectors.toMap(tokens -> tokens[0], tokens -> tokens[1]));
        return new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
    }
}
