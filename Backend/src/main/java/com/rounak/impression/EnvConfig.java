package com.rounak.impression;


import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {
    public static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    static {
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }

    public static String get(String key) {
        return dotenv.get(key);
    }
}
