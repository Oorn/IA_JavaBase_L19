package com.company;

import javax.lang.model.element.ElementKind;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

@SignCode(kind = ElementKind.CLASS, id = 2L)
public class Person {
    @SignCode(kind = ElementKind.FIELD, id = 3L)
    String surname;
    @SignCode(kind = ElementKind.FIELD, id = 4L)
    String name;
    @SignCode(kind = ElementKind.FIELD, id = 5L)
    int birthYear;

    @SignCode(kind = ElementKind.CONSTRUCTOR, id = 6L)
    public Person(
            @SignCode(kind = ElementKind.PARAMETER, id = 7L)
            String surname,
            @SignCode(kind = ElementKind.PARAMETER, id = 8L)
            String name,
            @SignCode(kind = ElementKind.PARAMETER, id = 9L)
            int birthYear) {
        this.surname = surname;
        this.name = name;
        this.birthYear = birthYear;
    }

    @Override
    @SignCode(kind = ElementKind.METHOD, id = 10L)
    public String toString() {
        return "Person{" +
                "surname='" + surname + '\'' +
                ", name='" + name + '\'' +
                ", birthYear=" + birthYear +
                '}';
    }

    @SignCode(kind = ElementKind.METHOD, id = 11L)
    public static void getSignatures() {
        class SigChecker<T extends AnnotatedElement> implements Consumer<T> {
            Object prefix = null;
            SigChecker(){}
            SigChecker(Object prefix) {
                this.prefix = prefix;
            }
            @Override
            public void accept(T t) {
                SignCode localAnno = t.getAnnotation(SignCode.class);
                if (localAnno != null) {
                    if (prefix != null)
                        System.out.print(prefix + ": ");
                    System.out.println(t + " is signed " + localAnno);
                }
            }
        }

        Stream.of(Person.class.getPackage()).forEach(new SigChecker<>());
        Stream.of(Person.class).forEach(new SigChecker<>());
        Arrays.stream(Person.class.getDeclaredFields()).forEach(new SigChecker<>());
        Arrays.stream(Person.class.getDeclaredConstructors()).peek(new SigChecker<>()).forEach((o) ->
            Arrays.stream(o.getParameters()).forEach(new SigChecker<>(o)));
        Arrays.stream(Person.class.getDeclaredMethods()).peek(new SigChecker<>()).forEach((o) ->
            Arrays.stream(o.getParameters()).forEach(new SigChecker<>(o)));

    }
}
