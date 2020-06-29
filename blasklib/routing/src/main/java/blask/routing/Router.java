package blask.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router implements Serializable {

    private static final long serialVersionUID = -4029110093582841408L;
    private View defaultView = null;
    private Map<String, View> views = new HashMap<String, View>();
    private Map<String, List<View>> methodViews = new HashMap<String, List<View>>();

    public Router() {

    }

    public void addView(View view) {
        this.views.put(view.getId(), view);
        if (view.getRoute() != null) {
            if (!methodViews.containsKey(view.getRoute().getMethod())) {
                methodViews.put(view.getRoute().getMethod(), new ArrayList<View>());
            }
            methodViews.get(view.getRoute().getMethod()).add(view);
        }
    }

    public View addView(String method, String uriPattern, String id, boolean isStatic, String target) {
        View view = new View(method, uriPattern, id, isStatic, target);
        this.views.put(view.getId(), view);
        if (view.getRoute() != null) {
            if (!methodViews.containsKey(view.getRoute().getMethod())) {
                methodViews.put(view.getRoute().getMethod(), new ArrayList<View>());
            }
            methodViews.get(view.getRoute().getMethod()).add(view);
        }
        return view;
    }

    public View addView(String method, String uriPattern, String id, boolean isStatic, String target, Map<String, Object> options) {
        View view = new View(method, uriPattern, id, isStatic, target, options);
        this.views.put(view.getId(), view);
        if (view.getRoute() != null) {
            if (!methodViews.containsKey(view.getRoute().getMethod())) {
                methodViews.put(view.getRoute().getMethod(), new ArrayList<View>());
            }
            methodViews.get(view.getRoute().getMethod()).add(view);
        }
        return view;
    }

    public Router setDefaultView(View view) {
        this.defaultView = view;
        return this;
    }

    public View setDefaultView(boolean isStatic, String target) {
        View view = new View(null, null, "default", isStatic, target);
        this.defaultView = view;
        return view;
    }

    public View setDefaultView(boolean isStatic, String target, Map<String, Object> options) {
        View view = new View(null, null, "default", isStatic, target, options);
        this.defaultView = view;
        return view;
    }

    public View getDefaultView() {
        return this.defaultView;
    }

    public RouteResult route(String method, String path) {
        RouteResult result = new RouteResult();

        if (!methodViews.containsKey(method)) {
            throw new IllegalArgumentException("Unknown method " + method);
        }
        List<View> views = methodViews.get(method);
        for (View v : views) {
            Pattern p = v.getRoute().getCompiledEffectivePattern();
            String auxpath = path.replaceAll("^ */|/ *$", "");
            Matcher m = p.matcher(auxpath);
            if (m.matches()) {
                result.setMatched(true);
                result.setView(v);
                break;
            }
        }
        if (!result.getMatched() && (this.getDefaultView() != null)) {
            result.setView(this.getDefaultView());
        }
        if (result.getView() != null && result.getView().getRoute() != null) {
            Map<String, String> inParams = result.getView().getRoute().parseParameters(path);
            for (Map.Entry<String, String> e : inParams.entrySet()) {
                result.getParametersList().add(new RouteResultParameter(e.getKey(), e.getValue()));
            }
        }
        return result;
    }

    public String getPathFor(String viewId, String[] parameters) {
        String url = null;

        View v = views.get(viewId);
        url = v.getRoute().getReversePath(parameters);

        return url;
    }

    public boolean existsView(String viewId) {
        return views.containsKey(viewId);
    }

}
