package de.mvitz.javaspektrum.json;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

import java.io.StringWriter;

public class OrgJsonExample {

    public static void main(String[] args) {
        create();
        parse();
        write();
        writeWithWriter();
        transform();
        pointer();
    }

    static JSONObject create() {
        JSONObject json = new JSONObject()
            .put("number", 1)
            .put("object", new JSONObject()
                .put("string", "Hallo"))
            .put("boolean", true)
            .put("array", new JSONArray()
                    .put(47.11)
                    .put("Hallo nochmal"));

        System.out.println(json);

        return json;
    }

    static void parse() {
        JSONObject json = new JSONObject(new JSONTokener("""
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
            """));

        System.out.println(json);
    }

    static void write() {
        JSONObject json = create();

        final StringWriter writer = new StringWriter();
        json.write(writer, 4, 0);
        System.out.println(writer);
    }

    static void writeWithWriter() {
        new JSONWriter(System.out)
            .object()
                .key("number").value(1)
                .key("object").object()
                    .key("string").value("Hallo")
                .endObject()
                .key("boolean").value(true)
                .key("array").array()
                    .value(47.11)
                    .value("Hallo nochmal")
                .endArray()
            .endObject();
        System.out.println();
        System.out.println();
    }

    static void transform() {
        System.out.println("OrgJsonExample.transform");

        JSONObject json = new JSONObject()
            .put("number", 1)
            .put("array", new JSONArray()
                .put(2))
            .put("string", "5")
            .put("null", JSONObject.NULL);

        json.increment("number"); // -> "number": 2
        json.append("array", false); // -> "array" [2, false]

        System.out.println(json);

        System.out.println(json.isEmpty()); // -> false
        System.out.println(json.has("not-there")); // -> false
        System.out.println(json.isNull("not-there")); // -> true
        System.out.println(json.has("null")); // -> true
        System.out.println(json.isNull("null")); // -> true

        //json.putOpt()
        //json.putOnce()
        System.out.println(json.optInt("string")); // -> 5
        System.out.println(json.getInt("string")); // -> 5
        //System.out.println(json.getInt("not-there"));
        //System.out.println(json.getString("number")); // throws Exception
        System.out.println(json.optString("number")); // -> "1"

        json.accumulate("string", 2); // -> "string": ["1", 2]
        System.out.println(json);

        System.out.println();
    }

    static void pointer() {
        JSONObject json = new JSONObject()
            .put("object", new JSONObject()
                .put("string", "Hallo"))
                .put("array", new JSONArray()
                    .put(47.11)
                    .put("Hallo nochmal")
                    .put(new JSONObject()
                        .put("string", "Und weg!")));

        System.out.println(json.query("/object/string"));
        System.out.println(json.query("/array/2/string"));
    }
}
