import java.util.*;
import java.util.function.Predicate;

public class ArraysAndStrings {
    public static void main(String[] args) {
        IsUniqueQuestion.test();
        CheckPermutationQuestion.test();
        URLifyQuestion.test();
        PalindromePermutationQuestion.test();
        OneAwayQuestion.test();
        StringCompressionQuestion.test();
        RotateMatrixQuestion.test();
        ZeroMatrixQuestion.test();
        StringRotationQuestion.test();
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
        String[] trueCase = {"String", "ringSt"};
        // false case
        String[] falseCase = {"Hello", "World"};
        // true for empty strings
        String[] emptyTrueCase = {"", ""};
        // trivial cases: equal strings (true), different length strings (false)
        String[] equalTrueCase = {"equal", "equal"};
        String[] lengthFalseCase = {"different", "length"};
        // respect letter case
        String[] caseFalseCase = {"Jason", "nosaj"};
        // false if exactly one is null
        String[] oneNullCase = {null, "Hello"};
        // true if both null
        String[] bothNullCase = {null, null};
        // bounds checking for different length strings (when one is a prefix of
        // the other)
        String[] boundsCheck = {"bounds", "bounds check"};
        // especially when strings are already sorted
        String[] boundsCheckSorted = {"abcd", "abcdef"};

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

        char[] sAsChars = s.toCharArray();
        char[] rAsChars = r.toCharArray();

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
        char[] johnSmith = "Mr John Smith    ".toCharArray();
        char[] empty = new char[0];
        char[] multipleSpaces = "Hello    there        ".toCharArray();
        char[] onlySpaces = "         ".toCharArray(); // true length = 3
        char[] otherWhitespace = {'\n', '\t'};
        char[] noSpaces = "none".toCharArray();
        char[] nullCase = null;

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
    private static void URLify(char[] string, int length) {
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
        //noinspection ConstantConditions
        assert !isPermutationOfPalindrome(null);
    }

    private static boolean isPermutationOfPalindrome(String string) {
        if (string == null) {
            return false;
        }

        // get rid of spaces in the string and lower case it
        String sNormalized =
                string.replace(" ", "").toLowerCase();
        int[] countsMod2 = new int[26]; // assume only English letters

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

/* 1.5 One Away */
// TODO: Check the official solution for this one especially
class OneAwayQuestion {
    static void test() {
        assert isOneAway("pale", "ple");
        assert isOneAway("pales", "pale");
        assert isOneAway("pale", "bale");
        assert !isOneAway("pale", "bake");
        // Difference in length is too large
        assert !isOneAway("pale", "pales!");
        // Relation is symmetric
        assert isOneAway("ple", "pale")
                == isOneAway("pale", "ple");
        // Relation is reflexive
        String reflexiveTestString = "ftbfbbgnhnfvg";
        assert isOneAway(reflexiveTestString, reflexiveTestString);
        // Handles empty strings
        assert isOneAway("", "a");
        // Not necessarily letters
        assert isOneAway("11!", "121!");
        // False if there is a null
        //noinspection ConstantConditions
        assert !isOneAway("", null);
        // Length difference of 1, but false
        assert !isOneAway("hello", "yellow");
        assert !isOneAway("hi there!", " hi there");
        assert !isOneAway("ABCD", "AED");
    }

    static private boolean isOneAway(String a, String b) {
        // return false on any null
        if (a == null || b == null) return false;

        // too many insertions/deletions
        if (Math.abs(a.length() - b.length()) > 1) return false;

        if (a.length() == b.length()) { // characters have been substituted
            int differences = 0;
            for (int i = 0; i < a.length(); i++) {
                if (a.charAt(i) != b.charAt(i)) {
                    differences++;
                    if (differences > 1) return false;
                }
            }
        }

        // There must be a length difference of one here, which means only one
        // insertion or deletion.
        if (a.length() - b.length() == 1) { // a character has been deleted
            int differences = 0;
            for (int i = 0, j = 0; i < a.length() && j < b.length(); i++, j++) {
                if (a.charAt(i) != b.charAt(j)) {
                    differences++;
                    // hold the index into b back to so that we can compare b[j]
                    // to a[i+1] next loop
                    j--;
                    if (differences > 1) return false;
                }
            }
        }

        // a character has been inserted; flip the arguments and treat it like
        // deletion
        if (a.length() - b.length() == -1) return isOneAway(b, a);

        return true;
    }
}

/* 1.6 String Compression */
class StringCompressionQuestion {
    static void test() {
        assert compressString("aabcccccaaa").equals("a2b1c5a3");
        // compressed would not be shorter, return original string
        assert compressString("abcd").equals("abcd"); // longer
        assert compressString("aaab").equals("aaab"); // same length
        // empty string
        assert compressString("").equals("");
        // null: choose to return "" instead of null
        assert compressString(null).equals("");
        // a letter with a more than one-digit count
        assert compressString("fffffffffffffffff").equals("f17");
        // case
        assert compressString("aaaaAAA").equals("a4A3");
        // we can assume the original string has only English letters
    }

    private static String compressString(String string) {
        char currentLetter = '@'; // initially not a letter
        int count = 0;
        StringBuilder builder = new StringBuilder();

        if (string == null) return "";

        for (char letter : string.toCharArray()) {
            if (letter == currentLetter) {
                count++;
            } else {
                if (count > 0) {
                    builder.append(currentLetter).append(count);
                }
                currentLetter = letter;
                count = 1;
            }
        }
        // make sure to encode the last run
        if (count > 0) {
            builder.append(currentLetter).append(count);
        }

        if (builder.length() >= string.length())
            return string;
        else
            return builder.toString();
    }
}

/* 1.7 Rotate Matrix */
class RotateMatrixQuestion {
    private static int[][] emptyMatrix;
    private static int[][] singletonMatrix;
    private static int[][] rotatedSingletonMatrix = {{1}};
    private static int[][] matrix;
    private static int[][] rotatedMatrix =
            {
                    {3, 7, 7, 6, 5, 7},
                    {45, 3, 454, 345, 453, 656},
                    {857, 565, 3, 5, 3, 4},
                    {7, 6, 2, 4, 5, 3},
                    {56, 365, 347, 756, 546, 642},
                    {234, 567, 567, 456, 765, 455}
            };
    private static int[][] oddSizedMatrix, rotatedOddSizedMatrix = {
            {45, 3, 454, 345, 453},
            {857, 565, 3, 5, 3},
            {7, 6, 2, 4, 5},
            {56, 365, 347, 756, 546},
            {234, 567, 567, 456, 765}
    };

    private static void initMatrices() {
        emptyMatrix = new int[0][0];
        singletonMatrix = new int[][]{{1}};
        matrix = new int[][]
                {
                        {234, 56, 7, 857, 45, 3},
                        {567, 365, 6, 565, 3, 7},
                        {567, 347, 2, 3, 454, 7},
                        {456, 756, 4, 5, 345, 6},
                        {765, 546, 5, 3, 453, 5},
                        {455, 642, 3, 4, 656, 7}
                };
        oddSizedMatrix = new int[][]
                {
                        {234, 56, 7, 857, 45},
                        {567, 365, 6, 565, 3},
                        {567, 347, 2, 3, 454},
                        {456, 756, 4, 5, 345},
                        {765, 546, 5, 3, 453}
                };
    }

    static void test() {
        // reset matrices here
        initMatrices();
        testPureImplementation();
        initMatrices();
        testInPlaceImplementation();
    }

    private static void testInPlaceImplementation() {
        rotateMatrixInPlace(emptyMatrix);
        assert Arrays.deepEquals(emptyMatrix, new int[0][0]);
        rotateMatrixInPlace(singletonMatrix);
        assert Arrays.deepEquals(singletonMatrix, rotatedSingletonMatrix);
        // assume counterclockwise rotation
        rotateMatrixInPlace(matrix);
        assert Arrays.deepEquals(matrix, rotatedMatrix);
        // make sure the logic doesn't fail based on size parity
        rotateMatrixInPlace(oddSizedMatrix);
        assert Arrays.deepEquals(oddSizedMatrix, rotatedOddSizedMatrix);
        // null --> Exception since algorithm is in-place
        boolean threwNPE = false;
        try {
            //noinspection ConstantConditions
            rotateMatrixInPlace(null);
        } catch (NullPointerException e) {
            threwNPE = true;
        }
        assert threwNPE;
    }

    private static void testPureImplementation() {
        assert Arrays.deepEquals(rotateMatrix(emptyMatrix), emptyMatrix);
        assert Arrays.deepEquals(rotateMatrix(singletonMatrix),
                singletonMatrix);
        // assume counterclockwise rotation
        assert Arrays.deepEquals(rotateMatrix(matrix), rotatedMatrix);
        // null --> { {} }
        assert Arrays.deepEquals(rotateMatrix(null), emptyMatrix);
    }

    // not in-place
    private static int[][] rotateMatrix(int[][] matrix) {
        if (matrix == null) {
            return new int[0][0];
        }

        int size = matrix.length;
        if (size <= 1) {
            return matrix;
        }

        int[][] rotatedMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                rotatedMatrix[size - 1 - j][i] = matrix[i][j];
            }
        }

        return rotatedMatrix;
    }

    private static void rotateMatrixInPlace(int[][] matrix) {
        int size = matrix.length;
        if (size <= 1) {
            return;
        }

        for (int row = 0; row < size / 2; row++) {
            for (int col = row; col < size - 1 - row; col++) {
                // get the four numbers to rotate, in counterclockwise order
                int a = matrix[row][col];
                int b = matrix[size - 1 - col][row];
                int c = matrix[size - 1 - row][size - 1 - col];
                int d = matrix[col][size - 1 - row];
                // now rotate
                matrix[row][col] = d;
                matrix[size - 1 - col][row] = a;
                matrix[size - 1 - row][size - 1 - col] = b;
                matrix[col][size - 1 - row] = c;
            }
        }
    }
}

/* 1.8 Zero Matrix */
class ZeroMatrixQuestion {
    private static int[][] emptyMatrix;
    private static int[][] singletonMatrix;
    private static int[][] zeroedSingletonMatrix = {{1}};
    private static int[][] matrix;
    private static int[][] zeroedMatrix =
            {
                    {234,  0,  0,   0, 45, 3},
                    {0,    0,  0,   0,  0, 0},
                    {567,  0,  0,   0, 454, 7},
                    {0,    0, 0,   0,   0, 0},
                    {0,    0,   0,   0, 0, 0},
                    {455,  0, 0,   0, 656, 7}
            };
    private static int[][] oddSizedMatrix, zeroedOddSizedMatrix = {
            {  0, 56,  0, 857,  0},
            {  0,  0,  0,   0,  0},
            {  0, 347, 0, 3,    0},
            {  0, 756, 0, 5,    0},
            {  0,   0, 0, 0,    0}
    };

