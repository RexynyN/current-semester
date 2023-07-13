package com.uws.jupiter.common.apollo;

import java.io.Serializable;

public class CodeResult implements Serializable {
    private Object returnValue;
    private String logs;
    private String compilationErrors; 
    private boolean errors;

    public CodeResult(Object returnValue, String logs, String compilationErrors, boolean errors) {
        this.returnValue = returnValue;
        this.logs = logs;
        this.compilationErrors = compilationErrors;
        this.errors = errors;
    }

    public boolean hadErrors() {
        return errors;
    }

    public String getCompilationErrors() {
        return compilationErrors;
    }

    public String getLogs() {
        return logs;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public static CodeResult parse(Object returnValue, String logs, String compilationErrors, boolean errors) {
        return new CodeResult(returnValue, logs, compilationErrors, errors);
    }
}
