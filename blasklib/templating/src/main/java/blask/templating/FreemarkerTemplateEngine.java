package blask.templating;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.InputSource;

import freemarker.ext.dom.NodeModel;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreemarkerTemplateEngine implements ITemplateEngine, Serializable {

    private static final long serialVersionUID = 5068046835061432590L;

    private Configuration cfg = null;
    private String templatesExtension = "";

    public void initialize() throws IOException {

    }

    @Override
    public void initialize(final String templatesPath, final String templatesExtension, final Boolean debugMode)
            throws IOException {
        // Create your Configuration instance, and specify if up to what FreeMarker
        // version (here 2.3.29) do you want to apply the fixes that are not 100%
        // backward-compatible. See the Configuration JavaDoc for details.
        final Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);

        // Specify the source where the template files come from. Here I set a
        // plain directory for it, but non-file-system sources are possible too:
        cfg.setDirectoryForTemplateLoading(new File(templatesPath));
        this.templatesExtension = templatesExtension;
        // From here we will set the settings recommended for new projects. These
        // aren't the defaults for backward compatibilty.

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is
        // better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        // Wrap unchecked exceptions thrown during template processing into
        // TemplateException-s:
        cfg.setWrapUncheckedExceptions(true);

        // Do not fall back to higher scopes when reading a null loop variable:
        cfg.setFallbackOnNullLoopVariable(false);
        this.cfg = cfg;
    }

    @Override
    public String renderTemplate(final String template) throws Exception {
        Template temp = this.cfg.getTemplate(template + "." + this.templatesExtension);
        Writer out = new StringWriter();
        Map<String, Object> ctx = new HashMap<String, Object>();
        temp.process(ctx, out);
        return out.toString();
    }

    @Override
    public String renderTemplate(final String template, final Map<String, Object> context) throws Exception {
        Template temp = this.cfg.getTemplate(template + "." + this.templatesExtension);
        Writer out = new StringWriter();
        temp.process(context, out);
        return out.toString();
    }

    @Override
    public String renderTemplate(final String template, final String xmlContext) throws Exception {
        final InputSource is = new InputSource(new StringReader(xmlContext));
        final NodeModel nm = NodeModel.parse(is);
        final Map<String, Object> context = new HashMap<String, Object>();
        context.put("context", nm);
        Template temp = this.cfg.getTemplate(template +"."+ this.templatesExtension);
        Writer out = new StringWriter();
        temp.process(context, out);
        return out.toString();
    }

}