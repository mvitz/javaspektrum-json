package de.mvitz.javaspektrum.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

public class GsonExample {

    public static void main(String[] args) throws IOException {
        create();
        parse();
        parseWithReader();
        write();
        writeWithWriter();
        transform();
        pointer();
        dataBinding();
    }

    static JsonObject create() {
        final JsonObject object = new JsonObject();
        object.addProperty("string", "Hallo");

        final JsonArray array = new JsonArray();
        array.add(47.11);
        array.add("Hallo nochmal");

        JsonObject json = new JsonObject();
        json.addProperty("number", 1);
        json.add("object", object);
        json.addProperty("boolean", true);
        json.add("array", array);

        System.out.println(json);

        return json;
    }

    static void parse() {
        JsonElement json = JsonParser.parseString("""
            {
              "number": 1,
              "object": {
                "string":"Hallo"
              },
              "boolean": true,
              "array": [
                47.11,
                "Hallo nochmal"
              ]
            }
            """);

        System.out.println(json);
    }

    static void parseWithReader() throws IOException {
        System.out.println("GsonExample.parseWithReader");

        JsonElement json = create();
        try (JsonReader reader = new JsonReader(
            new StringReader(json.toString()))) {
            reader.beginObject();
            while(reader.hasNext()) {
                System.out.println(reader.nextName());
                reader.skipValue();
            }
        }

        System.out.println();
    }

    static void write() {
        JsonObject json = create();

        final StringWriter writer = new StringWriter();
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        gson.toJson(json, writer);
        System.out.println(writer);
    }

    static void writeWithWriter() throws IOException {
        new JsonWriter(new PrintWriter(System.out))
            .beginObject()
                .name("number").value(1)
                .name("object").beginObject()
                    .name("string").value("Hallo")
                .endObject()
                .name("boolean").value(true)
                .name("array").beginArray()
                    .value(47.11)
                    .value("Hallo nochmal")
                .endArray()
            .endObject()
            .flush();
        System.out.println();
    }

    static void transform() {
        JsonObject json = create();
//
//        json.increment("number");
//        json.append("array", false);
//        json.put("string", "1");
//
//        System.out.println(json);
//
//        System.out.println(json.isEmpty());
//        System.out.println(new JSONObject().isEmpty());
//        System.out.println(json.has("not-there"));
//        System.out.println(json.isNull("not-there"));
//        //json.putOpt()
//        //json.putOnce()
//        System.out.println(json.optInt("string"));
//        System.out.println(json.getInt("string"));
//
//        json.accumulate("string", 2);
//        System.out.println(json);
    }

    static void pointer() {
//        JSONObject json = create();
//
//        json.append("array", new JSONObject()
//            .put("foo", "bar"));
//
//        System.out.println(json.query("/object/string"));
//        System.out.println(json.query("/array/2/foo"));
    }

    static void dataBinding() {
        final Gson gson = new GsonBuilder()
            .create();

        final Test test = gson
            .fromJson("""
                {
                  "number": 5,
                  "string": "Michael"
                }
                """, Test.class);
        System.out.println(test);

        String json = gson.toJson(test);
        System.out.println(json);
    }

    static class Test {
        private int number;
        private String string;

        @Override
        public String toString() {
            return "Test { number=%d, string=%s }".formatted(number, string);
        }
    }
}