    private static void initMatrices() {
        emptyMatrix = new int[0][0];
        singletonMatrix = new int[][]{{1}};
        matrix = new int[][]
                {
                        {234, 56, 7, 857, 45, 3},
                        {567, 365, 0, 565, 3, 7},
                        {567, 347, 2, 3, 454, 7},
                        {456, 756, 4, 0, 345, 6},
                        {765, 0, 5, 3, 453, 5},
                        {455, 642, 3, 4, 656, 7}
                };
        oddSizedMatrix = new int[][]
                {
                        {234, 56, 7, 857, 45},
                        {567, 365, 0, 565, 3},
                        {567, 347, 2, 3, 454},
                        {456, 756, 4, 5, 345},
                        {0, 546, 5, 3, 0}
                };
    }

    // in place
    // NOTE: I feel like there's a better way to implement this.
    private static void zeroMatrix(
            @SuppressWarnings("ConstantConditions") int[][] matrix
    ) {
        List<int[]> zeroLocations = new ArrayList<>();

        findZeros(matrix, zeroLocations);

        zeroFoundRowsAndCols(matrix, zeroLocations);
    }

    private static void zeroFoundRowsAndCols(
            int[][] matrix,
            List<int[]> zeroLocations
    ) {
        for (int[] location : zeroLocations) {
            for (int row = 0; row < matrix.length; row++) {
                for (int col = 0; col < matrix[0].length; col++) {
                    if (row == location[0] || col == location[1]) {
                        matrix[row][col] = 0;
                    }
                }
            }
        }
    }

