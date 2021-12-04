package de.mvitz.javaspektrum.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

public class JsonbExample {

    public static void main(String[] args) {
        dataBinding();
    }

    static void dataBinding() {
        Jsonb jsonb = JsonbBuilder.create();
        Test test = jsonb.fromJson("""
                {
                  "number": 5,
                  "string": "Michael"
                }
                """, Test.class);
        System.out.println(test);

        String json = jsonb.toJson(test);
        System.out.println(json);
    }

    public static class Test {
        int number;
        String string;

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setString(String string) {
            this.string = string;
        }

        public String getString() {
            return string;
        }

        @Override
        public String toString() {
            return "Test { number=%d, string=%s }".formatted(number, string);
        }
    }
}
