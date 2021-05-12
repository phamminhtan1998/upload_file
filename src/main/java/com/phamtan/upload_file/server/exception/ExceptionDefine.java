package com.phamtan.upload_file.server.exception;

public class ExceptionDefine extends RuntimeException{
    private String name;
    private String causeDef ;
    private String description;

    public ExceptionDefine(String name, String cause, String description) {
        this.name = name;
        this.causeDef = cause;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCauseDef() {
        return causeDef;
    }

    public void setCauseDef(String causeDef) {
        this.causeDef = causeDef;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
