package com.couchoptions.config;

import com.couchoptions.utils.Constants;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

public class MillisOrLocalDateDeserializer extends StdDeserializer<LocalDate> {

    public MillisOrLocalDateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.hasToken(JsonToken.VALUE_NUMBER_INT)) {
            long value = parser.getValueAsLong();
            if (!isValidEpochDay(value)) {
                return toLocalDate(value);
            }
        }

        return LocalDateDeserializer.INSTANCE.deserialize(parser, context);
    }

    private static boolean isValidEpochDay(long value) {
        try {
            ChronoField.EPOCH_DAY.checkValidValue(value);
        } catch (DateTimeException ex) {
            return false;
        }
        return true;
    }

    private static LocalDate toLocalDate(Long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return LocalDate.of(ldt.getYear(), ldt.getMonth(), ldt.getDayOfMonth());
    }

}
