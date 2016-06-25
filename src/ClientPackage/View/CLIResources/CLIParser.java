package ClientPackage.View.CLIResources;

import ClientPackage.View.GeneralView.CLIView;
import Utilities.Class.AnnotationUtilities;
import Utilities.Class.CircularArrayList;
import Utilities.Class.MapUtil;
import Utilities.Exception.MethodNotFoundException;
import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.util.MultiMap;
import org.apache.commons.cli.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by Emanuele on 19/06/2016.
 */
public class CLIParser {

    private static CLIParser cliParserInstance;
    private Options currentOption = OptionsClass.getGameOption();
    private Class mClass;
    private HashMap<String,Method> methodHashMap = new HashMap<>();
    private HashMap<String,String> methodDescription = new HashMap<>();
    public CLIParser(Class mClass){
        this.mClass = mClass;
        initMethods();
    }

    private void initMethods() {
        for(Method method: mClass.getMethods()){
            for(Annotation annotation: method.getAnnotations()){
                if(annotation instanceof Command){
                    methodHashMap.put(((Command) annotation).abbrev(),method);
                    methodHashMap.put(((Command) annotation).name(),method);
                    String description =CLIColor.ANSI_RED+" short: "
                            +((Command) annotation).abbrev()+CLIColor.ANSI_RESET+"\n" +
                            "\t Description: "+((Command) annotation).description();
                    Annotation[][] annotations = method.getParameterAnnotations();
                    if(method.getParameterCount()>0)
                        description+=CLIColor.ANSI_GREEN+"\n \t Parameter\n"+CLIColor.ANSI_RESET;
                    for (Annotation[] ann : annotations) {
                        for(Annotation paramAnn: ann) {
                            if(paramAnn instanceof Param){
                                description+= " \t" +CLIColor.ANSI_CYAN+((Param) paramAnn).name()+" - "+ ((Param) paramAnn).description()+ CLIColor.ANSI_RESET+"\n";
                            }
                        }
                    }
                    methodDescription.put(((Command) annotation).name(),description);
                    //methodDescription.put(((Command) annotation).name(),description);
                }
            }
        }
    }


    public CommandLine retrieveCommandLine(String line) throws ParseException {
        String[] strings = line.split(" ");
        System.out.println("parsed" + strings.length);
        CommandLineParser commandLineParser = new DefaultParser();
            CommandLine commandLine = commandLineParser.parse(currentOption, strings);
            return commandLine;
    }

    void parseInput(String line, Object object, CLIPrinter cliPrinter) {

        String[] lines = line.split(" ");

        if(lines[0].equalsIgnoreCase("help") ){
            printHelp(lines);
        }
        else {
            Method method = methodHashMap.get(lines[0]);
            if(method!=null) {
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
            }
            else{
                cliPrinter.printError("Sorry, method not found");
            }
        }




        /*
        switch (lines[0]){
            case "status":
            case"st":
                matchCliController.showStatus();
                break;
            case "be":
            case"buildEmporium":
                if(lines.length>=2)
                    matchCliController.buildEmporium(lines[1]);
                else
                    cliPrinter.printError("INVALID INPUT");
                break;
            case "buildWithKing":
            case "bk":
                matchCliController.buildWithKing();
                break;
            case "buyPermit":
            case "bp":
                if(lines.length>=2)
                    matchCliController.buyPermit(lines[1]);
                else
                    cliPrinter.printError("INVALID INPUT");
                break;
            case "changePermit":
            case "cp":
                if(lines.length>1){
                    matchCliController.changePermitAction(lines[1]);
                }
                else
                    cliPrinter.printError("INVALID INPUT");
                break;
            case "sp":
            case "showPermit":
                matchCliController.showPermitCard();
                break;
            case "electCouncilor":
            case "ec":
                if(lines.length>3){
                    matchCliController.getElectCouncilorArgs(lines[1],lines[2],lines[3]);
                }
                else
                    cliPrinter.printError("INVALID INPUT");
                break;
        }
        */
    }

    private void printHelp(String[] lines) {

        if(lines.length<=1) {

            Map<String,String> tmpMap = MapUtil.sortByValue(methodDescription);
            for (Map.Entry<String, String> entry : tmpMap.entrySet()) {
                System.out.println(CLIColor.ANSI_BLUE + entry.getKey() + CLIColor.ANSI_RESET + " \n \t " + entry.getValue());
            }
        }
        else {
            if(methodDescription.containsKey(lines[1])) {
                String desc = methodDescription.get(lines[1]);
                System.out.println(desc);
            }
            else{
                System.out.println("Sorry, command not found");
            }

        }
    }

    public void printHelp(){
        String[] tmp = new String []{};
        printHelp(tmp);
    }
}
