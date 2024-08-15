package chukhlantsev.oleg.taskboard.service.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

//хранит данные из application.yaml
//для удобства
@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    private  String secret;
    private  long access;
    private  long refresh;
}
