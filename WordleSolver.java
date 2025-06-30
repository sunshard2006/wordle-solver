import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

// How to input the result:
// 0 - Gray, 1 - Yellow, 2 - Green
// Input all five colors in a row, without space
// E.g: "12002" (Yellow - Green - Gray - Gray - Green)

// How to start a new round:
// Type in "new" for either the word input or the result input

// How to exit the program:
// Type in "exit" for either the word input or the result input

public class WordleSolver {
    static BufferedReader br = null;
    static BufferedWriter bw = null;
    static ArrayList<String> validWords = new ArrayList<>();
    static Scanner scan = new Scanner(System.in);

    public static int game() {
        // Exit codes:
        // 1: Start a new round
        // 0: Continue current round
        // -1: Exit program
        System.out.print("Enter inputted word: ");
        String wordInput;
        boolean valid;

        do {
            wordInput = scan.nextLine();
            valid = wordInput.toLowerCase().matches("[a-z]{5}");

            if (wordInput.equals("exit")) {
                return -1;
            } else if (wordInput.equals("new")) {
                return 1;
            }

            if (!valid) {
                System.out.print("Please enter a valid 5-letter word: ");
            }
        } while (!valid);

        System.out.print("Enter result: ");
        String resultInput;
        
        do {
            resultInput = scan.nextLine();
            valid = resultInput.matches("[012]{5}");

            if (resultInput.equals("exit")) {
                return -1;
            } else if (resultInput.equals("new")) {
                return 1;
            }

            if (!valid) {
                System.out.print("Please enter valid result: ");
            }
        } while (!valid);

        filterWords(wordInput.toLowerCase(), resultInput);

        return 0;
    }

    public static void green(char ch, int position) {
        String word = null;

        try {
            br = new BufferedReader(new FileReader("result.txt"));

            while ((word = br.readLine()) != null) {
                if (word.charAt(position) == ch) {
                    validWords.add(word);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void yellow(char ch, int position, String wordInput, String resultInput) {
        String word = null;
        int numOfRepeats = checkNumOfDuplicatesYellow(ch, position, wordInput, resultInput);

        try {
            br = new BufferedReader(new FileReader("result.txt"));
            
            while ((word = br.readLine()) != null) {
                boolean valid = (numOfRepeats == 1)? true : checkDuplicateValidity(word, numOfRepeats, ch);

                if (word.contains(String.valueOf(ch)) && word.charAt(position) != ch && valid) {
                    validWords.add(word);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gray(char ch, int position, String wordInput, String resultInput) {
        String word = null;
        int numOfRepeats = checkNumOfDuplicatesGray(ch, wordInput, resultInput);

        try {
            br = new BufferedReader(new FileReader("result.txt"));

            while ((word = br.readLine()) != null) {
                int tempRepeat = numOfRepeats;

                for (int i = 0; i < 5; i++) {
                    if (word.charAt(i) == ch) {
                        tempRepeat--;
                    }
                }

                if (tempRepeat == 0) {
                    validWords.add(word);
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Writes results from the validWords array and then clear it
    public static void writeFile() {
        boolean firstLine = true;
        try {
            bw = new BufferedWriter(new FileWriter("result.txt"));

            for (String i : validWords) {
                if (firstLine) {
                    firstLine = false;
                } else {
                    bw.newLine();
                }

                bw.write(i);
            }

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        validWords.clear();
    }

    // Copies all 5 letters word to result.txt for the start of a round
    public static void copyToResult() {
        String word;
        try {
            br = new BufferedReader(new FileReader("all_5_letter_words.txt"));
            bw = new BufferedWriter(new FileWriter("result.txt"));

            while ((word = br.readLine()) != null) {
                bw.write(word);
                bw.newLine();
            }

            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void filterWords(String wordInput, String resultInput) {
        // Checks green first, then yellow, then gray
        for (int i = 0; i < 5; i++) {
            char ch = wordInput.charAt(i);
            if (resultInput.charAt(i) == '2') {
                green(ch, i);
                writeFile();
            }
        }

        for (int i = 0; i < 5; i++) {
            char ch = wordInput.charAt(i);
            if (resultInput.charAt(i) == '1') {
                yellow(ch, i, wordInput, resultInput);
                writeFile();
            }
        }

        for (int i = 0; i < 5; i++) {
            char ch = wordInput.charAt(i);
            if (resultInput.charAt(i) == '0') {
                gray(ch, i, wordInput, resultInput);
                writeFile();
            }
        }

        System.out.println("-- Success, check the result file for filtered words --");
    }

    // Functions for dealing with repeating letters 
    public static int checkNumOfDuplicatesYellow(char ch, int position, String wordInput, String resultInput) {
        int numOfRepeats = 0;

        for (int i = 0; i < 5; i++) {
            if (wordInput.charAt(i) == ch && resultInput.charAt(i) != '0') {
                numOfRepeats++;
            }
        }

        return numOfRepeats;
    }

    public static int checkNumOfDuplicatesGray(char ch, String wordInput, String resultInput) {
        int numOfRepeats = 0;

        for (int i = 0; i < 5; i++) {
            if (wordInput.charAt(i) == ch && resultInput.charAt(i) != '0') {
                numOfRepeats++;
            }
        }

        return numOfRepeats;
    }

    public static boolean checkDuplicateValidity(String word, int numOfRepeats, char ch) {
        int tempRepeat = numOfRepeats;

            for (int i = 0; i < 5; i++) {
                if (tempRepeat == 0) {
                    return true;
                }
                if (word.charAt(i) == ch) {
                    tempRepeat--;
                }
            }

        return false;
    }

    public static void main(String args[]) {
        int exitCode;

        do {
            copyToResult();
            while ((exitCode = game()) == 0);
            if (exitCode == 1) {
                System.out.println("-- New round started --");
            }
            
        } while (exitCode != -1);

        System.out.println("-- Program successfully exited! --");
        scan.close();
    }
}