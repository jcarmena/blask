package blask.routing;

import java.io.Serializable;

// ================================
    // ViewParameter
    // ================================

public class ViewParameter implements Serializable {

    private static final long serialVersionUID = 6249732602341540699L;
    private String name;
    private String regex;
    private int position;

    public ViewParameter() {

    }

    public ViewParameter(String name, String regex, int position) {
        this.setName(name);
        this.setRegex(regex);
        this.setPosition(position);
    }

    public String getName() {
        return name;
    }

    public ViewParameter setName(String name) {
        this.name = name;
        return this;
    }

    public String getRegex() {
        return regex;
    }

    public ViewParameter setRegex(String regex) {
        this.regex = regex;
        return this;
    }

    public ViewParameter setPosition(int position) {
        this.position = position;
        return this;
    }

    public int getPosition() {
        return this.position;
    }
}