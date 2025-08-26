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
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI();
        Map<String, Object> parameters = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> {
            parameters.put(key, value[0]);
        });

        response.setContentType("text/html; charset=UTF-8");
        Page page = null;

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
                if (uri.equals(route)) {
                    Class<?> clazz = routeClassInfo.loadClass();
                    try {
                        Object instance = clazz.getDeclaredConstructor().newInstance();
                        page = (Page) instance;
                        break;
                    } catch (Exception e) {
                        Logger.getLogger(MiniServletMVC.class.getName()).log(Level.SEVERE, "Erro ao construir página: " + routeClassInfo.getName(), e);
                    }
                }
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
