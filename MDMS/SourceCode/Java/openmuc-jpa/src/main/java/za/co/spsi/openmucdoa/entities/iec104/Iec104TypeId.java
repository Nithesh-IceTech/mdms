package za.co.spsi.openmucdoa.entities.iec104;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class Iec104TypeId {

    private String type_id;
    private String  description;

    public Iec104TypeId() {

    }

    public Iec104TypeId(String type_id, String description) {
        this.type_id = type_id;
        this.description = description;
    }
}
