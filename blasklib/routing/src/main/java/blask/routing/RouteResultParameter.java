package blask.routing;

import java.io.Serializable;

public class RouteResultParameter implements Serializable {

        private static final long serialVersionUID = 6249732602341540699L;
        private String name;
        private String value;
    
        public RouteResultParameter() {
    
        }
    
        public RouteResultParameter(String name, String value) {
            this.setName(name);
            this.setValue(value);
        }
    
        public String getName() {
            return name;
        }
    
        public void setName(String name) {
            this.name = name;
        }
    
        public String getValue() {
            return value;
        }
    
        public void setValue(String value) {
            this.value = value;
        }
    }