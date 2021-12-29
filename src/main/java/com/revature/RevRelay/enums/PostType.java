package com.revature.RevRelay.enums;

public enum PostType {

    ORIGINAL("ORIGINAL"),
    REPLY("REPLY");

    String value;
    PostType(String value){
        this.value = value;
    }
    
    public String getValue(){
        return value;
    }
}
