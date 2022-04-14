package com.alibaba.chaosblade.box.common.experiment.task.flow.exception;

public class ExperimentDefinitionParserException extends ChaosFlowException {

    @Override
    public ExceptionCode getExceptionCode() {
        return ExceptionCode.EXPERIMENT_DEFINITION_PARSER_FAILED;
    }

    public ExperimentDefinitionParserException(String errorMessage, Throwable e) {
        super(errorMessage, e);
    }

}
