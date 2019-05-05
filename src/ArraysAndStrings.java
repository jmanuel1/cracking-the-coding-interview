import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

public class ArraysAndStrings {
    public static void main(String[] args) {
        IsUniqueQuestion.test();
        CheckPermutationQuestion.test();
        URLifyQuestion.test();
        PalindromePermutationQuestion.test();
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

/* 1.2 Check Permutation */
class CheckPermutationQuestion {
    @SuppressWarnings("ConstantConditions")
    static void test() {
        /* Test cases */
        // true case
        String trueCase[] = {"String", "ringSt"};
        // false case
        String falseCase[] = {"Hello", "World"};
        // true for empty strings
        String emptyTrueCase[] = {"", ""};
        // trivial cases: equal strings (true), different length strings (false)
        String equalTrueCase[] = {"equal", "equal"};
        String lengthFalseCase[] = {"different", "length"};
        // respect letter case
        String caseFalseCase[] = {"Jason", "nosaj"};
        // false if exactly one is null
        String oneNullCase[] = {null, "Hello"};
        // true if both null
        String bothNullCase[] = {null, null};
        // bounds checking for different length strings (when one is a prefix of
        // the other)
        String boundsCheck[] = {"bounds", "bounds check"};
        // especially when strings are already sorted
        String boundsCheckSorted[] = {"abcd", "abcdef"};

        // assume one char is one code point and that every character has only
        // one representation

        assert checkPermutation(trueCase[0], trueCase[1]);
        assert !checkPermutation(falseCase[0], falseCase[1]);
        assert checkPermutation(emptyTrueCase[0], emptyTrueCase[1]);
        assert checkPermutation(equalTrueCase[0], equalTrueCase[1]);
        assert !checkPermutation(lengthFalseCase[0], lengthFalseCase[1]);
        assert !checkPermutation(caseFalseCase[0], caseFalseCase[1]);
        assert !checkPermutation(oneNullCase[0], oneNullCase[1]);
        assert checkPermutation(bothNullCase[0], bothNullCase[1]);
        assert !checkPermutation(boundsCheck[0], boundsCheck[1]);
        assert !checkPermutation(boundsCheckSorted[0], boundsCheckSorted[1]);
        // check commutativity
        assert checkPermutation(trueCase[1], trueCase[0]);
        assert !checkPermutation(oneNullCase[1], oneNullCase[0]);
        assert !checkPermutation(boundsCheck[1], boundsCheck[0]);
        assert !checkPermutation(boundsCheckSorted[1], boundsCheckSorted[0]);
    }

    private static boolean checkPermutation(String s, String r) {
        if (s == null && r == null) return true;
        else if (s == null || r == null) return false;

        if (s.length() != r.length()) return false;

        char sAsChars[] = s.toCharArray();
        char rAsChars[] = r.toCharArray();

        // dual-pivot quicksort: often O(n log n), quadratic worst case
        // https://docs.oracle.com/javase/7/docs/api/java/util/Arrays.html#sort(char[])
        // it might be faster to collect character frequencies
        Arrays.sort(sAsChars);
        Arrays.sort(rAsChars);
        for (int i = 0; i < sAsChars.length; i++) {
            if (sAsChars[i] != rAsChars[i]) {
                return false;
            }
        }
        return true;
    }
}

class URLifyQuestion {
    @SuppressWarnings("ConstantConditions")
    static void test() {
        char johnSmith[] = "Mr John Smith    ".toCharArray();
        char empty[] = new char[0];
        char multipleSpaces[] = "Hello    there        ".toCharArray();
        char onlySpaces[] = "         ".toCharArray(); // true length = 3
        char otherWhitespace[] = {'\n', '\t'};
        char noSpaces[] = "none".toCharArray();
        char nullCase[] = null;

        URLify(johnSmith, 13);
        assert Arrays.equals(johnSmith, "Mr%20John%20Smith".toCharArray());
        URLify(empty, 0);
        assert Arrays.equals(empty, new char[0]);
        URLify(multipleSpaces, 14);
        assert Arrays.equals(multipleSpaces,
                "Hello%20%20%20%20there".toCharArray());
        URLify(onlySpaces, 3);
        assert Arrays.equals(onlySpaces, "%20%20%20".toCharArray());
        URLify(otherWhitespace, 2);
        assert Arrays.equals(otherWhitespace, new char[]{'\n', '\t'});
        URLify(noSpaces, 4);
        assert Arrays.equals(noSpaces, "none".toCharArray());
        URLify(nullCase, 0);
        assert Arrays.equals(nullCase, null);
    }

    // in place
    private static void URLify(char string[], int length) {
        for (int i = 0; i < length; i++) {
            if (string[i] == ' ') {
                // shift the rest of the string over by 2
                System.arraycopy(string, i + 1, string, i + 3,
                        length - i - 1);
                // and add two the 'real' length of the string
                length += 2;
                // replace the space with %20
                string[i] = '%';
                string[i + 1] = '2';
                string[i + 2] = '0';
            }
        }
    }
}

/* 1.4 Palindrome Permutation */
class PalindromePermutationQuestion {
    static void test() {
        // two simple cases
        assert isPermutationOfPalindrome("racecar");
        assert !isPermutationOfPalindrome("not");
        // ignore case
        assert isPermutationOfPalindrome("Tat");
        // ignore spaces
        assert isPermutationOfPalindrome("tact coa");
        // true on empty string
        assert isPermutationOfPalindrome("");
        // is false on null
        assert !isPermutationOfPalindrome(null);
    }

    private static boolean isPermutationOfPalindrome(String string) {
        if (string == null) {
            return false;
        }

        // get rid of spaces in the string and lower case it
        String sNormalized =
                string.replace(" ", "").toLowerCase();
        int countsMod2[] = new int[26]; // assume only English letters

        for (char c : sNormalized.toCharArray()) {
            int index = c - 'a';
            countsMod2[index] = (countsMod2[index] + 1) % 2;
        }

        int total = 0;
        for (int count : countsMod2) {
            total += count;
        }

        return total < 2;
    }
}