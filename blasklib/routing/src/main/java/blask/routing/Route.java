package blask.routing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// ================================
// Route
// ================================

public class Route implements Serializable {

    private static final long serialVersionUID = 3943167976531754822L;
    private static final String PARAM_REGEX = "<([^>:]+)(:(.+))?>$";
    private static final Pattern compiledParamRegex = Pattern.compile(PARAM_REGEX);
    private static final String REGEX_PATTERN_REGEX = "\\[([^/:]+):(.+)\\]$";
    private static final Pattern compiledRegexPatternRegex = Pattern.compile(REGEX_PATTERN_REGEX);
    private static final String URL_SPLIT_REGEX = "([^/]+)";
    private static final Pattern compiledUrlSplitRegex = Pattern.compile(URL_SPLIT_REGEX);

    private String method = null;
    private String pattern = null;
    private String viewId = null;
    private List<ViewParameter> reverseParameters = new ArrayList<ViewParameter>();
    private List<ViewParameter> inputParameters = new ArrayList<ViewParameter>();
    private String effectivePattern = null;
    private Pattern compiledEffectiveRegex = null;
    private List<String> reverseTemplate = new ArrayList<String>();

    public Route() {
    }

    public Route(String method, String pattern, String viewId) {
        this.setPattern(pattern);
        this.setMethod(method);
        this.setViewId(viewId);
    }

    String getEffectivePattern() {
        return this.effectivePattern;
    }

    Pattern getCompiledEffectivePattern() {
        return this.compiledEffectiveRegex;
    }

    public String getPattern() {
        return this.pattern;
    }

    public List<ViewParameter> getInputParameters() {
        return this.inputParameters;
    }

    public List<ViewParameter> getReverseParameters() {
        return this.reverseParameters;
    }

    public void setPattern(String pattern) {
        if (pattern == null | "".equals(pattern)) {
            throw new IllegalArgumentException("URI pattern cannot be empty nor null");
        }

        this.pattern = pattern;

        Matcher m = compiledUrlSplitRegex.matcher(pattern);
        StringJoiner joiner = new StringJoiner("/");
        int pos = 0;
        while (m.find()) {
            String fragment = m.group(1);
            Matcher paramMatcher = compiledParamRegex.matcher(fragment);
            if (paramMatcher.matches()) { // is a parameter
                paramMatcher.reset();
                paramMatcher.find();
                String paramName = paramMatcher.group(1).trim();
                String paramRegex = paramMatcher.group(3);
                if (paramRegex == null || "".equals(paramRegex)) {
                    paramRegex = "[^/]+";
                }
                joiner.add(paramRegex);
                ViewParameter param = new ViewParameter(paramName, paramRegex, pos);
                this.getInputParameters().add(param);
                this.getReverseParameters().add(param);
                reverseTemplate.add("");
            } else {
                Matcher regexMatcher = compiledRegexPatternRegex.matcher(fragment);
                if (regexMatcher.matches()) { // is a regex
                    regexMatcher.reset();
                    regexMatcher.find();
                    String paramName = regexMatcher.group(1).trim();
                    String paramRegex = regexMatcher.group(2);
                    joiner.add(paramRegex);
                    ViewParameter param = new ViewParameter(paramName, paramRegex, pos);
                    this.getReverseParameters().add(param);
                    reverseTemplate.add("");
                } else {
                    joiner.add(fragment);
                    reverseTemplate.add(fragment);
                }
            }
            pos++;
        }
        this.effectivePattern = joiner.toString();
        this.compiledEffectiveRegex = Pattern.compile(this.effectivePattern);
    }

    public String getViewId() {
        return this.viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public String getMethod() {
        return this.method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getReversePath(Map<String, String> parameters) {
        String result = null;

        ArrayList<String> aux = new ArrayList<String>();
        aux.addAll(reverseTemplate);
        for (ViewParameter v : reverseParameters) {
            if (parameters.containsKey(v.getName())) {
                aux.set(v.getPosition(), parameters.get(v.getName()));
            }
        }
        result = String.join("/", aux);

        return result;
    }

    public String getReversePath(String[] parameters) {
        String result = null;

        ArrayList<String> aux = new ArrayList<String>();
        aux.addAll(reverseTemplate);
        int i = 0;
        for (ViewParameter v : reverseParameters) {
            aux.set(v.getPosition(), parameters[i]);
            i++;
        }
        result = String.join("/", aux);

        return result;
    }

    public Map<String, String> parseParameters(String url) {
        Map<String, String> result = new HashMap<String, String>();

        Matcher m = compiledUrlSplitRegex.matcher(url);
        int i = 0;
        for (ViewParameter p : inputParameters) {
            while (m.find()) {
                if (i == p.getPosition()) {
                    String fragment = m.group(1);
                    result.put(p.getName(), fragment);
                    i++;
                    break;
                } else {
                    i++;
                }
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Route)) {
            return false;
        }
        Route route = (Route) o;
        return Objects.equals(effectivePattern, route.effectivePattern) && Objects.equals(method, route.method)
                && Objects.equals(pattern, route.pattern) && Objects.equals(inputParameters, route.inputParameters)
                && Objects.equals(viewId, route.viewId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(effectivePattern, method, pattern, inputParameters, viewId);
    }

    @Override
    public String toString() {
        return "{" + " effectivePattern='" + getEffectivePattern() + "'" + ", method='" + getMethod() + "'"
                + ", pattern='" + getPattern() + "'" + ", viewId='" + getViewId() + "'" + ", parameters='"
                + getInputParameters() + "'" + "}";
    }

}
