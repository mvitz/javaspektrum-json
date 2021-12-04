package de.mvitz.javaspektrum.json;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonPatch;
import jakarta.json.JsonReader;
import jakarta.json.JsonWriter;
import jakarta.json.stream.JsonGenerator;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

public class JsonpExample {

    public static void main(String[] args) {
        create();
        parse();
        write();
        writeWithWriter();
        transform();
        pointer();
        patch();
    }

    static JsonObject create() {
        JsonObject json = Json.createObjectBuilder()
            .add("number", 1)
            .add("object", Json.createObjectBuilder()
                .add("string", "Hallo"))
            .add("boolean", true)
            .add("array", Json.createArrayBuilder()
                .add(47.11)
                .add("Hallo nochmal"))
            .build();

        System.out.println(json);

        return json;
    }

    static void parse() {
        try (JsonReader reader = Json.createReader(new StringReader("""
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
                """))) {
            JsonObject json = reader.readObject();
            System.out.println(json);
        }
    }

    static void write() {
        JsonObject json = create();

        final StringWriter writer = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(writer)) {
            jsonWriter.write(json);
            System.out.println(writer);
        }
    }

    static void writeWithWriter() {
        Map<String,?> properties = Map.of(JsonGenerator.PRETTY_PRINTING, Boolean.FALSE);
        final JsonGenerator generator = Json.createGeneratorFactory(properties).createGenerator(System.out);

        generator
            .writeStartObject()
                .write("number", 1)
                .writeStartObject("object")
                    .write("string", "Hallo")
                .writeEnd()
                .write("boolean", true)
                .writeStartArray("array")
                    .write(47.11)
                    .write("Hallo nochmal")
                .writeEnd()
            .writeEnd()
            .flush();
    }

    static void transform() {
//        JsonObject json = create();
    }

    static void pointer() {
        JsonObject json = create();
        json = Json.createObjectBuilder(json)
            .add("array", Json.createArrayBuilder(json.getJsonArray("array"))
                .add(Json.createObjectBuilder().add("foo", "bar"))).build();

        json = Json.createObjectBuilder()
            .add("object", Json.createObjectBuilder()
                .add("string", "Hallo"))
            .add("array", Json.createArrayBuilder()
                .add(47.11)
                .add("Hallo nochmal")
                .add(Json.createObjectBuilder()
                    .add("string", "Und weg!")))
            .build();


        System.out.println(Json.createPointer("/object/string").getValue(json));
        System.out.println(Json.createPointer("/array/2/string").getValue(json));
    }

    static void patch() {
        JsonObject json = create();

        JsonPatch patch = Json.createPatchBuilder()
            .add("/added", Json.createObjectBuilder().add("foo", "bar").build())
            .remove("/boolean")
            .replace("/number", 3)
            .replace("/array/1", "Und weg")
            .add("/array/2", Json.createObjectBuilder().add("foo", "bar").build())
            .build();

        JsonObject patchedJson = patch.apply(json);
        System.out.println(patchedJson);
    }
}
