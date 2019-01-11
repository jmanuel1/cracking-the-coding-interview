import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ArraysAndStrings {
    public static void main(String[] args) {
        IsUniqueQuestion.test();
    }
}

/* 1.1 Is Unique */
class IsUniqueQuestion {
    static void test() {
        testWithImplementation(IsUniqueQuestion::isUnique);
        testWithImplementation(IsUniqueQuestion::isUniqueNoOtherStructures);
    }

    private static void testWithImplementation(Predicate<String> isUnique) {
        String trueCase = "abcde";
        String falseCase = "hello";
        String caseCase = "helLo";

        assert isUnique.test(trueCase);
        assert !isUnique.test(falseCase);
        // let's return true for ""
        assert isUnique.test("");
        // let's return false for null
        assert !isUnique.test(null);
        // let's respect case
        assert isUnique.test(caseCase);
    }

    /* These solutions assume that one char represents one code point, which
       isn't always true in Java. */

    private static boolean isUnique(String str) {
        Set<Character> chars = new HashSet<>();

        if (str == null) return false;

        for (char c : str.toCharArray()) {
            if (chars.contains(c)) {
                return false;
            }
            chars.add(c);
        }
        return true;
    }

    /* If we can't use additional data structures, we can do the following :
       1. Compare every character of the string to every other character of the
       string. This will take 0( n^2 ) time
       and 0(1) space.
       2. If we are allowed to modify the input string, we could sort the string
       in O(n log(n)) time and then
       linearly check the string for neighboring characters that are identical.
       Careful, though: many sorting
       algorithms take up extra space. (from p. 193)

       I took this note because I wasn't sure what counted as an 'additional
       data structure'. */
    private static boolean isUniqueNoOtherStructures(String str) {
        if (str == null) return false;

        for (int i = 0; i < str.length(); i++) {
            for (int j = i + 1; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }
}