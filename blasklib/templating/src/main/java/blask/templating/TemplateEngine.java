package blask.templating;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

public class TemplateEngine implements Serializable {

    private static final long serialVersionUID = -7365693809094615556L;
    ITemplateEngine engine = null;

    public TemplateEngine() {

    }

    public void initialize(String name, String version, String templatesPath, String templatesExtension,
            Boolean debugMode) throws IOException {
        if (name == "pebble") {
            if (version == "3.1" || version == "3.0") {
                this.engine = new PebbleTemplateEngine();
                this.engine.initialize(templatesPath, templatesExtension, debugMode);
                return;
            }
        }
        if (name == "freemarker") {
            if (version == "2.0" || version == "2.1" || version == "2.2" || version == "2.3") {
                this.engine = new FreemarkerTemplateEngine();
                this.engine.initialize(templatesPath, templatesExtension, debugMode);
                return;
            }
        }
        throw new UnsupportedOperationException(name + " engine not supported");
    }

    public String renderTemplate(final String template, final Map<String, Object> context) throws Exception {
        return this.engine.renderTemplate(template, context);
    }

    public String renderTemplate(final String template, String xmlContext) throws Exception {
        return this.engine.renderTemplate(template, xmlContext);
    }

    public String renderTemplate(final String template) throws Exception {
        return this.engine.renderTemplate(template);
    }
}