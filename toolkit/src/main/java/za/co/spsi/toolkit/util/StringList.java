package za.co.spsi.toolkit.util;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by jaspervdb on 3/9/16.
 */
public class StringList extends ArrayList<String> {

    public StringList() {
    }

    public StringList(String... values) {
        Collections.addAll(this, values);
    }


    public StringList(Collection<String> values) {
        if (values != null) {
            addAll(values);
        }
    }

    public StringList(Iterable<String> values) {
        this(values.iterator());
    }

    public StringList(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    public StringList readLines(InputStream is) {
        try {
            try (LineNumberReader reader = new LineNumberReader(new InputStreamReader(is))) {
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    add(line);
                }
            }
            return this;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public StringList readLines(String source) {
        return readLines(new ByteArrayInputStream(source.getBytes()));
    }

    public StringList readFile(File file) {
        try {
            try (FileInputStream fis = new FileInputStream(file)) {
                return readLines(fis);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public StringList(ResultSet rs, String colName) throws SQLException {
        while (rs.next()) {
            if ("*".equals(colName)) {
                StringList values = new StringList();
                for (int i =0;i < rs.getMetaData().getColumnCount();i++) {
                    values.add(rs.getMetaData().getColumnName(i+1)+"=" + rs.getString(i+1));
                }
                add(values.toString(","));
            } else {
                add(rs.getString(colName));
            }
        }
    }

    public StringList prepend(String value) {
        for (int i = 0; i < size(); i++) {
            set(i, value + get(i));
        }
        return this;
    }

    public StringList removeNull() {
        for (int i = 0; i < size(); i++) {
            if (get(i) == null) {
                remove(i--);
            }
        }
        return this;
    }

    public StringList(String list, String delimiter) {
        for (String value : list.split(delimiter)) {
            add(value);
        }
    }

    public StringList toUpperCase() {
        for (int i = 0; i < size(); i++) {
            set(i, get(i).toUpperCase());
        }
        return this;
    }

    public StringList append(String value) {
        for (int i = 0; i < size(); i++) {
            set(i, get(i) + value);
        }
        return this;
    }


    public StringList removeDuplicates() {
        for (int i = 0; i < size(); i++) {
            String value = remove(i);
            while (contains(value)) {
                remove(indexOf(value));
            }
            add(i, value);
        }
        return this;
    }

    /**
     * This will remove all leading (front) and trailing spaces
     *
     * @param front if true will remove all leading spaces
     * @return applying trim and return a reference to self
     */
    public StringList trim(boolean front) {
        for (int i = 0; i < size(); i++) {
            for (; front && get(i).startsWith(" "); set(i, get(i).substring(1))) {
            }
            set(i, get(i).trim());
        }
        return this;
    }


    public String toString(String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String value : this) {
            sb.append(value + delimiter);
        }
        if (sb.length() > 0) {
            sb.delete(sb.length() - delimiter.length(), sb.length());
        }
        return sb.toString();
    }

    public String toString() {
        return toString(",");
    }

    public int getIndexOfIgnoreCase(String value) {
        int i = 0;
        for (String s : this) {
            if (s.equalsIgnoreCase(value)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public boolean containsIgnoreCase(String value) {
        return getIndexOfIgnoreCase(value) != -1;
    }

    public boolean matchAnyOrderIgnoreCase(StringList values) {
        if (size() == values.size()) {
            for (String value : this) {
                if (!values.containsIgnoreCase(value)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * if value contains any of the items in this
     *
     * @param value
     * @return
     */
    public int getIndexOfAnyPartOf(String value) {
        for (int i = 0; i < size(); i++) {
            if (value.contains(get(i))) {
                return i;
            }
        }
        return -1;
    }


    /**
     * @param str
     * @return index of the list which contains the str
     */
    public int indexOfInsideIgnoreCase(String str) {
        for (int i = 0; i < size(); i++) {
            if (get(i).toLowerCase().indexOf(str.toLowerCase()) != -1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param str
     * @return index of the list which contains the str
     */
    public int indexInsideOfIgnoreCase(String str) {
        for (int i = 0; i < size(); i++) {
            if (str.toLowerCase().indexOf(get(i).toLowerCase()) != -1) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Index of a string that matches str ignoring the case of the string
     *
     * @param str String to test against
     * @retrun index of string that equals str
     */
    public int indexOfIgnoreCase(String str) {
        for (int i = 0; i < size(); i++) {
            if (get(i).equalsIgnoreCase(str)) {
                return i;
            }
        }
        return -1;
    }

    public StringList getIgnoreCaseAsList(String value) {
        StringList sl = new StringList();
        for (String s : this) {
            if (s.equalsIgnoreCase(value)) {
                sl.add(s);
            }
        }
        return sl;
    }

    public void removeAllIgnoreCase(List<String> values) {
        for (String value : values) {
            if (indexOfIgnoreCase(value) != -1) {
                remove(indexOfIgnoreCase(value));
            }
        }
    }

    public String[] toStringArray() {
        return toArray(new String[]{});
    }

    public StringList substring(int index) {
        StringList strings = new StringList();
        for (int i = index; i < size(); i++) {
            strings.add(get(i));
        }
        return strings;
    }

    /**
     * create a new list of strings, whose values have been substring'd
     *
     * @param index
     * @return
     */
    public StringList substringInside(int index) {
        StringList strings = new StringList();
        for (int i = 0; i < size(); i++) {
            strings.add(get(i).substring(index));
        }
        return strings;
    }

    public StringList clone() {
        StringList strings = new StringList();
        for (String value : this) {
            strings.add(value);
        }
        return strings;
    }

    /**
     * @param values
     * @return values from this that exists
     */
    public StringList intersect(List<String> values) {
        StringList strings = new StringList();
        for (String value : values) {
            if (contains(value)) {
                strings.add(value);
            }
        }
        return strings;
    }

    public StringList addValue(String value) {
        super.add(value);
        return this;
    }

    public StringList map(Transform transform) {
        StringList mapped = new StringList();
        for (int i = 0; i < size(); i++) {
            mapped.add(i, transform.transform(get(i)));
        }
        return mapped;
    }

    public static interface Transform {
        String transform(String value);
    }

}
