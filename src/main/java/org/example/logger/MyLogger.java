package org.example.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyLogger {
    public static final Logger log = LoggerFactory.getLogger(MyLogger.class);

    private MyLogger() {
    }


}