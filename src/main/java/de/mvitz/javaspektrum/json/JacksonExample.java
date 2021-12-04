package de.mvitz.javaspektrum.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class JacksonExample {

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

    static JsonNode create() throws IOException {
        ObjectMapper om = new ObjectMapper();

        final ObjectNode json = om.createObjectNode()
            .put("number", 1);
        json.set("object", om.createObjectNode()
            .put("string", "Hallo"));
        json.put("boolean", true)
            .set("array", om.createArrayNode()
                .add(47.11)
                .add("Hallo nochmal"));

        System.out.println(json);

        return json;
    }

    static void parse() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();
        JsonNode json = om.readTree("""
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
        ObjectMapper om = new ObjectMapper();
        JsonParser parser = om.createParser("""
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

//        reader.beginObject();
//        while(reader.hasNext()) {
//            System.out.println(reader.nextName());
//            reader.skipValue();
//        }
//        reader.close();

    }

    static void write() throws IOException {
        System.out.println("JacksonExample.write");

        JsonNode json = create();

        final StringWriter writer = new StringWriter();
        ObjectMapper om = new ObjectMapper();
        om.writeValue(writer, json);
        System.out.println(writer);

        System.out.println();
    }

    static void writeWithWriter() throws IOException {
        System.out.println("JacksonExample.writeWithWriter");

        ObjectMapper om = new ObjectMapper();

        JsonGenerator generator = om.createGenerator(System.out);

        generator.writeStartObject();

        generator.writeNumberField("number", 1);

        generator.writeObjectFieldStart("object");
        generator.writeStringField("string", "Hallo");
        generator.writeEndObject();

        generator.writeBooleanField("boolean", true);

        generator.writeArrayFieldStart("array");
        generator.writeNumber(47.11);
        generator.writeString("Hallo nochmal");
        generator.writeEndArray();

        generator.writeEndObject();

        generator.flush();
        System.out.println();

        System.out.println();
    }

    static void transform() {
//        JSONObject json = create();
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

    static void pointer() throws IOException {
        ObjectMapper om = new ObjectMapper();
        TreeNode json = om.readTree("""
            {
              "object": {
                "string": "Hallo"
              },
              "array": [
                47.11,
                "Hallo nochmal",
                {
                  "foo": "bar"
                }
              ]
            }
            """);

        System.out.println(json.at(JsonPointer.compile("/object/string")));
        System.out.println(json.at(JsonPointer.compile("/array/2/foo")));
    }

    static void dataBinding() throws JsonProcessingException {
        ObjectMapper om = new ObjectMapper();

        Test test = om
            .readValue("""
                {
                  "nummer": 5,
                  "string": "Michael",
                  "foos": [
                    {
                      "type": "my-bar",
                      "prop": 42
                    },
                    {
                      "type": "some-other",
                      "property": "Hallo"
                    }
                  ]
                }
                """, Test.class);
        System.out.println(test);



        String json = om.writeValueAsString(test);
        System.out.println(json);
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = Bar.class, name = "my-bar"),
        @JsonSubTypes.Type(value = Baz.class, name = "some-other")
        }
    )
    interface Foo {}

    record Bar(int prop) implements Foo {}

    record Baz(String property) implements Foo {}

    record Test(@JsonProperty("nummer") int number, String string, List<Foo> foos) {}
}
