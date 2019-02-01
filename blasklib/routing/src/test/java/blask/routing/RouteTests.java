package blask.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RouteTests {

  @Test
  public void basic() {
    Route r1 = new Route("GET", "/hola/mundo", "hola_mundo");
    assertEquals("GET", r1.getMethod());
    assertEquals("/hola/mundo", r1.getPattern());
    assertEquals("hola/mundo", r1.getEffectivePattern());
    Pattern p = r1.getCompiledEffectivePattern();
    Matcher m = p.matcher("hola/mundo");
    assertTrue(m.matches());
    Matcher m2 = p.matcher("hola/javier");
    assertFalse(m2.matches());
    assertEquals("hola_mundo", r1.getViewId());
    assertTrue(r1.getInputParameters().isEmpty());
    assertTrue(r1.getReverseParameters().isEmpty());
  }

  @Test
  public void param() {
    Route r1 = new Route("GET", "/hola/<param1>", "hola_xx");
    assertEquals("GET", r1.getMethod());
    assertEquals("/hola/<param1>", r1.getPattern());
    assertEquals("hola/[^/]+", r1.getEffectivePattern());
    assertEquals("hola_xx", r1.getViewId());
    assertTrue(r1.getInputParameters().size()==1);
    ViewParameter vp = r1.getInputParameters().get(0);
    assertEquals("param1", vp.getName());
    assertEquals("[^/]+", vp.getRegex());
    assertTrue(r1.getReverseParameters().size()==1);
    ViewParameter vpr = r1.getReverseParameters().get(0);
    assertEquals("param1", vpr.getName());
    assertEquals("[^/]+", vpr.getRegex());
    Pattern p = r1.getCompiledEffectivePattern();
    Matcher m = p.matcher("hola/javier");
    assertTrue(m.matches());
    Matcher m2 = p.matcher("hola/javier/com");
    assertFalse(m2.matches());
    Map<String, String> args = r1.parseParameters("/hola/javier");
    assertTrue(args.size()==1);
    assertEquals(args.get("param1"), "javier");
  }

  @Test
  public void regexParam() {
    Route r1 = new Route("GET", "/hola/<p1:[a-zA-Z0-9]+>", "hola_xxxx");
    assertEquals("GET", r1.getMethod());
    assertEquals("/hola/<p1:[a-zA-Z0-9]+>", r1.getPattern());
    assertEquals("hola/[a-zA-Z0-9]+", r1.getEffectivePattern());
    assertEquals("hola_xxxx", r1.getViewId());
    assertTrue(r1.getInputParameters().size()==1);
    ViewParameter vp = r1.getInputParameters().get(0);
    assertEquals("p1", vp.getName());
    assertEquals("[a-zA-Z0-9]+", vp.getRegex());
    assertTrue(r1.getReverseParameters().size()==1);
    ViewParameter vpr = r1.getInputParameters().get(0);
    assertEquals("p1", vpr.getName());
    assertEquals("[a-zA-Z0-9]+", vpr.getRegex());
    Pattern p = r1.getCompiledEffectivePattern();
    Matcher m = p.matcher("hola/javier");
    assertTrue(m.matches());
    Matcher m2 = p.matcher("hola/user234/fail");
    assertFalse(m2.matches());
  }

  @Test
  public void reverseParam() {
    Route r1 = new Route("GET", "/hola/[nombre:[a-zA-Z0-9]+]", "hola_regex");
    assertEquals("GET", r1.getMethod());
    assertEquals("/hola/[nombre:[a-zA-Z0-9]+]", r1.getPattern());
    assertEquals("hola/[a-zA-Z0-9]+", r1.getEffectivePattern());
    assertEquals("hola_regex", r1.getViewId());
    assertTrue(r1.getInputParameters().isEmpty());
    assertTrue(r1.getReverseParameters().size()==1);
    ViewParameter vp = r1.getReverseParameters().get(0);
    assertEquals("nombre", vp.getName());
    assertEquals("[a-zA-Z0-9]+", vp.getRegex());
    Pattern p = r1.getCompiledEffectivePattern();
    Matcher m = p.matcher("hola/jaVIER35");
    assertTrue(m.matches());
    Matcher m2 = p.matcher("hola/jaVIER35/fail");
    assertFalse(m2.matches());
    assertEquals("hola/javier", r1.getReversePath(new String[]{"javier"}));
    Map<String, String> params = new HashMap<String, String>();
    params.put("nombre", "javier");
    assertEquals("hola/javier", r1.getReversePath(params));

  }

  @Test
  public void fullTest() {
    Route r1 = new Route("POST", "/p/items/[id:[a-zA-Z0-9_:+\\]]+]/<number:[0-9>:\\]]+>/save/all", "hola_fulltest");
    assertEquals("POST", r1.getMethod());
    assertEquals("/p/items/[id:[a-zA-Z0-9_:+\\]]+]/<number:[0-9>:\\]]+>/save/all", r1.getPattern());
    assertEquals("p/items/[a-zA-Z0-9_:+\\]]+/[0-9>:\\]]+/save/all", r1.getEffectivePattern());
    assertEquals("hola_fulltest", r1.getViewId());
    // input parameters
    assertTrue(r1.getInputParameters().size()==1);
    ViewParameter vpi = r1.getInputParameters().get(0);
    assertEquals("number", vpi.getName());
    assertEquals("[0-9>:\\]]+", vpi.getRegex());
    // reverse parameters
    assertTrue(r1.getReverseParameters().size()==2);
    ViewParameter vpr = r1.getReverseParameters().get(0);
    assertEquals("id", vpr.getName());
    assertEquals("[a-zA-Z0-9_:+\\]]+", vpr.getRegex());
    ViewParameter vpr2 = r1.getReverseParameters().get(1);
    assertEquals("number", vpr2.getName());
    assertEquals("[0-9>:\\]]+", vpr2.getRegex());
    // match url
    Pattern p = r1.getCompiledEffectivePattern();
    Matcher m = p.matcher("p/items/box_94/96001/save/all");
    assertTrue(m.matches());
    Matcher m2 = p.matcher("p/items/box_94/96a001/save/all");
    assertFalse(m2.matches());
    // reverse
    assertEquals("p/items/box_23/2218/save/all", r1.getReversePath(new String[]{"box_23", "2218"}));
    Map<String, String> params = new HashMap<String, String>();
    params.put("id", "box_20");
    params.put("number", "2210");
    assertEquals("p/items/box_20/2210/save/all", r1.getReversePath(params));
    // parse parameters
    Map<String, String> args = r1.parseParameters("/p/items/box_31/821/save/all");
    assertTrue(args.size()==1);
    assertEquals(args.get("number"), "821");
  }
}