package leetcode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import leetcode.libs.RLinkedList;

/**
 * To provide common utilities for test cases.
 */
@RunWith(value = Parameterized.class)
public class TestBase
{

    private static final String VERBOSE = "verbose";
    private static final String DOPROFILING = "doprofiling";
    protected static final String IF_PREFIX = "src/test/java/leetcode/";

    // list and list of list
    //
    protected static final String s0 = "^";
    protected static final String s1 = "$";
    protected static final String ws = "\\s*";
    protected static final String ww = "\\s+";
    protected static final String b0 = "\\[";
    protected static final String b1 = "\\]";
    protected static final String tk = "[^" + b0 + b1 + "]*";
    protected static final String c0 = "(";
    protected static final String c1 = ")";
    protected static final String nc = "?:";
    protected static final String or = "|";
    protected static final String sp = ",";
    protected static final String as = "*";
    protected static final String hs = "#";
    protected static final String an = ".*";
    protected static final String DL = "(\\s*,\\s*|\\s+)";
    protected static final String LIST_PATTERN_STR = b0 + c0 + tk + c1 + b1;
    protected static final Pattern LIST_PATTERN =
        Pattern.compile(LIST_PATTERN_STR);
    protected static final String LIST_2D_PATTERN_STR = ""
        + b0 + ws + c0
             + LIST_PATTERN_STR
             + c0 + DL
                  + LIST_PATTERN_STR
             + c1 + as
        + c1 + ws + b1;
    protected static final Pattern LIST_2D_PATTERN =
        Pattern.compile(LIST_2D_PATTERN_STR);

    protected static final Pattern COMMENT_LINE_PATTERN =
        Pattern.compile("^\\s*#.*$");
    protected static final Pattern EMPTY_LINE_PATTERN =
        Pattern.compile("^\\s*$");
    protected static DecimalFormat formatter = new DecimalFormat("#,###");

    @Parameters
    public static Iterable<Object[]> data() {
        return new ArrayList<Object[]>();
    };

    @Rule
    public Stopwatch stopwatch = (!doProfiling()) ? null :
        new Stopwatch() {
            @Override
            protected void succeeded(
                long nanos, Description description) {
                System.out.println(description
                    + ":succeeded:"
                    + formatter.format(runtime(TimeUnit.MICROSECONDS))
                    + "(us).");
            }

            @Override
            protected void failed(
                long nanos, Throwable e, Description description) {
                System.out.println(description
                    + ":failed:"
                    + formatter.format(runtime(TimeUnit.MICROSECONDS))
                    + "(us).");
            }

            @Override
            protected void skipped(
                long nanos, AssumptionViolatedException e,
                Description description) {
                System.out.println(description
                    + ":skipped:"
                    // + formatter.format(runtime(TimeUnit.MICROSECONDS))
                    + "(us).");
            }

            @Override
            protected void finished(long nanos, Description description) {
                System.out.println(description
                    + ":finished:"
                    + formatter.format(runtime(TimeUnit.MICROSECONDS))
                    + "(us).");
            }
        };

    protected static boolean isVerbose() {
        return "Y".equals(System.getProperty(VERBOSE));
    }

    protected static boolean doProfiling() {
        return "Y".equals(System.getProperty(DOPROFILING));
    }

    protected static String strip(String s, String set) {
        if (s == null) return null;
        if (set == null || set.length() == 0) return s.trim();
        int low = 0;
        int high = s.length();
        while (low < high) {
            if (set.indexOf(s.charAt(low)) == -1) break;
            low++;
        }
        high--;
        while (high > low) {
            if (set.indexOf(s.charAt(high)) == -1) break;
            high--;
        }
        return s.substring(low, high + 1);
    }

