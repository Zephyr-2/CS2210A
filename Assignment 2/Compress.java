/**
 * Compresses a file (defined by a String parameter) using the compression algorithm outlined in
 * the outline for CS 2210A Assignment 2, by Western University.
 *
 * @author Robert Meagher   2502749364
 * @since October 6, 2015
 */

import java.io.*;
import java.util.Scanner;

public class Compress {
    final static int MAX_SIZE = 4096; // The maximum number of codes that can be stored by the compression algorithm
    final static String COMPRESS_EXT = ".zzz"; // The extension to write a compressed file to

    public static void main(String args[]) {
        String fileName = "";

        if(args.length > 0)
            fileName = args[0];

        new Compress(fileName);
    }

    /**
     * Creates a new Compress class that attempts to compress the file with the given file name
     * @param fileName The name of the file to compress
     */
    public Compress(String fileName) {
        fileName = getValidFileName(fileName, "File to compress (include extension), or 'exit' to quit", "exit");

        if(fileName != null)
            compress(fileName);
    }

    /**
     * Checks if a given file name is valid. If it isn't, prompts for a new file name until a
     * valid name is given.
     * @param fileName The file name to check first
     * @param errorMessage The error prompt if a file name is invalid
     * @return A String that contains a valid file name
     */
    private String getValidFileName(String fileName, String errorMessage, String exitPrompt) {
        Scanner scanner = new Scanner(System.in);
        File file = new File(fileName);

        // Loop until a valid file name is given
        while(!file.exists() || file.isDirectory()) {
            System.out.println(errorMessage);
            fileName = scanner.nextLine();

            if(fileName.equals(exitPrompt)) return null;    // Quit if the exit prompt is given

            file = new File(fileName);
        }

        return fileName;
    }

    /**
     * Compresses a file with the given file name. The compressed file will be written to
     * a new file with the name fileName.zzz
     * @param fileName The name of the file to compress
     */
    private void compress(String fileName) {
        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;

        try {
            // Open the file streams
            inputStream = new BufferedInputStream(new FileInputStream(fileName));
            outputStream = new BufferedOutputStream(new FileOutputStream(fileName + COMPRESS_EXT));

            // Compress the file
            compress(inputStream, outputStream);
        }
        catch(DictionaryException ex) {
            System.err.println(ex.getMessage());
        }
        catch(IOException ex) {
            System.err.println(ex.getMessage());
        }
        finally {
            try {
                if(inputStream != null) inputStream.close();
                if(outputStream != null) outputStream.close();
            }
            catch(IOException ex) { System.err.println("Error closing stream"); }
        }
    }

    /**
     * Compresses the given BufferedInputStream and writes the result to the given BufferedOutputStream.
     * @param inputStream The input stream to compress
     * @param outputStream The output stream to write the compressed file to.
     * @throws IOException If there is an exception while reading the input stream
     * @throws DictionaryException If there is an exception while performing operations on the Dictionary
     */
    private void compress(BufferedInputStream inputStream, BufferedOutputStream outputStream) throws IOException, DictionaryException {
        MyOutput output = new MyOutput();

        // Variables used for the dictionary
        Dictionary dict = new Dictionary(4096);
        int currentCode;

        // Variables used to input from the stream
        String s = "";
        int c = inputStream.read();

        // Initialize the dictionary
        for(currentCode = 0; currentCode < 256; currentCode++)
            dict.insert(new DictEntry("" + (char) currentCode, currentCode));

        // While there is information to read in the input stream
        while(c != -1) {
            if(dict.find(s + (char) c) != null) {
                s += (char) c;
            }
            else {
                output.output(dict.find(s).getCode(), outputStream);

                // If the compression algorithm supports more items, add an item to the Dictionary
                if(currentCode < MAX_SIZE)
                    dict.insert(new DictEntry(s + (char) c, currentCode++));
                s = "" + (char) c;
            }

            c = inputStream.read();
        }

        // If there is a string unwritten to the output stream, write to the output stream
        if(s.length() > 0)
            output.output(dict.find(s).getCode(), outputStream);

        output.flush(outputStream);

        System.out.println("File successfully compressed.");
    }
}
