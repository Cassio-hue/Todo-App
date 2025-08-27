package servlet;

import custom.annotations.Rota;
import io.github.classgraph.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servlet.pages.NotFoundPage;
import servlet.pages.Page;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MiniServletMVC extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(MiniServletMVC.class.getName());
    private final Map<String, Class<?>> UriPageMap = new HashMap<>();


    @Override
    public void init() {
        String pkg = "servlet.pages";
        String routeAnnotation = Rota.class.getName();
        try (ScanResult scanResult =
                     new ClassGraph()
                             .enableAllInfo()
                             .acceptPackages(pkg)
                             .scan()) {
            for (ClassInfo routeClassInfo : scanResult.getClassesWithAnnotation(routeAnnotation)) {
                AnnotationInfo routeAnnotationInfo = routeClassInfo.getAnnotationInfo(routeAnnotation);
                List<AnnotationParameterValue> routeParamVals = routeAnnotationInfo.getParameterValues();
                String route = (String) routeParamVals.getFirst().getValue();

                Class<?> clazz = routeClassInfo.loadClass();
                UriPageMap.put(route, clazz);
            }
        }
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        Map<String, Object> parameters = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            parameters.put(key, value[0]);
        });
        response.setContentType("text/html; charset=UTF-8");

        Page page = null;
        if (UriPageMap.containsKey(uri)) {
            Class<?> clazz = UriPageMap.get(uri);
            try {
                Object instance = clazz.getDeclaredConstructor().newInstance();
                page = (Page) instance;
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Erro ao instanciar página: " + clazz.getName(), e);
            }
        }

        if (page == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(NotFoundPage.render());
            return;
        }

        response.getWriter().write(page.render(parameters));
    }
}
