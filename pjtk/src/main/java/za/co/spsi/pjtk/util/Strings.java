package za.co.spsi.pjtk.util;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor
public class Strings extends LinkedList<String> implements Serializable {
   
    private static final long serialVersionUID = 264120837841302213L;

    public Strings(Collection<String> collection) {
        addAll(collection);
    }

    public static Strings asList(String... strings) {
        return new Strings(Arrays.asList(strings));
    }

    /**
     * return all the indexes of strings that contains the text
     * @param text
     * @return
     */
    public List<Integer> indexesOfContains(String text) {
        return IntStream.range(0,size()).filter(i -> get(i).contains(text))
                .boxed().collect(Collectors.toList());
    }

    public Optional<Integer> firstIndexesOfContains(String text) {
        List<Integer> indexes = indexesOfContains(text);
        return indexes.isEmpty()?Optional.empty():Optional.of(indexes.get(0));
    }

    public Strings replace(String oldV, String newV) {
        return stream().map(s -> s != null?s.replace(oldV,newV):null)
                .collect(Collectors.toCollection(Strings::new));
    }

    @Override
    public Strings clone() {
        return new Strings(this);
    }

    /**
     * @param filter
     * @return a new instance with items matching filter removed
     */
    public Strings removeWhen(Predicate<String> filter) {
        Strings clone = clone();
        clone.removeIf(filter);
        return clone;
    }

    @Override
    public String toString() {
        return toString("\n");
    }

    public static void main(String[] args) {
        System.out.println(new Strings().append(";\n").toString(""));
    }

    // overload so that it comes up as an first option function in IDE - might accidentally select append
    @Override
    public boolean add(String value) {
        return super.add(value);
    }

    public Strings prepend(String value) {
        return new Strings(stream().map(s -> value + s).collect(Collectors.toList()));
    }

    public Strings append(String value) {
        return new Strings(stream().map(s -> s + value).collect(Collectors.toList()));
    }

    public Strings plus(String... values) {
        return plus(Arrays.asList(values));
    }

    public Strings plus(Collection<String> values) {
        Strings strings = new Strings(this);
        strings.addAll(values);
        return strings;
    }

    public String toString(String del) {
        return !isEmpty() ? stream().reduce((s1, s2) -> s1 + del + s2).get() : "";
    }

    public Strings toUpper() {
        return stream().map(s -> s != null ? s.toUpperCase() : s).collect(Collectors.toCollection(Strings::new));
    }

    public Strings toLower() {
        return stream().map(s -> s != null ? s.toLowerCase() : s).collect(Collectors.toCollection(Strings::new));
    }

    public Strings trim() {
        return stream().map(s -> s != null ? s.trim() : s).collect(Collectors.toCollection(Strings::new));
    }

    public Strings replace(Call1<String,String> replaceFunc) {
        return stream().map(s -> replaceFunc.call(s)).collect(Collectors.toCollection(Strings::new));
    }

    public Strings sub(int fromIndex, int toIndex) {
        return new Strings(super.subList(fromIndex,toIndex));
    }


    /**
     * return the index of an item that contains the value
     *
     * @param value
     * @return
     */
    public OptionalInt getInsideIndex(String value) {
        return IntStream.range(0, size())
                .filter(i -> get(i) != null && get(i).contains(value))
                .findAny();
    }

    public OptionalInt endsWithInside(String value) {
        return IntStream.range(0, size())
                .filter(i -> get(i) != null && get(i).endsWith(value))
                .findAny();
    }

    public OptionalInt startsWithInside(String value) {
        return IntStream.range(0, size())
                .filter(i -> get(i) != null && get(i).startsWith(value))
                .findAny();
    }

    /**
     * return if this contains a string with value
     *
     * @param value
     * @return
     */
    public boolean containsInside(String value) {
        return getInsideIndex(value).isPresent();
    }


    public boolean containsInsideIgnoreCase(String value) {
        return toLower().getInsideIndex(value.toLowerCase()).isPresent();
    }

    public boolean containsIgnoreCase(String value) {
        return toLower().contains(value.toLowerCase());
    }

    public int indexIOfIgnoreCase(String value) {
        return toLower().indexOf(value.toLowerCase());
    }


    @SneakyThrows
    public static Strings range(int start,int end,Call1Ex<Integer,String> call) {
        Strings strings = new Strings();
        for (int i = start;i < end;i++) {
            strings.add(call.call(i));
        }
        return strings;
    }

    public static Strings asList(String value, String regex){
        return value != null ? Strings.asList(value.split(regex)) : null;
    }
    
    public int frequency(String value){
        return Collections.frequency(this, value);
    }

}
