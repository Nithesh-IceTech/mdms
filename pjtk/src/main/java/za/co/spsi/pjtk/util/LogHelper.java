package za.co.spsi.pjtk.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

import java.util.stream.IntStream;

@Slf4j
public class LogHelper {

    public static void logBanner(Logger log, String heading, String message) {
        // calc with
        String line = IntStream.range(0,heading.length()+4).mapToObj(i -> "*").reduce((s1,s2) -> s1+s2).get();
        log.info(line);
        log.info("* " + heading + " *");
        log.info(line);
        log.info(message);
    }
}
