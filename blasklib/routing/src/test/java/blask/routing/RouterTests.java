package blask.routing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RouterTests {

  @Test
  public void basic() {
      Router router = new Router();
      View view = new View("GET", "/hola/mundo", "hola_mundo", false, "proeceso.process");
      router.addView(view);

      RouteResult result = router.route("GET", "/hola/mundo");
      assertEquals(true, result.getMatched());
      assertEquals(view.getId(), result.getView().getId());
      assertTrue(result.getParametersList().isEmpty());
  }

  @Test
  public void defaultView() {
      Router router = new Router();
      View view = new View("GET", "/hola/mundo", "hola_mundo", false, "proeceso.process");
      View defaultView = new View(null, null, "default", false, "default.process");
      router.addView(view);
      router.setDefaultView(defaultView);

      RouteResult result = router.route("GET", "/foo/bar");
      assertEquals(false, result.getMatched());
      assertEquals(defaultView.getId(), result.getView().getId());
      assertTrue(result.getParametersList().isEmpty());
  }

  @Test
  public void parameters() {
      Router router = new Router();
      View viewSaludo = new View("GET", "/hola/<nombre>/saludo", "hola_mundo", false, "hola.process");
      View viewDespedida = new View("GET", "/hola/<nombre>/despedida", "adios_mundo", false, "adios.process");
      View viewApellido = new View("GET", "/hola/<apellido>/despedida", "adios_apellido", false, "adios_apellido.process");
      View defaultView = new View(null, null, "default", false, "default.process");
      router.addView(viewSaludo);
      router.addView(viewDespedida);
      router.addView(viewApellido);
      router.setDefaultView(defaultView);

      RouteResult result = router.route("GET", "/hola/javier/saludo");
      assertEquals(true, result.getMatched());
      assertEquals(viewSaludo.getId(), result.getView().getId());
      assertTrue(result.getParametersList().size()==1);
      assertEquals("nombre", result.getParametersList().get(0).getName());
      assertEquals("javier", result.getParametersList().get(0).getValue());

      RouteResult result2 = router.route("GET", "/hola/alberto/despedida");
      assertEquals(true, result2.getMatched());
      assertEquals(viewDespedida.getId(), result2.getView().getId());
      assertTrue(result2.getParametersList().size()==1);
      assertEquals("nombre", result2.getParametersList().get(0).getName());
      assertEquals("alberto", result2.getParametersList().get(0).getValue());

      RouteResult result3 = router.route("GET", "/hola/alberto/despedida/final");
      assertEquals(false, result3.getMatched());
      assertEquals(defaultView.getId(), result3.getView().getId());
      assertTrue(result3.getParametersList().isEmpty());
  }

  @Test
  public void regex() {
      Router router = new Router();
      View view = new View("GET", "/p/[project:[abc234]+]/f/[item:item[0-9]+]/all", "regex1", false, "regex1.process");
      View defaultView = new View(null, null, "default", false, "default.process");
      router.addView(view);
      router.setDefaultView(defaultView);

      RouteResult result = router.route("GET", "/p/a5/f/item14/all");
      assertEquals(false, result.getMatched());
      assertEquals(defaultView.getId(), result.getView().getId());
      assertTrue(result.getParametersList().isEmpty());

      RouteResult result2 = router.route("GET", "/p/acb43222/f/item14/all");
      assertEquals(true, result2.getMatched());
      assertEquals(view.getId(), result2.getView().getId());
      assertTrue(result2.getParametersList().isEmpty());

      RouteResult result3 = router.route("GET", "/p/c3/f/item08/all");
      assertEquals(true, result3.getMatched());
      assertEquals(view.getId(), result3.getView().getId());
      assertTrue(result3.getParametersList().isEmpty());
  }
}