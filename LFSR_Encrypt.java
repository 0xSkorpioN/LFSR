package LFSR;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LFSR_Encrypt {
    BufferedReader Buff = new BufferedReader(new FileReader("C:\\Users\\abdallah\\OneDrive\\Documents\\Examples Java\\LFSR\\src\\LFSR\\inputs.txt"));
    private String initial = Buff.readLine();
    private String feedbackGates = Buff.readLine();
    private int numberOfDiscardedBits = 100;
    private String feedbackGatesIndeces;

    public LFSR_Encrypt() throws IOException {
        this.feedbackGatesIndeces = getFeedbackBitsIndeces(feedbackGates);
//		System.out.println("The Indeces of the Feedback Gates: " + this.feedbackGatesIndeces);
    }

    public char XOR(char x, char y) {
        if (x == y) {
            return '0';
        } else {
            return '1';
        }
    }
    // it returns the result from XOR and shifting operations
    public char getNewInput(String bits) {

        char res = XOR(bits.charAt(0), bits.charAt(1));

        for (int i = 2; i < bits.length(); i++) {
            res = XOR(res, bits.charAt(i));
        }

        return res;
    }

    // it return the indeces of the feedbackBits that will be XORed together
    public String getFeedbackBitsIndeces(String gates) {

        String feedbackGatesIndeces = "";

        for (int i = 0; i < gates.length(); i++) {
            if (gates.charAt(i) == '1') {
                feedbackGatesIndeces += i;
            }
        }

        return feedbackGatesIndeces;
    }
    // it returns the bits that occur in the feedbackBits indeces
    public String getFeedbackBits(String bits, String indeces) {

        String feedbackBits = "";
        for (int i = 0; i < indeces.length(); i++) {
//			System.out.println(indeces.charAt(i)-48);
            feedbackBits += bits.charAt(indeces.charAt(i)-48);
        }
        return feedbackBits;
    }
    // it generates the key
    public String getKey() {

        String oldRow = this.initial;

        String key = "";
        String feedbackBitsValues = "";
        char newInput = ' ';


        // (2 ^ 9) -1 = 511
        for (int i = 0; i < 511; i++) {

            String newRow = "";

            key += oldRow.charAt(8);
//			System.out.println("The Cipher Text: " + key);

            feedbackBitsValues = getFeedbackBits(oldRow, this.feedbackGatesIndeces);
//			System.out.println("The Feedback Bits Values: " + feedbackBitsValues);

            newInput = getNewInput(feedbackBitsValues);
//			System.out.println("The New Input: " + newInput);

            newRow += newInput;

            for (int j = 0; j < 8; j++) {
                newRow += oldRow.charAt(j);
            }
            oldRow = newRow;
//			System.out.println("The New Row: " + oldRow);
        }

        return key;
    }
    // it transforms the plain text to cipher text or vice versa
    private String transform(String text, String key) {

        String plainText = "";
        int dis = this.numberOfDiscardedBits;

        for (int i = dis, j = 0; i < text.length()+dis && j < text.length() ; i++, j++) {
            plainText += XOR(text.charAt(j), key.charAt(i));
        }

        return plainText;
    }

    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub

        LFSR_Encrypt en = new LFSR_Encrypt();

        String key = en.getKey();
        System.out.println("The Key: " + key);

        String plainText = "11001111";

        String cipher = en.transform(plainText, key);
        System.out.println("The Cipher Text: " + cipher);

        String plain = en.transform(cipher, key);
        System.out.println("The Plain Text: " + plain);

    }
}
