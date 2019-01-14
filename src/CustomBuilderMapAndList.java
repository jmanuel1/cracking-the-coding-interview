import java.util.*;

public class CustomBuilderMapAndList {
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

    private static void testMyArrayList() {
        List<Integer> arrayList = new ArrayList<>();
        MyArrayList<Integer> myArrayList = new MyArrayList<>();
        int hailstoneSeq[] = { 39, 118, 59, 178, 89, 268, 134, 67, 202, 101,
                304, 152, 77, 232, 116, 58, 29, 88, 44, 22, 11, 34, 17, 52, 26,
                13, 40, 20, 10, 5, 16, 8, 4, 2, 1 };

        for (int n : hailstoneSeq) {
            arrayList.add(n);
            myArrayList.add(n);
        }

//        System.out.printf("Java: %d\nMe: %d", arrayList.get(4), myArrayList.get(4));

        assert arrayList.get(4).equals(myArrayList.get(4)) &&
                myArrayList.get(4).equals(89);
    }

    public static void main(String[] args) {
        testMyStringBuilder();
        testMyHashMap();
        testMyArrayList();
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

// custom ArrayList
class MyArrayList<ElemType> {
    private ElemType array[];
    private int length; // length as it appears to the outside

    MyArrayList() {
        final int INITIAL_SIZE = 16;
        //noinspection unchecked
        array = (ElemType[]) new Object[INITIAL_SIZE];
        length = 0;
    }

    void add(ElemType elem) {
        set(length, elem);
    }

    ElemType get(@SuppressWarnings("SameParameterValue") int index) {
//        System.out.printf("Internal array (length %d): \n", length);
//        for (int i = 0; i < length; i++) {
//            System.out.print(array[i] + " ");
//        }
        return array[index];
    }

    @SuppressWarnings("unused")
    ElemType remove(int index) {
        ElemType elem = array[index];
        System.arraycopy(array, index + 1, array, index,
                array.length - index - 1);
        length--;
        return elem;
    }

    @SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
    ElemType set(int index, ElemType elem) {
        ElemType prevElem;
        int newLength = Math.max(index + 1, length);

        if (index >= length) {
            prevElem = null;
        } else {
            prevElem = array[index];
        }

        if (newLength >= array.length) {
            //noinspection unchecked
            ElemType biggerArray[] = (ElemType[]) new Object[array.length*2];
            System.arraycopy(array, 0, biggerArray, 0, length);
            array = biggerArray;
        }

        length = newLength;
        array[index] = elem;

        return prevElem;
    }
}