    protected static String nextLine(Scanner sc) {
        String line = null;
        boolean ignored = true;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            if (!shouldIgnore(line)) {
                ignored = false;
                break;
            }
        }
        return !ignored ? trim(line) : null;
    }

    protected static boolean shouldIgnore(CharSequence cs) {
        if (   COMMENT_LINE_PATTERN.matcher(cs).matches()
            || EMPTY_LINE_PATTERN.matcher(cs).matches()
            ) {
            return true;
        } else {
            return false;
        }
    }

    protected static String trim(String line) {
        String newLine = line.trim();
        int c = -1;
        if ( (c = newLine.indexOf('#')) >= 0) {
            newLine = newLine.substring(0, c);
        }
        return newLine;
    }

    protected static <T> List<T> getList(CharSequence cs, Class<T> t) {
        if (cs.length() == 0) return null;
        Matcher m = LIST_PATTERN.matcher(cs);
        return getList(m, t);
    }

    protected static <T> List<T> getList(Matcher m, Class<T> t) {
        if (m == null) return null;
        if (!m.find()) {
            if (isVerbose()) {
                System.out.println("L178:null");
            }
            return null;
        }
        String group = m.group(1).trim();
        if (group.length() == 0) {
            return new ArrayList<T>(0);
        }
        String[] splits = group.split(DL);
        if (isVerbose()) {
            System.out.println("L188:splits:" + Arrays.toString(splits));
        }
        if (Arrays.equals(new String[]{""}, splits)) {
            if (isVerbose()) {
                System.out.println("L192:empty list");
            }
            return new ArrayList<T>(0);
        }
        Object[] array = null;
        if ("java.lang.String".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> s.replace('\"', ' ').trim())
                .toArray(String[]::new);
        } else if ("java.lang.Integer".equals(t.getTypeName())) {
            array = Arrays.stream(splits)
                .map(s -> Integer.valueOf(s))
                .toArray(Integer[]::new);
        } else if ("java.lang.Long".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Long.valueOf(s))
                .toArray(Long[]::new);
        } else if ("java.lang.Short".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Short.valueOf(s))
                .toArray(Short[]::new);
        } else if ("java.lang.Double".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Double.valueOf(s))
                .toArray(Double[]::new);
        } else if ("java.lang.Float".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Float.valueOf(s))
                .toArray(Float[]::new);
        } else if ("java.lang.Byte".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Byte.valueOf(s))
                .toArray(Byte[]::new);
        } else if ("java.lang.Boolean".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Boolean.valueOf(s))
                .toArray(Boolean[]::new);
        } else if ("java.lang.Character".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> s.charAt(1)) // [ "a", "b", "c" ]
                .toArray(Character[]::new);
        }
        // array elements are of type T, so cast is correct
        @SuppressWarnings("unchecked")
        List<T> l = Arrays.asList((T[]) array);

        if (isVerbose()) {
            System.out.println("L220:" + l);
        }
        return l;
    }

    protected static <T> List<List<T>> get2dList(CharSequence cs, Class<T> t,
            boolean allowEmptyList) {
        if (cs.length() == 0) return null;
        Matcher m0 = LIST_2D_PATTERN.matcher(cs);
        if (!m0.find()) {
            if (isVerbose()) {
                System.out.println("L235: 2dList doesn't match.");
            }
            return null;
        }
        List<List<T>> ll = new ArrayList<>();
        Matcher m1 = LIST_PATTERN.matcher(m0.group(1).trim());
        List<T> l = null;
        while ((l = getList(m1, t)) != null) {
            if (allowEmptyList ? true : l.size() > 0) {
                ll.add(l);
            }
        }
        if (isVerbose()) {
            System.out.println("L248:" + ll);
        }
        return ll;
    }

    protected static <T> List<List<T>> get2dList(CharSequence cs, Class<T> t) {
        return get2dList(cs, t, false);
    }

    protected static <T> List<List<T>> get2dList(Scanner sc, Class<T> t,
            boolean allowEmptyList) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        }
        return get2dList(line, t, allowEmptyList);
    }

    protected static <T> List<List<T>> get2dList(Scanner sc, Class<T> t) {
        return get2dList(sc, t, false);
    }

    @SuppressWarnings("unchecked")
    protected static <T> T[][] nestedListTo2dArray(List<List<T>> list, Class<
            ?> t) {
        if (list == null) return null;
        if ("java.lang.Character".equals(t.getTypeName())) {
            return (T[][]) list.stream()
                    .map(subl -> subl.stream().toArray(Character[]::new))
                    .toArray(Character[][]::new);
        } else if ("java.lang.Integer".equals(t.getTypeName())) {
            return (T[][]) list.stream()
                    .map(subl -> subl.stream().toArray(Integer[]::new))
                    .toArray(Integer[][]::new);
        } else if ("java.lang.Long".equals(t.getTypeName())) {
            return (T[][]) list.stream()
                    .map(subl -> subl.stream().toArray(Long[]::new))
                    .toArray(Long[][]::new);
        }
        // ...
        // ...

        // Generic array creation
        // return list.stream()
        //         .map(subl -> subl.stream().toArray(T[]::new)
        //         .toArray(T[][]::new);
        return null;
    }

    protected static <T> void unboxing2dArray(T[][] boxed, Object unboxed,
            Class<?> t) {
        if (boxed == null || boxed.length == 0) {
            return;
        }
        if ("java.lang.Character".equals(t.getTypeName())) {
            for (int i = 0; i < boxed.length; i++) {
                for (int j = 0; j < boxed[0].length; j++) {
                    ((char[][])unboxed)[i][j] = ((Character[][])boxed)[i][j];
                }
            }
        } else if ("java.lang.Integer".equals(t.getTypeName())) {
            for (int i = 0; i < boxed.length; i++) {
                for (int j = 0; j < boxed[0].length; j++) {
                    ((int[][])unboxed)[i][j] = ((Integer[][])boxed)[i][j];
                }
            }
        } else if ("java.lang.Boolean".equals(t.getTypeName())) {
            for (int i = 0; i < boxed.length; i++) {
                for (int j = 0; j < boxed[0].length; j++) {
                    ((boolean[][])unboxed)[i][j] = ((Boolean[][])boxed)[i][j];
                }
            }
        }
    }

    protected static <T> T getSingle(String s, Class<T> t) {
        T obj = null;
        try {
            if ("java.lang.Integer".equals(t.getTypeName()))
                obj = t.cast(Integer.valueOf(s));
            else if("java.lang.Long".equals(t.getTypeName()))
                obj = t.cast(Long.valueOf(s));
            else if("java.lang.Short".equals(t.getTypeName()))
                obj = t.cast(Short.valueOf(s));
            else if("java.lang.Double".equals(t.getTypeName()))
                obj = t.cast(Double.valueOf(s));
            else if("java.lang.Float".equals(t.getTypeName()))
                obj = t.cast(Float.valueOf(s));
            else if("java.lang.Byte".equals(t.getTypeName()))
                obj = t.cast(Byte.valueOf(s));
            else if("java.lang.Boolean".equals(t.getTypeName()))
                obj = t.cast(Boolean.valueOf(s));
        } catch (NumberFormatException nfe) {
            obj = null;
            System.out.println(nfe);
        }

        if (isVerbose()) {
            System.out.println(obj);
        }
        return obj;
    }

    protected static <T> T getSingle(Scanner sc, Class<T> t) {
        T obj = null;
        try {
            if ("java.lang.Integer".equals(t.getTypeName()))
                obj = t.cast(sc.nextInt());
            else if("java.lang.Long".equals(t.getTypeName()))
                obj = t.cast(sc.nextLong());
            else if("java.lang.Short".equals(t.getTypeName()))
                obj = t.cast(sc.nextShort());
            else if("java.lang.Double".equals(t.getTypeName()))
                obj = t.cast(sc.nextDouble());
            else if("java.lang.Float".equals(t.getTypeName()))
                obj = t.cast(sc.nextFloat());
            else if("java.lang.Byte".equals(t.getTypeName()))
                obj = t.cast(sc.nextByte());
            else if("java.lang.Boolean".equals(t.getTypeName()))
                obj = t.cast(sc.nextBoolean());
        } catch (java.util.InputMismatchException ime) {
            return null;
        }

        if (isVerbose()) {
            System.out.println(obj);
        }
        return obj;
    }

    protected static String trimQuotes(String s) {
        if (s == null || !s.matches("\".*\"")) {
            return s;
        } else if (s.equals("\"\"")) {
            return new String("");
        } else {
            return s.substring(1, s.length() - 1);
        }
    }

    protected static int[] getListOfInt(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        };
        List<Integer> l = getList(line, Integer.class);
        if (l == null) {
            return null;
        }
        return l.stream().mapToInt(i->i).toArray();
    }

    protected static List<Integer> getListOfInteger(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        }
        return getList(line, Integer.class);
    }

    protected static Integer getInteger(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        };
        Integer i = getSingle(line, Integer.class);
        if (i == null) {
            return null;
        }
        return i;
    }

    protected static String getString(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        }
        line = line.trim();
        return trimQuotes(line);
    }

    protected static List<String> getListOfString(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        }
        return getList(line, String.class);
    }

    protected static Boolean getBoolean(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        };
        Boolean b = getSingle(line, Boolean.class);
        if (b == null) {
            return null;
        }
        return b;
    }

    protected static <T> void printList2d(List<List<T>> list2d) {
        StringBuilder sb = new StringBuilder("[");
        for (List<T> list : list2d) {
            sb.append("[");
            for (T item : list) sb.append(item).append(",");
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length()-1);
            }
            sb.append("],");
        }
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    @SuppressWarnings("unchecked")
    protected static <T> void sortListofList(List<List<T>> e, Class<?> t) {
         // sort
        if ("java.lang.Integer".equals(t.getTypeName())) {
            Comparator<List<Integer>> comparator = listComparator();
            List<List<Integer>> ce = e.stream()
                                      .map(i -> (List<Integer>)i)
                                      .collect(Collectors.toList());
            ce.sort(comparator);
        }
    }
       // // outer list no order, inner list in order.
        // if (e == o) {
            // return true;
        // }
        // if (e == null || o == null || e.size() != o.size()) {
            // return false;
        // }
        // // compare
        // }
        // return false;
    // }

    // private static <T extends Comparable<? super T>>
        // Comparator<List<T>> listComparator() {
        // return listComparator(Comparator.naturalOrder());
    // }
    protected static <T extends Comparable<? super T>>
        Comparator<List<T>> listComparator() {
        return (l1, l2) -> compareList(l1, l2, Comparator.naturalOrder());
    }

    protected static <T> Comparator<List<T>> listComparator(
        Comparator<? super T> comparator) {
        return (l1, l2) -> compareList(l1, l2, comparator);
    }

    @SuppressWarnings("unchecked")
    protected static <T> int compareList(
        List<? extends T> a,
        List<? extends T> b,
        Comparator<? super T> comparator) {
        if (a == b) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        Iterator<T> ia = (Iterator<T>)a.iterator();
        Iterator<T> ib = (Iterator<T>)b.iterator();
        while (ia.hasNext() && ib.hasNext()) {
            int q = 0;
            if ((q = comparator.compare(ia.next(), ib.next())) != 0) {
                return q;
            }
        }
        if (ia.hasNext()) return 1;
        if (ib.hasNext()) return -1;
        return 0;
    }

    protected static <T extends Comparable<? super T>> RLinkedList<T> getLinkedList(CharSequence cs, Class<T> t) {
        if (cs.length() == 0) return null;
        Matcher m = LIST_PATTERN.matcher(cs);
        return getLinkedList(m, t);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends Comparable<? super T>> RLinkedList<T> getLinkedList(Matcher m, Class<T> t) {
        if (m == null) return null;
        if (!m.find()) {
            if (isVerbose()) {
                System.out.println("L597:null");
            }
            return null;
        }
        String group = m.group(1).trim();
        if (group.length() == 0) {
            return new RLinkedList<>();
        }
        String[] splits = group.split(DL);
        if (isVerbose()) {
            System.out.println("L607:splits:" + Arrays.toString(splits));
        }
        if (Arrays.equals(new String[]{""}, splits)) {
            if (isVerbose()) {
                System.out.println("L611:empty list");
            }
            return new RLinkedList<>();
        }
        Object[] array = null;
        if ("java.lang.String".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> s.replace('\"', ' ').trim())
                .toArray(String[]::new);
        } else if ("java.lang.Integer".equals(t.getTypeName())) {
            array = Arrays.stream(splits)
                .map(s -> Integer.valueOf(s))
                .toArray(Integer[]::new);
        } else if ("java.lang.Long".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Long.valueOf(s))
                .toArray(Long[]::new);
        } else if ("java.lang.Short".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Short.valueOf(s))
                .toArray(Short[]::new);
        } else if ("java.lang.Double".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Double.valueOf(s))
                .toArray(Double[]::new);
        } else if ("java.lang.Float".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Float.valueOf(s))
                .toArray(Float[]::new);
        } else if ("java.lang.Byte".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Byte.valueOf(s))
                .toArray(Byte[]::new);
        } else if ("java.lang.Boolean".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> Boolean.valueOf(s))
                .toArray(Boolean[]::new);
        } else if ("java.lang.Character".equals(t.getTypeName())) {
            array = Arrays
                .stream(splits)
                .map(s -> s.charAt(1)) // [ "a", "b", "c" ]
                .toArray(Character[]::new);
        }
        // array elements are of type T, so cast is correct
        @SuppressWarnings("unchecked")
        RLinkedList<T> l = new RLinkedList<>((T[]) array);

        if (isVerbose()) {
            System.out.println("L629:" + l);
        }
        return l;
    }

    protected static RLinkedList<Integer> getLinkedListOfInteger(Scanner sc) {
        String line = nextLine(sc);
        if (line == null) {
            return null;
        }
        return getLinkedList(line, Integer.class);
    }


}

