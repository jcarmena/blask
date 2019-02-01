package blask.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RouteResult implements Serializable {

    private static final long serialVersionUID = -6357254723597912811L;
    private Boolean matched = false;
    private View view;
    private List<RouteResultParameter> parameters = new ArrayList<RouteResultParameter>();

    public Boolean getMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public List<RouteResultParameter> getParametersList() {
        return parameters;
    }

}
