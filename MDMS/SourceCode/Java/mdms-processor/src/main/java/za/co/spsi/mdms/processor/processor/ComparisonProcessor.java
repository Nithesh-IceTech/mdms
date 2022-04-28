package za.co.spsi.mdms.processor.processor;

import za.co.spsi.mdms.processor.ano.Comparison;
import za.co.spsi.mdms.processor.ano.ComparisonField;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.util.Set;

/**
 * Created by jaspervdb on 15/06/17.
 */
@SupportedAnnotationTypes({"za.co.spsi.mdms.processor.ano.Comparison"})
public class ComparisonProcessor extends AbstractProcessor {

    public static final String PRIMARY = "Primary",COMPARED = "Compared";
    public static final String TEMPLATE =
            "import lombok.AccessLevel;\n" +
                    "import lombok.Data;\n" +
                    "import lombok.Getter;\n" +
                    "import lombok.Setter;\n" +
                    "import za.co.spsi.mdms.processor.ano.ComparisonField;\n" +
                    "import za.co.spsi.toolkit.entity.Field;\n" +
                    "import za.co.spsi.toolkit.reflect.RefFields;\n" +
                    "\n" +
                    "import java.sql.Timestamp;\n" +
                    "\n" +
                    "@Data public class %s {" +
                    "\n" +
                    "%s" +
                    "\n" +
                    "}";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element elem : roundEnv.getElementsAnnotatedWith(Comparison.class)) {
            Comparison entity = elem.getAnnotation(Comparison.class);
            if (entity != null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "annotation found in " + elem.getSimpleName());
                try {
                    for (TypeElement e : annotations) {
                        String className = elem.getSimpleName() + "Cmp";
                        messager.printMessage(Diagnostic.Kind.NOTE, "Building: " + className);
                        PackageElement packageElement =
                                (PackageElement) e.getEnclosingElement();

                        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(className);

                        BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                        if (packageElement.getQualifiedName().length() > 0) {
                            bw.append("package ");
                            bw.append(packageElement.getQualifiedName());
                            bw.append(";");
                        }
                        bw.newLine();
                        bw.newLine();

                        StringBuffer fieldBuffer = new StringBuffer();

                        elem.getEnclosedElements().stream().forEach(el -> {
                            if (el.getAnnotation(ComparisonField.class) != null) {
                                fieldBuffer.append(String.format("private %s %s%s;\n", el.asType(), el.getSimpleName().toString(),PRIMARY));
                                fieldBuffer.append(String.format("private %s %s%s;\n", el.asType(), el.getSimpleName().toString(),COMPARED));
                            }
                        });

                        // add all the fields

                        messager.printMessage(Diagnostic.Kind.NOTE, "Created : " + jfo.getName());
                        // get the variables
                        bw.write(String.format(TEMPLATE, className, fieldBuffer.toString()));
                        bw.close();

                    }
                } catch (Exception ex) {
                    messager.printMessage(Diagnostic.Kind.ERROR, "Printing: " + ex.toString());
                    return true;
                }
            }
        }
        return true;
    }
}

