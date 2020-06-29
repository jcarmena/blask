package blask.templating;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TemplateEngineTests {

  @Test
  public void pebble() throws Exception {
    TemplateEngine engine = new TemplateEngine();
    engine.initialize("pebble", "3.1", "src/test/resources/templates", "p", true);
    String result = engine.renderTemplate("template1");
    assertEquals("Hola mundo :)", result);

    Map<String, Object> context = new HashMap<String, Object>();
    context.put("nombre", "javier");
    String result2 = engine.renderTemplate("template2", context);
    assertEquals("Hola javier :)", result2);
  }

  @Test
  public void freemarker() throws Exception {
    TemplateEngine engine = new TemplateEngine();
    engine.initialize("freemarker", "2.3", "src/test/resources/templates", "f", true);
    String result = engine.renderTemplate("template1");
    assertNotEquals("Hola mundo", result);
    assertEquals("Hola mundo :)", result);

    Map<String, Object> context = new HashMap<String, Object>();
    context.put("nombre", "javier");
    String result2 = engine.renderTemplate("template2", context);
    assertEquals("Hola javier :)", result2);

    context.put("nombre", "javier");
    String result3 = engine.renderTemplate("template3", "<nombre>javier</nombre>");
    assertEquals("Hola javier :)", result3);
  }
}