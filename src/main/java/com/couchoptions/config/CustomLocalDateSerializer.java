package com.couchoptions.config;

import com.couchoptions.utils.Constants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateSerializer
        extends StdSerializer<LocalDate> {

    public CustomLocalDateSerializer() {
        this(null);
    }

    public CustomLocalDateSerializer(Class<LocalDate> t) {
        super(t);
    }

    @Override
    public void serialize(
            LocalDate value,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(value.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT)));
    }
}
