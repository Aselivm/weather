package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.K;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThymeleafUtil {
    public static void templateEngineProcess(String template, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        templateEngine.process(template, ctx, resp.getWriter());
    }

    public static void templateEngineProcessWithVariables(String template, HttpServletRequest req, HttpServletResponse resp, Map<String,Object> nameToVariable) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());
        for(Map.Entry<String, Object> entry : nameToVariable.entrySet()){
            ctx.setVariable(entry.getKey(),entry.getValue());
        }
        templateEngine.process(template, ctx, resp.getWriter());
    }
}
