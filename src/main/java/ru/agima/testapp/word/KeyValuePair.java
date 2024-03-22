package ru.agima.testapp.word;

public class KeyValuePair {

    private String key;
    private Long value;

    public KeyValuePair(String key, Long value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "KeyValuePair{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
