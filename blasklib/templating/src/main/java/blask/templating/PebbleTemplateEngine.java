package blask.templating;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import com.mitchellbosecke.pebble.utils.PathUtils;

public class PebbleTemplateEngine implements ITemplateEngine, Serializable {

    private static final long serialVersionUID = -7267522981268431863L;
    PebbleEngine engine = null;

    public void initialize(final String templatesPath, final String templatesExtension, final Boolean debugMode) {
        final FileLoader loader = new FileLoader();
        loader.setPrefix(templatesPath);
        loader.setSuffix("." + templatesExtension);
        this.engine = new PebbleEngine.Builder().loader(loader).build();
        File f = getFile(loader, "template1");
        System.out.println(f.getAbsolutePath());
    }

    public File getFile(FileLoader loader, String templateName) {
        // add the prefix and ensure the prefix ends with a separator character
        StringBuilder path = new StringBuilder();
        if (loader.getPrefix() != null) {

            path.append(loader.getPrefix());

            if (!loader.getPrefix().endsWith(String.valueOf(File.separatorChar))) {
                path.append(File.separatorChar);
            }
        }

        templateName = templateName + (loader.getSuffix() == null ? "" : loader.getSuffix());


        /*
         * if template name contains path segments, move those segments into the path
         * variable. The below technique needs to know the difference between the path
         * and file name.
         */
        String[] pathSegments = PathUtils.PATH_SEPARATOR_REGEX.split(templateName);

        if (pathSegments.length > 1) {
            // file name is the last segment
            templateName = pathSegments[pathSegments.length - 1];
        }
        for (int i = 0; i < (pathSegments.length - 1); i++) {
            path.append(pathSegments[i]).append(File.separatorChar);
        }

        // try to load File
        return new File(path.toString(), templateName);
    }

    public String renderTemplate(final String template, final Map<String, Object> context) throws IOException {
        final PebbleTemplate compiledTemplate = this.engine.getTemplate(template);
        final Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);
        final String output = writer.toString();
        return output;
    }

    public String renderTemplate(final String template, String xmlContext) throws IOException {
        return "";
    }

    public String renderTemplate(final String template) throws IOException {
        final PebbleTemplate compiledTemplate = this.engine.getTemplate(template);
        final Writer writer = new StringWriter();
        Map<String, Object> context = new HashMap<String, Object>();
        compiledTemplate.evaluate(writer, context);
        final String output = writer.toString();
        return output;
    }

}