package explore_with_me.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import explore_with_me.auxiliary_classes.Attributes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "endpoints_hit")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // id of this recording in BD
    @Column(name = "app")
    private String app; // name of service where was made a request (in our case 'ewn')
    @Type(type = "jsonb")
    @Column(name = "attributes", columnDefinition = "jsonb")
    private Attributes attributes; // ip address of user who made a request
    @Column(name = "timestamp")
    private LocalDateTime timestamp; // time when this request was made by user
}
