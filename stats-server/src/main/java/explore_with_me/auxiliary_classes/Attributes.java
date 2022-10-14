package explore_with_me.auxiliary_classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attributes implements Serializable {
    private final static long serialVersionUID = 7702L;

    private String uri;
    private String ip;
}
