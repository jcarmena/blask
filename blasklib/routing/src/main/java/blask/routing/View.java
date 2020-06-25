package blask.routing;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


// ================================
// View
// ================================

public class View implements Serializable {

    private static final long serialVersionUID = 7501313103067080022L;

    private String id = null;
    private boolean isStatic = false;
    private String target = null;
    private Route route = null;
    private Map<String, Object> options = new HashMap<String, Object>();


    public View() {
    }

    public View(String method, String uriPattern, String id, boolean isStatic, String target) {
        if (method!=null && uriPattern!=null) {
            this.route = new Route(method, uriPattern, id);
        }
        this.setId(id);
        this.setIsStatic(isStatic);
        this.setTarget(target);
    }

    public View(String method, String uriPattern, String id, boolean isStatic, String target, Map<String, Object> options) {
        if (method!=null && uriPattern!=null) {
            this.route = new Route(method, uriPattern, id);
        }
        this.setId(id);
        this.setIsStatic(isStatic);
        this.setTarget(target);
        this.setOptions(options);
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public String getTarget() {
        return this.target;
    }

    public String getId() {
        return this.id;
    }

    public View setId(String id) {
        this.id = id;
        return this;
    }

    public View setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
        return this;
    }

    public View setTarget(String target) {
        this.target = target;
        return this;
    }

    public Map<String, Object> getOptions() {
        return this.options;
    }

    View setOptions(Map<String, Object> options) {
        this.options = options;
        return this;
    }

    public Route getRoute() {
        return this.route;
    }

    public View setRoute(Route route) {
        this.route = route;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof View)) {
            return false;
        }
        View view = (View) o;
        return Objects.equals(id, view.id) && isStatic == view.isStatic && Objects.equals(target, view.target);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isStatic, target);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", isStatic='" + isStatic() + "'" +
            ", target='" + getTarget() + "'" +
            "}";
    }


}
