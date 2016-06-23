package Utilities.Class;

import Utilities.Exception.MethodNotFoundException;
import asg.cliche.Command;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Emanuele on 21/06/2016.
 */
public class AnnotationUtilities {

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        Class<?> klass = type;
        while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
            // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
            final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));
            for (final Method method : allMethods) {
                if (method.isAnnotationPresent(annotation)) {
                    Annotation annotInstance = method.getAnnotation(annotation);
                    // TODO process annotInstance
                    methods.add(method);
                }
            }
            // move to the upper class in the hierarchy in search for more methods
            klass = klass.getSuperclass();
        }
        return methods;
    }

    public static Method filterMethod(List<Method> list, String line, Class<Command> commandClass) throws MethodNotFoundException {

        List<Method> toReturn = new ArrayList<>();
        for(Method method: list){
            for(Annotation annotation: method.getAnnotations()){
                if(annotation instanceof Command){
                    Command command = (Command) annotation;
                    if(command.abbrev().equalsIgnoreCase(line) || command.description().equalsIgnoreCase(line))
                        return method;
                }
            }
        }

        throw new MethodNotFoundException();
    }
}
