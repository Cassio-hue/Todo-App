package custommvc.servlet.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthFilter implements Filter {
    private static final String[] publicURLs = {"/login.html", "/notfound.html", "/css/", "/js/", "/images/"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        for (String publicURL : publicURLs) {
            if (path.startsWith(publicURL)) {
                chain.doFilter(request, response);
                return;
            }
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);

            if (values.length == 2) {
                String username = values[0];
                String password = values[1];

                if (isValidUser(username, password)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"" + "TodoApp" + "\"");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("HTTP Status 401 - Unauthorized");
    }

    private boolean isValidUser(String username, String password) {
        return "teste".equals(username) && "teste".equals(password);
    }
}