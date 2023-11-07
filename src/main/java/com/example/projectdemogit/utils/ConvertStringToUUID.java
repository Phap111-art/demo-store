package com.example.projectdemogit.utils;

import java.util.UUID;

public class ConvertStringToUUID {
    public static  UUID  getUUID(String uuid){
        return UUID.fromString(uuid);
    }
}
