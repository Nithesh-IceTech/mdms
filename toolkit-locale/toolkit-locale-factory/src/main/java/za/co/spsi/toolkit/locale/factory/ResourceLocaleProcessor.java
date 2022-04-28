package za.co.spsi.toolkit.locale.factory;

import za.co.spsi.locale.annotation.ResourceLocale;
import za.co.spsi.toolkit.util.Assert;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Set;

/**
 * Created by jaspervdb on 15/06/17.
 */
@SupportedAnnotationTypes({"za.co.spsi.locale.annotation.ResourceLocale"})
//@AutoService(Processor.class)
public class ResourceLocaleProcessor extends AbstractProcessor {

    public static final String TEMPLATE = "\n" +
            "import za.co.spsi.toolkit.locale.factory.LocaleHelper;\nimport za.co.spsi.toolkit.locale.factory.LocaleResource;\npublic class %s {\n" +
            "\n" +
            "%s\n" +
            "\n" +
            "    static {\n" +
            "%s\n" +
            "    }\n" +
            "}";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = processingEnv.getMessager();
        ResourceLocale resourceLocale = null;
        for (Element elem : roundEnv.getElementsAnnotatedWith(ResourceLocale.class)) {
            resourceLocale = elem.getAnnotation(ResourceLocale.class);
            if (resourceLocale != null) {
                String message = "annotation found in " + elem.getSimpleName()
                        + " with complexity " + resourceLocale.resources();

                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, message);
                try {
                    for (TypeElement e : annotations) {
                        String className = elem.getSimpleName() + "Id";
                        messager.printMessage(Diagnostic.Kind.NOTE, "Building: " + className);
                        TypeElement classElement = (TypeElement) e;
                        PackageElement packageElement =
                                (PackageElement) classElement.getEnclosingElement();

                        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(className);

                        BufferedWriter bw = new BufferedWriter(jfo.openWriter());
                        if (packageElement.getQualifiedName().length() > 0) {
                            bw.append("package ");
                            bw.append(packageElement.getQualifiedName());
                            bw.append(";");
                        }
                        bw.newLine();
                        bw.newLine();

                        messager.printMessage(Diagnostic.Kind.NOTE, "Created : " + jfo.getName());

                        LocaleResourceList localeResourceList = new LocaleResourceList();
                        Assert.isTrue(resourceLocale.locales().length == resourceLocale.resources().length, "Resource files and locales length does not match");
                        for (int x = 0; x < resourceLocale.locales().length; x++) {
                            for (int y = 0; y < resourceLocale.resources()[x].resources().length; y++) {
                                for (int z = 0; z < resourceLocale.context().length; z++) {
                                    String resourceName = resourceLocale.resources()[x].resources()[y];
                                    String localeName = resourceLocale.locales()[x];
                                    String contextName = resourceLocale.context()[z];

                                    if (resourceName.indexOf("-") != -1) {
                                        contextName = resourceName.substring(resourceName.indexOf("-")+1, resourceName.lastIndexOf("."));
                                    }

                                    FileObject resource = processingEnv.getFiler().getResource(
                                            StandardLocation.CLASS_PATH, "", resourceName);
                                    Assert.notNull(resource, String.format("Could not load resources file %s", resourceName));
                                    Properties properties = new Properties();
                                    BufferedReader in = new BufferedReader(new InputStreamReader(resource.openInputStream(), "UTF8"));
                                    properties.load(in);
                                    for (Object key : properties.keySet()) {
                                        Assert.isTrue(key.toString().indexOf(" ") == -1, String.format("Key name %s in %s may not contain any spaces", key, resourceName));
                                        String oldKeyName = key.toString();
                                        key = key.toString().replace(".", "_");
                                        if (!localeResourceList.contains(key.toString())) {
                                            localeResourceList.add(new LocaleResource(key.toString()));
                                        }
                                        localeResourceList.getByName(key.toString()).addLocale(localeName, contextName, properties.getProperty(oldKeyName.toString()));
                                    }
                                }
                            }
                        }
                        // load and populate the template
//                        FileObject resource = processingEnv.getFiler().getResource(StandardLocation.CLASS_PATH, "", "locale/locale_template.txt");
//                        java.util.Scanner s = new java.util.Scanner(resource.openInputStream()).useDelimiter("\\A");
//                        String template = s.next();
                        bw.write(String.format(TEMPLATE, className, localeResourceList.getFieldDeclaration(), localeResourceList.getStaticInit()));
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

