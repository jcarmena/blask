package blask.templating;

import java.io.IOException;
import java.util.Map;

public interface ITemplateEngine {

    public void initialize(String templatesPath, String templatesExtension, Boolean debugMode) throws IOException;

    public String renderTemplate(String template, Map<String, Object> context) throws Exception;
    public String renderTemplate(String template, String xmlContext) throws Exception;
    public String renderTemplate(String template) throws Exception;

}