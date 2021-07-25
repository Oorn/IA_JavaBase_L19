package com.company;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.TreeSet;

@SupportedSourceVersion(SourceVersion.RELEASE_11)
@SupportedAnnotationTypes("com.company.SignCode")
public class SignCodeProcessor extends AbstractProcessor {
    TreeSet<Long> idDatabase = new TreeSet<>();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "SignCodeProcessor is actually running!");
        for(TypeElement annotation: annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            annotatedElements.forEach((elem) -> {
                SignCode thisAnnotation = elem.getAnnotation(SignCode.class);
                if (!kindContainsKind(thisAnnotation.kind(), elem.getKind()))
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "@SignCode type mismatch (" + elem.getKind() + " is not a " + thisAnnotation.kind() + ") ",
                            elem);
                //wrong type error

                if (idDatabase.contains(thisAnnotation.id()))
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            "@SignCode id is not unique",
                            elem);
                    //non-unique id error

                idDatabase.add(thisAnnotation.id());
            });
        }
        return true;
    }
    boolean kindContainsKind(ElementKind kOuter, ElementKind kInner) {
        switch (kOuter) {
            case CLASS:
                switch (kInner) {
                    case CLASS:
                    case ENUM:
                        return true;
                }
                break;
            case FIELD:
                switch (kInner) {
                    case FIELD:
                    case ENUM_CONSTANT:
                        return true;
                }
                break;
            case INTERFACE:
                switch (kInner) {
                    case INTERFACE:
                    case ANNOTATION_TYPE:
                        return true;
                }
                break;
            default:
                if (kOuter == kInner)
                    return true;
        }
        return false;
    }
    /* boolean typeMatchesKind(ElementType t, ElementKind k) {
        switch (t) {
            case ANNOTATION_TYPE:
                if (k == ElementKind.ANNOTATION_TYPE)
                    return true;
                break;
            case CONSTRUCTOR:
                if (k == ElementKind.CONSTRUCTOR)
                    return true;
                break;
            case FIELD:
                if (k == ElementKind.FIELD)
                    return true;
                break;
            case LOCAL_VARIABLE:
                if (k == ElementKind.LOCAL_VARIABLE)
                    return true;
                break;
            case METHOD:
                if (k == ElementKind.METHOD)
                    return true;
                break;
            case PACKAGE:
                if (k == ElementKind.PACKAGE)
                    return true;
                break;
            case PARAMETER:
                if (k == ElementKind.PARAMETER)
                    return true;
                break;
            case TYPE_PARAMETER:
                if (k == ElementKind.TYPE_PARAMETER)
                    return true;
                break;
            case TYPE:
                switch(k) {
                    case CLASS:
                    case INTERFACE:
                    case ANNOTATION_TYPE:
                    case ENUM:
                        return true;
                    default:
                }
                break;
            case TYPE_USE:
                return true;
        }
        return false;
    }*/
    public static void main(String[] args) { }
}
