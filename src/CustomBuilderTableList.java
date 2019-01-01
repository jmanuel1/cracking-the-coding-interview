import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CustomBuilderTableList {
    private static void testMyStringBuilder() {
        StringBuilder builder = new StringBuilder();
        MyStringBuilder myStringBuilder = new MyStringBuilder();
        String words[] = { "An", " interesting", " sentence" };

        for (String word : words) {
            builder.append(word);
            myStringBuilder.append(word);
        }

        assert builder.toString().equals(myStringBuilder.toString());
    }

    private static void testMyHashMap() {
        Map<String, Integer> hashMap = new HashMap<>();
        MyHashMap<String, Integer> myHashMap = new MyHashMap<>();
        String keys[] = { "two", "keys" };
        int vals[] = { 42, 6*9 };

        for (int i = 0; i < keys.length; i++) {
            hashMap.put(keys[i], vals[i]);
            myHashMap.put(keys[i], vals[i]);
        }

        assert hashMap.get("keys").equals(myHashMap.get("keys"));

        hashMap.remove("keys");
        myHashMap.remove("keys");

        assert hashMap.get("keys") == null && myHashMap.get("keys") == null;
    }

    public static void main(String[] args) {
        testMyStringBuilder();
        testMyHashMap();
    }
}

// Custom StringBuilder
class MyStringBuilder {
    private ArrayList<String> stringList;

    MyStringBuilder() {
        this.stringList = new ArrayList<>();
    }

    @SuppressWarnings("UnusedReturnValue")
    MyStringBuilder append(String str) {
        this.stringList.add(str);
        return this;
    }

    @Override
    public String toString() {
        return String.join("", stringList);
    }
}

// Custom HashMap
class MyHashMap<KeyType, ValType> {
    private ArrayList<LinkedList<KeyValPair<KeyType, ValType>>> buckets;
    private static int numOfBuckets = 1024;

    MyHashMap() {
        buckets = new ArrayList<>();

        for (int i = 0; i < numOfBuckets; i++) {
            buckets.add(new LinkedList<>());
        }
    }

    private LinkedList<KeyValPair<KeyType, ValType>> getBucket(Object key) {
        int hash = key.hashCode();
        int index = hash % numOfBuckets;

        return buckets.get(index);
    }

    @SuppressWarnings("UnusedReturnValue")
    ValType put(KeyType key, ValType val) {
        for (KeyValPair<KeyType, ValType> pair : getBucket(key)) {
            if (pair.getKey().equals(key)) {
                ValType prevVal = pair.getVal();
                pair.setVal(val);
                return prevVal;
            }
        }

        getBucket(key).add(0, new KeyValPair<>(key, val));
        return null;
    }

    ValType get(@SuppressWarnings("SameParameterValue") Object key) {
        for (KeyValPair<KeyType, ValType> pair : getBucket(key)) {
            if (pair.getKey().equals(key)) {
                return pair.getVal();
            }
        }

        return null;
    }

    @SuppressWarnings("UnusedReturnValue")
    ValType remove(@SuppressWarnings("SameParameterValue") Object key) {
        for (KeyValPair<KeyType, ValType> pair : getBucket(key)) {
            if (pair.getKey().equals(key)) {
                getBucket(key).remove(pair);
                return pair.getVal();
            }
        }

        return null;
    }
}

class KeyValPair<KeyType, ValType> {
    private KeyType key;
    private ValType val;

    KeyValPair(KeyType key, ValType val) {
        this.key = key;
        this.val = val;
    }

    KeyType getKey() {
        return key;
    }

    ValType getVal() {
        return val;
    }

    void setVal(ValType val) {
        this.val = val;
    }
}