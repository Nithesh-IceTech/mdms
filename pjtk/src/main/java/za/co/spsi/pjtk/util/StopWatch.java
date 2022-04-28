package za.co.spsi.pjtk.util;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class StopWatch {

    private String name;
    private List<Task> stack = new ArrayList<>();

    public StopWatch(String name) {
        this.name = name;
    }

    public StopWatch start() {
        stack.add(new Task(""));
        return this;
    }

    public void mark(String name) {
        stack.add(new Task(name));
    }

    public long getRunning() {
        return stack.size() > 0?stack.get(stack.size()-1).start - stack.get(0).start:0;
    }

    public String toString() {
        List<String> values = new ArrayList<>();
        for (int i = 1;i < stack.size();i++) {
            values.add(stack.get(i).toString(stack.get(i-1).start));
        }
        return String.format("Stopwatch: %s {\n\t%s\n}",name,values.stream().reduce((s1,s2) -> s1+"\n\t"+s2).get());
    }

    @Data
    static class Task {
        private long start;
        private String name;

        Task(String name) {
            start = System.currentTimeMillis();
            this.name = name;
        }

        public String toString(long offset) {
            return String.format("Task %s Took %d",name,start-offset);
        }
    }
}