    private static void findZeros(int[][] matrix, List<int[]> zeroLocations) {
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[0].length; col++) {
                if (matrix[row][col] == 0) {
                    int[] location = {row, col};
                    zeroLocations.add(location);
                }
            }
        }
    }

    // utility method for debugging
    @SuppressWarnings("unused")
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.print(element + " ");
            }
            System.out.println();
        }
    }

    static void test() {
        initMatrices();
        zeroMatrix(emptyMatrix);
        assert Arrays.deepEquals(emptyMatrix, new int[0][0]);
        zeroMatrix(singletonMatrix);
        assert Arrays.deepEquals(singletonMatrix, zeroedSingletonMatrix);
        zeroMatrix(matrix);
        assert Arrays.deepEquals(matrix, zeroedMatrix);
        // make sure the logic doesn't fail based on size parity
        zeroMatrix(oddSizedMatrix);
        assert Arrays.deepEquals(oddSizedMatrix, zeroedOddSizedMatrix);
        // null --> Exception since algorithm is in-place
        boolean threwNPE = false;
        try {
            zeroMatrix(null);
        } catch (NullPointerException e) {
            threwNPE = true;
        }
        assert threwNPE;
    }
}

/* 1.9 String Rotation */
class StringRotationQuestion {
    static void test() {
        // a true case
        assert isRotationOf("waterbottle", "erbottlewat");
        // empty strings -> true
        assert isRotationOf("", "");
        // different lengths -> false
        assert !isRotationOf("fghi", "ifg");
        // two nulls -> true
        assert isRotationOf(null, null);
        // null and a string -> false
        assert !isRotationOf(null, "str");
        // symmetry
        assert isRotationOf("abcd", "dabc") ==
                isRotationOf("dabc", "abcd");
        // a false case
        assert !isRotationOf("gnghn", "fggnf");
        // two rotations of the same string
        assert isRotationOf("gngf", "ngfg") ==
                isRotationOf("gngf", "gfgn");
    }

    private static boolean isRotationOf(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;

        return isSubstring(a, b + b);
    }

    private static boolean isSubstring(String a, String b) {
        return b.contains(a);
    }
}