package za.co.spsi.toolkit.crud.util;

import org.json.JSONObject;

import java.io.*;

public class GitVersionUtil {

    public String getVersion() {
        try {
            JSONObject jsonObject = new JSONObject(readGitProperties());
            return jsonObject.getString("git.build.version");
        } catch (FileNotFoundException ioex) {
            return "1.0-SNAPSHOT";
        }
    }

    private String readGitProperties() throws FileNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("git.properties");
        try {
            if (inputStream == null) {
                throw new FileNotFoundException("File not found");
            }
            return readFromInputStream(inputStream);
        } catch (FileNotFoundException fne) {
            throw fne;

        } catch (IOException e) {
            return "Version information could not be retrieved";
        }
    }

    private String readFromInputStream(InputStream inputStream) throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
