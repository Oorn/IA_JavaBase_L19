
package com.company;



import jdk.jfr.Description;

import javax.lang.model.element.ElementKind;
import javax.sound.midi.spi.SoundbankReader;
import java.io.*;

import java.lang.reflect.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HW19 {
    @SignCode(kind = ElementKind.CLASS, id = 0L)
    public static class ExceptionTaskNumber extends Exception {
        ExceptionTaskNumber() {
            super("Invalid task number");
        }
    }

    @SignCode(kind = ElementKind.CLASS, id = 1L)
    public static class ExceptionNegative extends Exception {
        ExceptionNegative() {
            super("value cannot be negative");
        }
    }


    public static void main(String[] args) {
        Random ran = new Random();
        Scanner in = new Scanner(System.in);

        //Logger log = Logger.getLogger(HW11.class.getName());
        Logger log = Logger.getAnonymousLogger();
        log.setUseParentHandlers(false);
        log.setLevel(Level.FINER); //level at which throwing is logged
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINER);
        log.addHandler(handler);
        //SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            //1
            printClassInfo(House.class);
            System.out.println();

            System.out.println(createClassInstance(House.class));
            System.out.println();

            House h1 = new House(5, 20, 10, "houseInstance");
            printObjectFields(h1);
            System.out.println();

            callAllMethods(h1);
            System.out.println();
            System.out.println(h1);
            System.out.println();

            Person.getSignatures();



        } catch (InputMismatchException ex) {
            System.out.println("Error - " + ex);
        } finally {
            in.close();
        }
    }

    static final int DEFAULT_INT = 0;
    static final String DEFAULT_STRING = "defaultString";

    static void printClassInfo(Class<?> clazz) {
        System.out.println("Class: " + clazz.getCanonicalName());
        System.out.print("Constructors: ");
        Arrays.stream(clazz.getDeclaredConstructors()).forEach((cons) -> {
            System.out.print(cons + " | ");
        });
        System.out.println();
        System.out.print("Fields: ");
        Arrays.stream(clazz.getDeclaredFields()).forEach((field) -> {
            System.out.print(field + " | ");
        });
        System.out.println();
        System.out.print("Methods: ");
        Arrays.stream(clazz.getDeclaredMethods()).forEach((field) -> {
            System.out.print(field + " | ");
        });
        System.out.println();
    }
    static <T> T createClassInstance(Class<T> clazz) {
        Constructor<?> cons = clazz.getDeclaredConstructors()[0];
        Class<?>[] params = cons.getParameterTypes();
        Object[] actualParams  = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (params[i] == (int.class))
                actualParams[i] = DEFAULT_INT;
            else if (params[i] == (String.class))
                actualParams[i] = DEFAULT_STRING;
            else
                try {
                    actualParams[i] = params[i].getDeclaredConstructor().newInstance();
                }
                catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                    System.out.println("we should not be reaching this branch");
                    actualParams[i] = null;
                }

        }
        try {
            return (T) cons.newInstance(actualParams);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException ignored) {}

        return null;
    }
    static void printObjectFields(Object o) {
        Class<?> clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            boolean oldAccess = fields[i].canAccess(o);
            if (!oldAccess)
                fields[i].setAccessible(true);
            try {
                System.out.println(fields[i] + " = " + fields[i].get(o));
            } catch (IllegalAccessException ex) {System.out.println("access error which should never happen");}
            if (!oldAccess)
                fields[i].setAccessible(false);
        }

    }
    static <T> void callAllMethods(T obj) {
        Class<?> clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        Class<?>[] params;
        Object[] actualParams;

        for (int i = 0; i < methods.length; i++) {
            boolean oldAccess = methods[i].canAccess(obj);
            if (!oldAccess)
                methods[i].setAccessible(true);

            params = methods[i].getParameterTypes();
            actualParams  = new Object[params.length];
            for (int j = 0; j < params.length; j++) {
                if (params[j] == (int.class))
                    actualParams[j] = DEFAULT_INT;
                else if (params[j] == (String.class))
                    actualParams[j] = DEFAULT_STRING;
                else
                    try {
                        actualParams[j] = params[j].getDeclaredConstructor().newInstance();
                    }
                    catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
                        System.out.println("we should not be reaching this branch");
                        actualParams[j] = null;
                    }

            }
            System.out.print("executing method " + methods[i] + " with params " + Arrays.toString(actualParams) + "; result = ");
            try {
                System.out.println(methods[i].invoke(obj, actualParams));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                System.out.println(ex);
            }

            if (!oldAccess)
                methods[i].setAccessible(false);

        }
    }


}