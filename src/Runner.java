// I/O classes needed to read and write files
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

// language introspection/metaprogramming classes
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

// resource locator helpers
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;

// more metaprogramming tools
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

// the crux of the application -- a polished, upgraded version of
// the Runner class in my ICS3U Code Review project, "Java in 10".
// Allows for arbitrary execution of Java code, specifically testing
// a method of known signature, using Java metaprogramming tools.
// Errors from bad user input are reported gracefully with special
// enum flags.
public class Runner {
    public enum Err {
        COMPILE_TIME_ERROR,
        PROGRAM_ENTRY_ERROR,
        RUNTIME_ERROR
    }

    // instance variables that locate the method to be tested
    private final String folder;
    private final String className;
    private final String fullPath;
    private final String entryMethodName;

    // used to report the latest runtime exception (see method below)
    private Throwable latestRuntimeException;

    public Runner(String folderPath, String className, String entryMethodName) {
        folder = folderPath;

        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.className = className;
        fullPath = folder + className + ".java";
        this.entryMethodName = entryMethodName;
    }

    // step 1 of running arbitrary code: compile it to a .class file
    private int compile(String code) throws FileNotFoundException {
        // write the code into a .java file, like you would when coding manually
        PrintWriter writer = new PrintWriter(fullPath);
        writer.println(code);
        writer.close();

        // compile said file with default system streams
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        return compiler.run(System.in, System.out, System.err, fullPath);
    }

    // step 2: run the compiled file; any compile-time error is also reported here
    public Object run(String code, Class<?>[] types, Object[] args) {
        try {
            // if anything goes wrong while compiling (success result would be 0), flag that
            if (compile(code) != 0) {
                return Err.COMPILE_TIME_ERROR;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // access the folder we've specified when initializing a Runner object
        File directory = new File(folder);
        Method entry;

        try {
            // try to access the specified class in that folder,
            // then the specified method with specified parameter types
            Class<?> cls = Class.forName(className, true,
                URLClassLoader.newInstance(new URL[] { directory.toURI().toURL() }));
            entry = cls.getMethod(entryMethodName, types);
        } catch (Exception e) {
            // if we can't find the method, something's wrong with its
            // signature, or the user disobeyed other instructions; flag that
            return Err.PROGRAM_ENTRY_ERROR;
        }

        // if the method is found but is not static, flag that too
        if (!Modifier.isStatic(entry.getModifiers())) {
            return Err.PROGRAM_ENTRY_ERROR;
        }

        try {
            // finally, try to call the method with
            // arguments provided and return its result
            return entry.invoke(null, args);
        } catch (Exception e) {
            // if anything goes wrong here, it's a runtime exception;
            // flag and store that in storage variable to defer reporting
            latestRuntimeException = e.getCause();
            return Err.RUNTIME_ERROR;
        }
    }

    public String getLatestRuntimeExceptionName() {
        return latestRuntimeException.getClass().getName();
    }
}
