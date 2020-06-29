package blask.routing;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ViewTests {

  @Test
  public void basic() {
    View view = new View("GET", "/hola/mundo", "hola_mundo", false, "path/to/process.process");
    assertEquals("hola_mundo", view.getId());
    assertEquals("path/to/process.process", view.getTarget());
    assertEquals(false, view.isStatic());
  }

  @Test
  public void setters() {
    View view = new View();
    view.setId("get_static");
    view.setIsStatic(true);
    view.setTarget("path-to-target");
    Route route = new Route("GET", "/path/to/static", "get_static");
    view.setRoute(route);
    assertEquals("get_static", view.getId());
    assertEquals("path-to-target", view.getTarget());
    assertEquals(true, view.isStatic());
    assertEquals("get_static", route.getViewId());
  }
}