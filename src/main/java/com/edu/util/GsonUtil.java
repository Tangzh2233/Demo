package com.edu.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Tangzh
 * @version 1.0.0
 * @date 2020/9/24 14:54
 * @description
 **/
public class GsonUtil {

    private static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gson = gsonBuilder.create();
    }

    public static String obj2Json(Object obj) {
        return gson.toJson(obj);
    }

    public static <T> T json2obj(String obj, Class<T> type) {
        return gson.fromJson(obj, type);
    }


    /**
     * 字符串转换成集合，type事例：new TypeToken<List<AbcModel>>(){}.getType()
     *
     * @param obj     json格式字符串
     * @param typeOfT com.google.gson.reflect.TypeToken
     * @return
     */
    public static <T> T json2List(String obj, Type typeOfT) {
        return gson.fromJson(obj, typeOfT);
    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

        public void write(JsonWriter writer, LocalDateTime value) throws IOException {
            if (value == null) {
                writer.nullValue();
                return;
            }

            writer.value(LocalDateUtils.getLongFromLocalDateTime(value));
        }

        public LocalDateTime read(JsonReader reader) throws IOException {
            if (reader.peek() == JsonToken.NULL) {
                reader.nextNull();
                return null;
            }
            Long dateTime = reader.nextLong();
            return LocalDateUtils.getLocalDateTimeFromLong(dateTime);
        }
    }

    public static class LocalDateUtils {
        private static final ZoneId ZONE_ID = ZoneId.systemDefault();
        public static LocalDateTime getLocalDateTimeFromLong(long timestamp) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZONE_ID);
        }

        public static Long getLongFromLocalDateTime(LocalDateTime localDateTime) {
            return localDateTime.atZone(ZONE_ID).toInstant().toEpochMilli();
        }

        public static LocalDate getLocalDateFromLong(long timestamp) {
            return Instant.ofEpochMilli(timestamp).atZone(ZONE_ID).toLocalDate();
        }
        public static Long getLongFromLocalDate(LocalDate localDate) {
            return localDate.atStartOfDay(ZONE_ID).toInstant().toEpochMilli();
        }

    }
}
