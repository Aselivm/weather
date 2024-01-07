package org.primshic.stepan.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.primshic.stepan.exception.ApplicationException;
import org.primshic.stepan.exception.ErrorMessage;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThymeleafUtil {
    public static void templateEngineProcessWithVariables(String template, HttpServletRequest req, HttpServletResponse resp, Map<String, Object> nameToVariable) throws IOException {
        TemplateEngine templateEngine = (TemplateEngine) req.getServletContext().getAttribute("templateEngine");
        WebContext ctx = new WebContext(req, resp, req.getServletContext(), req.getLocale());

        for (Map.Entry<String, Object> entry : nameToVariable.entrySet()) {
            ctx.setVariable(entry.getKey(), entry.getValue());
        }

        try {
            templateEngine.process(template, ctx, resp.getWriter());
            log.info("Template '{}' processed successfully with variables.", template);
        } catch (IOException e) {
            log.error("Error processing template '{}' with variables", template, e);
            throw new ApplicationException(ErrorMessage.INTERNAL_ERROR);
        }
    }
}
