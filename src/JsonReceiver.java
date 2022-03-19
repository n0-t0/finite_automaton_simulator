import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonReceiver {
    private Set<String> state;
    private String start_state;
    private Set<String> set_of_accept_states;
    private Set<String> alphabet;
    private Map<String, Map<String, String>> transition_function;

    public Set<String> getState() {
        return state;
    }
    public String getStart_state() {
        return start_state;
    }
    public Set<String> getSet_of_accept_states() {
        return set_of_accept_states;
    }
    public Set<String> getAlphabet() {
        return alphabet;
    }
    public Map<String, Map<String, String>> getTransition_function() {
        return transition_function;
    }
    public void setState(Set<String> state) {
        this.state = state;
    }
    public void setStart_state(String start_state) {
        this.start_state = start_state;
    }public void setSet_of_accept_states(Set<String> set_of_accept_states) {
        this.set_of_accept_states = set_of_accept_states;
    }
    public void setAlphabet(Set<String> alphabet) {
        this.alphabet = alphabet;
    }
    public void setTransition_function(Map<String, Map<String, String>>transition_function) {
        this.transition_function = transition_function;
    }
}
