package za.co.spsi.mdms.processor.processor;

import za.co.spsi.mdms.processor.ano.SummaryEntity;
import za.co.spsi.mdms.processor.ano.SummaryField;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.BufferedWriter;
import java.util.Set;

import static javax.lang.model.SourceVersion.RELEASE_8;

/**
 * Created by jaspervdb on 15/06/17.
 */
@SupportedAnnotationTypes({"za.co.spsi.mdms.processor.ano.SummaryEntity"})
@SupportedSourceVersion(RELEASE_8)
public class SummaryProcessor extends AbstractProcessor {

    public static final String TOTAL = "Total",AVERAGE = "Average",COUNT = "Count",MIN_DATE = "MinDate",MAX_DATE = "MaxDate",
            MIN_VALUE = "MinValue",MAX_VALUE = "MaxValue";

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
                    "@Data public class %s extends za.co.spsi.mdms.common.dao.AbstractMeterResultDataSummary {" +
                    "\n\n" +
                    "%s" +
                    "\n\n" +
                    "}";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        for (Element elem : roundEnv.getElementsAnnotatedWith(SummaryEntity.class)) {
            SummaryEntity entity = elem.getAnnotation(SummaryEntity.class);
            if (entity != null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "annotation found in " + elem.getSimpleName());
                try {
                    for (TypeElement e : annotations) {
                        String className = elem.getSimpleName() + "Summary";
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
                            if (el.getAnnotation(SummaryField.class) != null) {
                                fieldBuffer.append(String.format("\tprivate double %s%s = 0d;\n", el.getSimpleName().toString(),TOTAL));
                                fieldBuffer.append(String.format("\tprivate double %s%s = 0d;\n", el.getSimpleName().toString(),AVERAGE));
                                fieldBuffer.append(String.format("\tprivate int %s%s = 0;\n", el.getSimpleName().toString(),COUNT));

                                fieldBuffer.append(String.format("\tprivate Double %s%s = null;\n", el.getSimpleName().toString(),MIN_VALUE));
                                fieldBuffer.append(String.format("\tprivate Double %s%s = null;\n", el.getSimpleName().toString(),MAX_VALUE));

                                fieldBuffer.append(String.format("\tprivate java.util.Date %s%s;\n", el.getSimpleName().toString(),MIN_DATE));
                                fieldBuffer.append(String.format("\tprivate java.util.Date %s%s;\n\n", el.getSimpleName().toString(),MAX_DATE));
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

