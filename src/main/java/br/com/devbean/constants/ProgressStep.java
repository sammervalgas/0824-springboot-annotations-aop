package br.com.devbean.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProgressStep {

    WAITING(0),
    STARTED(1),
    IN_PROGRESS(2),
    COLLECTING(3),
    FINISHED(4);

    private final int code;

}
