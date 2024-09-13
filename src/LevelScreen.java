import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class LevelScreen extends Screen implements ActionListener {
    // static constants: the campaign, and the one and only Runner
    // instance used by all LevelScreens to test arbitrary code
    private static final Level[] LEVELS = new Level[10];
    private static final Runner runner = new Runner(
        // store user code in an output folder of current directory
        // (we'll also stick with the class name "Code" throughout)
        "./JavaiserUserOutput/",
        "Code", "solution"
    );

    // which level this screen represents
    private final int levelIndex;
    private final Level level;

    // state variables that dictate when to offer user solution
    // (we don't want to do that before they've tried hard enough)
    private int failures = 0;
    private boolean hintUsed = false;

    // input components accessed by multiple instance methods
    private final JTextArea sandbox;
    private final JButton submit;
    private JButton hint = null;

    // clip played when user fails, saved globally so we can
    // stop the one already playing if user fails again in
    // quick succession (i.e. prevent overlapping of sounds)
    private Clip womp;

    // my first time using a static initializer :D
    // used to populate the ten levels; see Level.java
    // for more on how level data is structured (it's
    // well-designed and self-documenting, so I've omitted
    // comments below)
    static {
        LEVELS[0] = new Level(
        "Take two integers and return their sum.",
    "int solution(int a, int b)",
            new Class[]{int.class, int.class},
    "public static int solution(int a, int b) {\n" +
            "    // add the two numbers and return\n" +
            "    return a + b;\n" +
            "}"
        );

        LEVELS[0].cases.put(new Object[]{0, 0}, 0);
        LEVELS[0].cases.put(new Object[]{32, -129}, -97);
        LEVELS[0].cases.put(new Object[]{143, 20}, 163);
        LEVELS[0].cases.put(new Object[]{-1, -2}, -3);

        LEVELS[1] = new Level(
            "Check if a positive integer is even.",
            "boolean solution(int n)",
            new Class[]{int.class},
    "public static boolean solution(int n) {\n" +
            "    // modulus returns the remainder of division,\n" +
            "    // so if the remainder when dividing by 2 is 0,\n" +
            "    // the number is even\n" +
            "    return n % 2 == 0;\n" +
            "}"
        );
        LEVELS[1].hint = "Remember the modulus operator?";

        LEVELS[1].cases.put(new Object[]{4}, true);
        LEVELS[1].cases.put(new Object[]{15}, false);
        LEVELS[1].cases.put(new Object[]{20}, true);
        LEVELS[1].cases.put(new Object[]{888}, true);
        LEVELS[1].cases.put(new Object[]{1}, false);

        LEVELS[2] = new Level(
            "Get the maximum value of an array of integers.",
            "int solution(int[] numbers)",
            new Class[]{int[].class},
    "public static int solution(int[] n) {\n" +
            "    // initialize 'max' to the first element in the array\n" +
            "    int max = n[0];\n" +
            "\n" +
            "    // loop through each number in the array\n" +
            "    for (int num : n) {\n" +
            "        // update 'max' if a larger number is found\n" +
            "        if (num > max) {\n" +
            "            max = num;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    // return the largest number\n" +
            "    return max;\n" +
            "}"
        );
        LEVELS[2].hint = "Maybe it's smart to keep track of the *current* "
            + "maximum value, as you're iterating through the array.";

        LEVELS[2].cases.put(new Object[]{new int[]{1, 2, 3, 4}}, 4);
        LEVELS[2].cases.put(new Object[]{new int[]{-1, -2, -3}}, -1);
        LEVELS[2].cases.put(new Object[]{new int[]{5, 5, 5, 5, 0}}, 5);
        LEVELS[2].cases.put(new Object[]{new int[]{-5, -55, 10, 50, 30}}, 50);

        LEVELS[3] = new Level(
            "Count the number of vowels (AEIOU) in a string.",
            "int solution(String s)",
            new Class[]{String.class},
    "public static int solution(String s) {\n" +
            "    int count = 0;\n" +
            "    \n" +
            "    // convert string to lowercase and loop through each character\n" +
            "    for (char c : s.toLowerCase().toCharArray()) {\n" +
            "        // increment count if character is a vowel\n" +
            "        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {\n" +
            "            count++;\n" +
            "        }\n" +
            "    }\n" +
            "    \n" +
            "    return count;\n" +
            "}"
        );
        LEVELS[3].hint = "Vowels aren't case sensitive!";

        LEVELS[3].cases.put(new Object[]{"hello"}, 2);
        LEVELS[3].cases.put(new Object[]{"world"}, 1);
        LEVELS[3].cases.put(new Object[]{""}, 0);
        LEVELS[3].cases.put(new Object[]{"bc dfghj klbc dfghjkl"}, 0);
        LEVELS[3].cases.put(new Object[]{"aEIoUuA"}, 7);

        LEVELS[4] = new Level(
            "Find the length of the longest word in a sentence. "
                + "You can safely assume that there are no hyphenated words or other shenanigans.",
            "int solution(String s)",
            new Class[]{String.class},
    "public static int solution(String s) {\n" +
            "    int maxLength = 0;\n" +
            "\n" +
            "    // split string into words by spaces, and loop\n" +
            "    for (String word : s.split(\" \")) {\n" +
            "        // update maxLength if the current word is longer\n" +
            "        if (word.length() > maxLength) {\n" +
            "            maxLength = word.length();\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    return maxLength;\n" +
            "}"
        );
        LEVELS[4].hint = "This is basically Level 2, except you have to find a way "
            + "to get the length of words (sequences of characters separated by spaces).";

        LEVELS[4].cases.put(new Object[]{"Hello world"}, 5);
        LEVELS[4].cases.put(new Object[]{"I love programming"}, 11);
        LEVELS[4].cases.put(new Object[]{""}, 0);
        LEVELS[4].cases.put(new Object[]{"O"}, 1);
        LEVELS[4].cases.put(new Object[]{"A quick brown fox"}, 5);

        LEVELS[5] = new Level(
            "Take two positive integers and return their greatest common divisor.",
            "int solution(int a, int b)",
            new Class[]{int.class, int.class},
    "// This level actually has an incredibly elegant recursive solution.\n" +
            "// To learn more, search up the \"Euclidean algorithm\".\n\n" +
            "public static int solution(int a, int b) {\n" +
            "    int gcd = 1;\n" +
            "\n" +
            "    // we basically brute force all possible divisors;\n" +
            "    // iterate, from 1 to the smaller of the two numbers\n" +
            "    for (int i = 1; i <= Math.min(a, b); i++) {\n" +
            "        // if i divides both numbers, we have a bigger gcd\n" +
            "        if (a % i == 0 && b % i == 0) {\n" +
            "            gcd = i;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    // by this point, we'll have found the greatest common divisor\n" +
            "    return gcd;\n" +
            "}"
        );
        LEVELS[5].hint = "Try using increasing numbers and see if they divide both inputs.";

        LEVELS[5].cases.put(new Object[]{12, 8}, 4);
        LEVELS[5].cases.put(new Object[]{14, 21}, 7);
        LEVELS[5].cases.put(new Object[]{48, 180}, 12);
        LEVELS[5].cases.put(new Object[]{101, 103}, 1);
        LEVELS[5].cases.put(new Object[]{1, 1999}, 1);
        LEVELS[5].cases.put(new Object[]{1999, 1999}, 1999);
        LEVELS[5].cases.put(new Object[]{40, 20}, 20);

        LEVELS[6] = new Level(
            "Find the longest palindromic substring in a string. If there are multiple, " +
                "return the first one. You're guaranteed small inputs because I'm nice :)",
            "String solution(String s)",
            new Class[]{String.class},
    "public static String solution(String s) {\n" +
            "    // return early if our input is trivial\n" +
            "    if (s.length() < 2) return s;\n" +
            "\n" +
            "    int start = 0;\n" +
            "    int maxLength = 1;\n" +
            "\n" +
            "    // loop through each character as a potential PALINDROME CENTER\n" +
            "    for (int i = 0; i < s.length(); i++) {\n" +
            "        // check for longest odd- and even- length\n" +
            "        // palindromes that we can form from here \n" +
            "        int lenOdd = expandFromCenter(s, i, i);\n" +
            "        int lenEven = expandFromCenter(s, i, i + 1);\n" +
            "\n" +
            "        // get the longer palindrome\n" +
            "        int len = Math.max(lenOdd, lenEven);\n" +
            "        \n" +
            "        // update start and maxLength if a strictly longer palindrome\n" +
            "        // is found (so if multiple tie for longest, we keep only the first)\n" +
            "        if (len > maxLength) {\n" +
            "            start = i - (len - 1) / 2;\n" +
            "            maxLength = len;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    return s.substring(start, start + maxLength);\n" +
            "}\n" +
            "\n" +
            "private static int expandFromCenter(String s, int left, int right) {\n" +
            "    // expand outwards while the characters match and the bounds are valid\n" +
            "    while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {\n" +
            "        left--;\n" +
            "        right++;\n" +
            "    }\n" +
            "\n" +
            "    // return length of the palindrome found\n" +
            "    return right - left - 1;\n" +
            "}"
        );
        LEVELS[6].hint = "Since you're guaranteed small inputs, why not just brute force :P";

        LEVELS[6].cases.put(new Object[]{"babad"}, "bab");
        LEVELS[6].cases.put(new Object[]{"cbbbbbbbd"}, "bbbbbbb");
        LEVELS[6].cases.put(new Object[]{"a"}, "a");
        LEVELS[6].cases.put(new Object[]{"tunaracecartuna"}, "racecar");
        LEVELS[6].cases.put(new Object[]{"abacdfgdcaba"}, "aba");

        LEVELS[7] = new Level(
            "Convert a positive integer not exceeding 3000 into Roman numerals.",
            "String solution(int number)",
            new Class[]{int.class},
    "private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();\n" +
            "\n" +
            "// static block to initialize Roman numeral mappings\n" +
            "static {\n" +
            "    map.put(1000, \"M\");\n" +
            "    map.put(900, \"CM\");\n" +
            "    map.put(500, \"D\");\n" +
            "    map.put(400, \"CD\");\n" +
            "    map.put(100, \"C\");\n" +
            "    map.put(90, \"XC\");\n" +
            "    map.put(50, \"L\");\n" +
            "    map.put(40, \"XL\");\n" +
            "    map.put(10, \"X\");\n" +
            "    map.put(9, \"IX\");\n" +
            "    map.put(5, \"V\");\n" +
            "    map.put(4, \"IV\");\n" +
            "    map.put(1, \"I\");\n" +
            "}\n" +
            "\n" +
            "public static String solution(int number) {\n" +
            "    // get the largest key less than or equal to current number\n" +
            "    int l = map.floorKey(number);\n" +
            "\n" +
            "    // BASE CASE: if our number exactly matches it,\n" +
            "    // simply return the corresponding Roman numeral\n" +
            "    if (number == l) {\n" +
            "        return map.get(number);\n" +
            "    }\n" +
            "\n" +
            "    // RECURSIVE CASE: subtract the largest value, recurse to solve\n" +
            "    // this smaller, similar subproblem, and concatenate the result\n" +
            "    return map.get(l) + solution(number - l);\n" +
            "}"
        );
        LEVELS[7].hint = "Got a feeling that a mapping (1 maps to I, 4 to IV, 5 to V, 9 to IX...) " +
            "and a greedy algorithm might work wonders here...";

        LEVELS[7].cases.put(new Object[]{2737}, "MMDCCXXXVII");
        LEVELS[7].cases.put(new Object[]{949}, "CMXLIX");
        LEVELS[7].cases.put(new Object[]{1981}, "MCMLXXXI");
        LEVELS[7].cases.put(new Object[]{99}, "XCIX");
        LEVELS[7].cases.put(new Object[]{3000}, "MMM");
        LEVELS[7].cases.put(new Object[]{1}, "I");

        LEVELS[8] = new Level(
        "(Mr Lauder's deleted Part C problem) Determine if two lowercase words are "
            + "isomorphic -- that is, if the letters in one can be replaced to form the other. "
            + "No two letters may map to the same letter. Example: 'paper' and 'title' are "
            + "isomorphic. 'egg' and 'ebb', too. 'good' and 'day' are not. ",
    "boolean solution(String s, String t)",
            new Class[]{String.class, String.class},
    "// Solution from StackOverflow:\n" +
            "// https://stackoverflow.com/questions/74763156/check-if-the-two-given-string-are-isomorphic\n" +
            "\n" +
            "// They provide an excellent explanation,\n" +
            "// so we won't reinvent the wheel here.\n" +
            "\n" +
            "public static boolean solution(String s, String t) {\n" +
            "    if (s.length() != t.length()) return false; // early kill, if length of these string is not equal they are not isomorphic\n" +
            "\n" +
            "    String first = s.toLowerCase();\n" +
            "    String second = t.toLowerCase();\n" +
            "\n" +
            "    Set<List<Integer>> positions1 = getPositions(first);\n" +
            "    Set<List<Integer>> positions2 = getPositions(second);\n" +
            "\n" +
            "    return positions1.equals(positions2);\n" +
            "}\n" +
            "\n" +
            "private static Set<List<Integer>> getPositions(String str) {\n" +
            "\n" +
            "    Map<Character, List<Integer>> positionsByCharacter = new HashMap<>();\n" +
            "\n" +
            "    for (int i = 0; i < str.length(); i++) {\n" +
            "        char next = str.charAt(i);\n" +
            "\n" +
            "        positionsByCharacter\n" +
            "            .computeIfAbsent(next, k -> new ArrayList<>())\n" +
            "            .add(i);\n" +
            "    }\n" +
            "\n" +
            "    return new HashSet<>(positionsByCharacter.values());\n" +
            "}"
        );
        LEVELS[8].hint = "If you mapped each distinct letter in a string to the indices "
            + "at which it appears, what underlying structure is there for isomorphism?";

        LEVELS[8].cases.put(new Object[]{"paper", "title"}, true);
        LEVELS[8].cases.put(new Object[]{"egg", "ebb"}, true);
        LEVELS[8].cases.put(new Object[]{"a", "b"}, true);
        LEVELS[8].cases.put(new Object[]{"good", "day"}, false);
        LEVELS[8].cases.put(new Object[]{"paper", "aaaaa"}, false);
        LEVELS[8].cases.put(new Object[]{"zsertyu", "mjuytru"}, false);

        LEVELS[9] = new Level(
            "You're given a rectangular 2D maze represented by an array of strings, "
            + " where X's are walls, O's are empty spaces, A is Start and B is End. You're "
            + "also given an integer, let's call it N. Determine whether you can get from "
            + "A to B in no more than N moves (up, down, left, right).",
            "boolean solution(String[] maze, int n)",
            new Class[]{String[].class, int.class},
    "public static boolean solution(String[] maze, int n) {\n" +
            "    int rows = maze.length;\n" +
            "    int cols = maze[0].length();\n" +
            "    int[] start = new int[2];\n" +
            "    int[] end = new int[2];\n" +
            "\n" +
            "    // locate Start (A) and End (B)\n" +
            "    for (int i = 0; i < rows; i++) {\n" +
            "        for (int j = 0; j < cols; j++) {\n" +
            "            if (maze[i].charAt(j) == 'A') {\n" +
            "                start[0] = i;\n" +
            "                start[1] = j;\n" +
            "            } else if (maze[i].charAt(j) == 'B') {\n" +
            "                end[0] = i;\n" +
            "                end[1] = j;\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    // directions for up, down, left, right movements\n" +
            "    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};\n" +
            "\n" +
            "    // BFS initialization; queue entry structure:\n" +
            "    // {row, col, howManyMovesItTookToGetHere}\n" +
            "    Queue<int[]> queue = new LinkedList<>();\n" +
            "    boolean[][] visited = new boolean[rows][cols];\n" +
            "    queue.add(new int[]{start[0], start[1], 0});\n" +
            "    visited[start[0]][start[1]] = true;\n" +
            "\n" +
            "    // BFS loop\n" +
            "    while (!queue.isEmpty()) {\n" +
            "        int[] current = queue.poll();\n" +
            "        int row = current[0];\n" +
            "        int col = current[1];\n" +
            "        int moves = current[2];\n" +
            "\n" +
            "        // check if we reached our goal, B\n" +
            "        if (row == end[0] && col == end[1]) {\n" +
            "            // if so, return whether we reached it in no more than n moves\n" +
            "            return moves <= n;\n" +
            "        }\n" +
            "\n" +
            "        // explore in all four directions\n" +
            "        for (int[] direction : directions) {\n" +
            "            int newRow = row + direction[0];\n" +
            "            int newCol = col + direction[1];\n" +
            "\n" +
            "            // check if the move is valid\n" +
            "            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols &&\n" +
            "                maze[newRow].charAt(newCol) != 'X' && !visited[newRow][newCol]) {\n" +
            "                // if it is, record that we'll have visited it and add to BFS queue\n" +
            "                visited[newRow][newCol] = true;\n" +
            "                queue.add(new int[]{newRow, newCol, moves + 1});\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    // if there exists no path between A and B (which will be\n" +
            "    // the case if BFS queue ends up empty without finding B,\n" +
            "    // leading us here), then we definitely can't reach it in n moves\n" +
            "    return false;\n" +
            "}"
        );
        LEVELS[9].hint = "Classic elementary graph theory problem. "
            + "Do you know about breadth-first search?";

        LEVELS[9].cases.put(new Object[]{new String[]{"AB"}, 1}, true);
        LEVELS[9].cases.put(new Object[]{new String[]{"AB"}, 0}, false);
        LEVELS[9].cases.put(new Object[]{new String[]{"AOOOOOOO", "XXXOOOXX", "OOOOXOOO", "XOOXXXXX", "XXOOOOOB"}, 12}, false);
        LEVELS[9].cases.put(new Object[]{new String[]{"AOOOOOOO", "XXXOOOXX", "OOOOXOOO", "XOOXXXXX", "XXOOOOOB"}, 13}, true);
        LEVELS[9].cases.put(new Object[]{new String[]{"AOXO", "OOXO", "XXXB"}, 999}, false);
        LEVELS[9].cases.put(new Object[]{new String[]{"AOOO", "OXXO", "OXBX", "OOOO"}, 8}, true);
    }

    public LevelScreen(int levelIndex) {
        // initialize with border layout and padding around
        // the actual content (that's what EmptyBorder is for)
        super();
        setLayout(new BorderLayout(0, 25));
        setBorder(new EmptyBorder(50, 30, 40, 30));

        // record info about the level this screen represents
        this.levelIndex = levelIndex;
        level = LEVELS[levelIndex];

        // top part of the layout, containing title and problem description;
        // setOpaque(false) so the inherited BG color from Screen shines through
        JPanel top = new JPanel(new BorderLayout());
        top.setPreferredSize(new Dimension(260, 260));
        top.setOpaque(false);
        add(top, BorderLayout.NORTH);

        // create bold, centered title with a bit of extra height to space things out
        JLabel title = new JLabel("Level " + levelIndex);
        title.setForeground(Utils.TEXT_CLR_HIGH_CONTRAST);
        title.setPreferredSize(new Dimension(80, 80));
        title.setFont(new Font("Rasa", Font.BOLD, 48));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.TOP);
        top.add(title, BorderLayout.NORTH);

        // create problem description; longer ones (levels 8 & 9) get a smaller font
        JLabel problem = new JLabel(Utils.html(level.desc + "\n\nSignature: " + level.signature));
        if (levelIndex == 8 || levelIndex == 9) problem.setFont(new Font("", Font.PLAIN, 20));
        else problem.setFont(new Font("", Font.PLAIN, 26));
        problem.setForeground(Utils.TEXT_CLR);
        problem.setVerticalAlignment(SwingConstants.TOP);
        top.add(problem, BorderLayout.CENTER);

        // create programming sandbox with slight padding,
        // a monospace font, and higher BG contrast
        sandbox = new JTextArea();
        sandbox.setFont(new Font("Courier", Font.PLAIN, 18));
        sandbox.setBorder(new EmptyBorder(5, 5, 5, 5));
        sandbox.setBackground(Utils.CODE_BG_CLR);
        sandbox.setForeground(Utils.TEXT_CLR);
        sandbox.setCaretColor(Utils.TEXT_CLR);

        // on later levels, sandbox is populated with this placeholder comment
        sandbox.setText("// Write your solution here!");

        // on level 0, sandbox is populated with a guide-you-by-the-hand message
        if (levelIndex == 0) {
            sandbox.setText(
                "// Welcome to the tutorial level! ( ^_^)／\n"
                + "// I've written half of it for you. You're welcome.\n\n"
                + "// Notice how there's no public class and all that.\n"
                + "// That's already been done. java.util.* imported, too.\n\n"
                + "public static int solution(int a, int b) {\n"
                + "    // What should you write here?\n"
                + "}"
            );
        }

        // on level 1, user is less dependent, but we still help 'em out a little
        if (levelIndex == 1) {
            sandbox.setText(
                "// Let me help you once more.\n"
                + "// After this one, you're on your own.\n\n"
                + "public static boolean solution(int n) {\n"
                + "    // Complete this method!\n"
                + "}"
            );
        }

        // wrap sandbox in a JScrollPane to allow for bidirectional scroll overflow
        JScrollPane scrollPane = new JScrollPane(
            sandbox,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        // use a thin border as a workaround to get rid of annoying white outline
        scrollPane.setBorder(BorderFactory.createLineBorder(Utils.CODE_BG_CLR, 2));
        add(scrollPane, BorderLayout.CENTER);

        // bottom part of the layout; contains submit button, and maybe hint button
        JPanel bottom = new JPanel(new GridLayout(1, 2, 25, 0));
        bottom.setPreferredSize(new Dimension(80, 80));
        bottom.setOpaque(false);
        add(bottom, BorderLayout.SOUTH);

        // create button to submit solution
        submit = new JButton("SUBMIT");
        submit.setFont(new Font("", Font.ITALIC, 28));
        submit.addActionListener(this);
        bottom.add(submit);

        // if this level has a hint, create button to view it
        if (level.hint != null) {
            hint = new JButton("I WANT A HINT");
            hint.setFont(new Font("", Font.ITALIC, 28));
            hint.addActionListener(this);
            bottom.add(hint);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // if this level has a hint and hint button pressed, show it
        if (hint != null && e.getSource() == hint) {
            showHint();
        }

        // otherwise, if event comes from any source
        // other than submit, it's none of our business
        if (e.getSource() != submit) {
            return;
        }

        // now that it is our business, let's get working:
        // wrap user's code in a class and import util classes for them
        String program = String.format(
            "import java.util.*; public class Code { %s }", sandbox.getText());

        // then iterate through each test input argument array...
        for (Object[] key : level.cases.keySet()) {
            // cheat code used for demo purposes
            if (sandbox.getText().equals("next")) {
                // skip all test cases, effectively passing the level
                break;
            }

            // easter egg to poke fun at Andrew
            if (sandbox.getText().equals("dvd")) {
                showEasterEgg();
                return;
            }

            // run solution method with input, and get a result
            // (note that this result might well be an error flag!)
            Object result = runner.run(program, level.parameterTypes, key);

            // console logging for demo purposes
            System.out.println("Running test case: " + Arrays.deepToString(key) + "   Getting result: " + result);

            // below are four failure cases, each corresponding to an
            // appropriate message to help user debug their code
            if (result == Runner.Err.COMPILE_TIME_ERROR) {
                wompWomp("Your code did not compile! Check for syntax errors.");
                return;
            }

            if (result == Runner.Err.PROGRAM_ENTRY_ERROR) {
                wompWomp("I can't find your solution method. Make sure it's public static and matches signature!");
                return;
            }

            if (result == Runner.Err.RUNTIME_ERROR) {
                wompWomp("Program crashed with a " + runner.getLatestRuntimeExceptionName() + ". Debugging time!");
                return;
            }

            // check if what user returns matches expected output
            if (!result.equals(level.cases.get(key))) {
                wompWomp("Program ran but failed test cases. Debugging time!");
                return;
            }
        }

        // if we make it all the way here, that means no failure
        // caused the code above to abort, which means user's code
        // runs and passes all test cases. They pass the level!
        if (levelIndex < 9) {
            // there are still more levels -- get 'em to the next one
            Utils.playSound("success.wav");
            replaceWith(new LevelScreen(levelIndex + 1));
        } else  {
            // this means they've beat the game -- well done!
            Utils.playSound("yay.wav");
            replaceWith(new WinScreen());
        }
    }

    // show level hint in a JOptionPane pop-up
    private void showHint() {
        // make sure to track that a hint has been used
        // (for solution offering after much struggle)
        hintUsed = true;

        JOptionPane.showMessageDialog(
            null, wrapInJLabel(level.hint), "Hint",
            JOptionPane.INFORMATION_MESSAGE, Utils.icon("hint.png", 70, 70)
        );
    }

    // show failure message in a JOptionPane pop-up while playing
    // a womp womp sound; if multiple fails already + hint used
    // (the latter only when there IS a hint, of course; for levels
    // with no hints, we adjust the magnitude of former accordingly),
    // include "Show Solution?" option (which does exactly that)
    private void wompWomp(String message) {
        playWompWompSound();

        // keep track of how many times user has failed
        failures++;

        // only show user a way out after they've struggled
        if (failures > 6 && hintUsed || level.hint == null && failures > 7) {
            showFailPopupWithSolutionOption(message);
            return;
        }

        // otherwise, just show simple failure dialog
        JOptionPane.showMessageDialog(
            null, wrapInJLabel(message), "Womp Womp",
            JOptionPane.INFORMATION_MESSAGE, Utils.gif("womp.gif", 70, 70)
        );
    }

    // helper method used by wompWomp; see above
    private void showFailPopupWithSolutionOption(String message) {
        // create OK & Show Solution button
        JButton ok = new JButton("OK");
        JButton showSolution = new JButton("Show Solution?");

        // we need them in an array of options
        Object[] options = { ok, showSolution };

        // this closure'd single-item list will be mutated to store user choice
        final int[] choice = { -1 };

        // since we're using custom option buttons, they don't
        // automatically close our pop-up window when clicked
        for (int i = 0; i < options.length; i++) {
            // cast to a JButton because we're using an Object array
            JButton option = (JButton) options[i];

            // duplicate index in temporary final variable for closure
            final int index = i;

            // when each button clicked, get and close the pop-up
            option.addActionListener(e -> {
                choice[0] = index;
                Window window = SwingUtilities.getWindowAncestor(option);
                window.dispose();
            });
        }

        JOptionPane.showOptionDialog(
            null, wrapInJLabel(message), "Womp Womp",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            Utils.gif("womp.gif", 70, 70), options, ok
        );

        // if chosen index is that of the show solution button index, proceed
        if (choice[0] == 1) {
            int confirmation = JOptionPane.showConfirmDialog(
                null, wrapInJLabel("Warning: Your current code will be erased. Replace it with official solution?"),
                "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null
            );

            if (confirmation == JOptionPane.YES_OPTION) {
                sandbox.setText(level.solution);
            }
        }
    }

    // wrap content in JLabel for use in showHint and
    // wompWomp, so we can style it & constrain size
    private JLabel wrapInJLabel(String message) {
        // we have to first make it multiline before putting it in a JLabel
        JLabel label = new JLabel(Utils.html(message));

        // change font, indirectly set size of JOptionPane pop-up, and align text
        label.setFont(new Font("", Font.PLAIN, 18));
        label.setPreferredSize(new Dimension(370, 200));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setVerticalAlignment(SwingConstants.TOP);

        return label;
    }

    private void playWompWompSound() {
        // if there's already a womp-womp sound playing, stop it
        if (womp != null) {
            womp.stop();
            womp = null;
        }

        // play a new womp-womp sound and save it
        // for the above check in the next call
        womp = Utils.playSound("womp.wav");
    }

    private void showEasterEgg() {
        JFrame newFrame = new JFrame("DVD");
        newFrame.setResizable(false);
        newFrame.add(new DVDPanel());
        newFrame.pack();
        newFrame.setVisible(true);
    }
}
