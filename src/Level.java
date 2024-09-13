import java.util.HashMap;
import java.util.Map;

// simple storage class used to bundle level data for easy access
public class Level {
    // each level must have a problem description, its solution
    // method's required signature, its solution method's parameter
    // types (but programmatically this time, not just as a string),
    // and a set of test cases (which is a mapping of test input, as
    // an array of arguments, to expected output, as a single object)
    public String desc;
    public String signature;
    public Class<?>[] parameterTypes;
    public Map<Object[], Object> cases;

    // each level must also have an official solution
    public String solution;

    // each level may optionally have a hint that the user can access
    public String hint = null;

    public Level(String desc, String signature, Class<?>[] parameterTypes, String solution) {
        this.desc = desc;
        this.signature = signature;
        this.parameterTypes = parameterTypes;
        cases = new HashMap<>();

        this.solution = solution;
    }
}
