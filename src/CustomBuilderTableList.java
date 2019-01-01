import java.util.ArrayList;

public class CustomBuilderTableList {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder();
        MyStringBuilder myStringBuilder = new MyStringBuilder();
        String words[] = { "An", " interesting", " sentence" };

        for (String word : words) {
            builder.append(word);
            myStringBuilder.append(word);
        }

        assert builder.toString().equals(myStringBuilder.toString());
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