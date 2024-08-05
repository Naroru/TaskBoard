package chukhlantsev.oleg.taskboard.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionBody {

    String message;
    Map<String,String> errors;

    public ExceptionBody(String message) {
        this.message = message;
    }
}
