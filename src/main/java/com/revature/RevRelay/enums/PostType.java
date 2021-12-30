package com.revature.RevRelay.enums;

/**
 * Enum to be used for marking a post as either an original post or a reply to an original post
 */
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
