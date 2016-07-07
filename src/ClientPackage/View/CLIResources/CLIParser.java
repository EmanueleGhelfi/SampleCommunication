package ClientPackage.View.CLIResources;

import Utilities.Class.MapUtil;
import asg.cliche.Command;
import asg.cliche.Param;
import org.apache.commons.cli.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class CLIParser {

    private Class mClass;
    private HashMap<String, Method> methodHashMap = new HashMap<>();
    private HashMap<String, String> methodDescription = new HashMap<>();
    private HashMap<String, String> shortMethodDescription = new HashMap<>();

    public CLIParser(Class mClass) {
        this.mClass = mClass;
        initMethods();
    }

    private void initMethods() {
        for (Method method : mClass.getMethods()) {
            for (Annotation annotation : method.getAnnotations()) {
                if (annotation instanceof Command) {
                    methodHashMap.put(((Command) annotation).abbrev(), method);
                    methodHashMap.put(((Command) annotation).name(), method);
                    String description = CLIColor.ANSI_RED + " short: "
                            + ((Command) annotation).abbrev() + CLIColor.ANSI_RESET + "\n" +
                            "\t Description: " + ((Command) annotation).description();
                    Annotation[][] annotations = method.getParameterAnnotations();
                    if (method.getParameterCount() > 0)
                        description += CLIColor.ANSI_GREEN + "\n \t Parameter\n" + CLIColor.ANSI_RESET;
                    for (Annotation[] ann : annotations) {
                        for (Annotation paramAnn : ann) {
                            if (paramAnn instanceof Param) {
                                description += " \t" + CLIColor.ANSI_CYAN + ((Param) paramAnn).name() + " - " + ((Param) paramAnn).description() + CLIColor.ANSI_RESET + "\n";
                            }
                        }
                    }
                    methodDescription.put(((Command) annotation).name(), description);
                    shortMethodDescription.put(((Command) annotation).abbrev(), description);
                }
            }
        }
    }

    void parseInput(String line, Object object, CLIPrinter cliPrinter) {

        String[] lines = line.split(" ");

        if (lines[0].equalsIgnoreCase("help")) {
            printHelp(lines);
        } else {
            Method method = methodHashMap.get(lines[0]);
            if (method != null) {
                try {
                    if (method.getParameterCount() != lines.length - 1)
                        System.out.println("Check parameter number!");
                    else {
                        Object[] objects = new Object[lines.length - 1];
                        System.arraycopy(lines, 1, objects, 0, lines.length - 1);
                        method.invoke(object, objects);
                    }

                } catch (InvocationTargetException e) {
                    System.out.println(e.getMessage());
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                cliPrinter.printError("Sorry, method not found");
            }
        }
    }

    private void printHelp(String[] lines) {

        if (lines.length <= 1) {

            Map<String, String> tmpMap = MapUtil.sortByValue(methodDescription);
            for (Map.Entry<String, String> entry : tmpMap.entrySet()) {
                System.out.println(CLIColor.ANSI_BLUE + entry.getKey() + CLIColor.ANSI_RESET + " \n \t " + entry.getValue());
            }
        } else {
            if (methodDescription.containsKey(lines[1])) {
                String desc = methodDescription.get(lines[1]);
                System.out.println(desc);
            } else {
                if (shortMethodDescription.containsKey(lines[1])) {
                    String desc = shortMethodDescription.get(lines[1]);
                    System.out.println(desc);
                } else {
                    System.out.println("Sorry, command not found");
                }
            }

        }
    }

    public void printHelp() {
        String[] tmp = new String[]{};
        printHelp(tmp);
    }
}
