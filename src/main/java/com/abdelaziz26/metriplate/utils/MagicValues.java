package com.abdelaziz26.metriplate.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

@Profile("dev")
public class MagicValues {

    @Value("${BUCKET_CAPACITY}")
    public static int BUCKET_CAPACITY;

    @Value("{REFILL_RATE}")
    public static int REFILL_RATE;

    public static String RATE_LIMIT_EXCEEDED_MSG = "\"error\" : \"Rate limit exceeded\"";

    public static int pageSize = 10;

}
