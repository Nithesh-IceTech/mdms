package za.co.spsi.mdms.common.generator.msg;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageFactory {

    private static List<GeneratorMessage> MESSAGES = new ArrayList<>();

    static {
        MESSAGES.add(new G1Msg());
        MESSAGES.add(new StatusMsg());
    }

    public static Optional<GeneratorMessage> getMessage(String source) {
        return MESSAGES.stream().filter(m -> m.match(source)).map(m -> {
            try {
                return m.getClass().newInstance().init(source);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).findFirst();
    }
}
