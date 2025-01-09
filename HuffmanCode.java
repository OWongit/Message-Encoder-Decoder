// Owen Wilhere
// 03/08/2023
// CSE 123
// TA: Jay Dharmadhikari
// This class is HuffmanCode. It works as a Huffman encoder capable of compressing and
// decompressing text files. The code is in standard format.

import java.io.*;
import java.util.*;

public class HuffmanCode {

    private HuffmanNode code;

    // This constructor constructs a HuffmanNode tree.
    // It is given an array of integers used to make the tree.
    // No Returns.
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> priorQ = createPQ(frequencies);
        while (priorQ.size() > 1) {
            HuffmanNode left = priorQ.remove();
            HuffmanNode right = priorQ.remove();
            HuffmanNode freq =
                new HuffmanNode(left.frequency + right.frequency, left, right);
            priorQ.add(freq);
        }
        this.code = priorQ.remove();
    }

    // Creates a priority queue of Huffman nodes.
    // Each index of the inputted array corresponds to the frequency of a character.
    // Characters with non-zero frequencies are added to a priority queue as Huffman
    // nodes, with the character index and its frequency as fields. 
    // Parameters: int[] frequencies - an array of frequencies. Returns a priority 
    //             queue with Huffman nodes sorted by their frequencies(lowest at the 
    //             start).
    private Queue<HuffmanNode> createPQ(int[] frequencies) {
        Queue<HuffmanNode> priorQ = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                priorQ.add(new HuffmanNode(i, frequencies[i]));
            }
        }
        return priorQ;
    }

    // Constructs a HuffmanCode object by interpreting encoded data retrieved from the
    // provided Scanner input. 
    // Parameters: Scanner input - a Scanner object containing encoded data. No Returns.
    public HuffmanCode(Scanner input) {
        while (input.hasNextLine()) {
            int cVal = Integer.parseInt(input.nextLine());
            String pathway = input.nextLine();
            code = createCode(cVal, pathway, code, 0);
        }
    }

    // This method helps the constructor method HuffmanCode(Scanner input).
    // Parameters: int cVal - an ASCII value of a character
    //             String pathway - the numerical pathway to a leafnode containing the
    //             cVal HuffmanNode code - the Huffman tree being constructed int index -
    //             the index of the pathway
    // Returns a HuffmanNode representing the root of the HuffmanNode tree.
    private HuffmanNode createCode(int cVal, String pathway, HuffmanNode code,
                                   int index) {
        if (index == pathway.length()) {
            return new HuffmanNode(cVal, 0);
        }
        if (code == null) {
            code = new HuffmanNode();
        }
        char first = pathway.charAt(index);
        if (first == '0') {
            code.left = createCode(cVal, pathway, code.left, index + 1);
        } else if (first == '1') {
            code.right = createCode(cVal, pathway, code.right, index + 1);
        }
        return code;
    }

    // Saves the current Huffman tree to an output file in the format of an ASCII value
    // followed by the tree pathway 
    // Parameters: PrintStream output - a printstream object to output the data. No Returns.
    // No Returns.
    public void save(PrintStream output) { 
        save(output, code, ""); 
        }

    // Helper method for save(PrintStream output). Navigates through the HuffmanNode tree
    // and prints the ASCII value followed by the tree pathway.
    // Parameters: PrintStream output - a printstream object to output the data.
    //             HuffmanNode node - the current node the method is referencing.
    //             String currentComp - the pathway to the node.
    // No Returns.
    private void save(PrintStream output, HuffmanNode node, String currentComp) {
        if (node != null) {
            if (node.left == null && node.right == null) {
                output.println(node.cVal);
                output.println(currentComp);
            } else {
                save(output, node.left, currentComp + "0");
                save(output, node.right, currentComp + "1");
            }
        }
    }

    // Method is given a BitInputStream and a PrintStream and converts bits from the input
    // stream and outputs the corresponding text to a PrintStream output. 
    // Parameters: BitInputStream input - the inputted bitstream to be converted
    //             PrintStream output - a printstream object to output the data.
    // No Returns.
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode current = code;
        while (input.hasNextBit()) {
            int bit = input.nextBit();
            if (bit == 0) {
                current = current.left;
            } else if (bit == 1) {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                output.write((char)current.cVal);
                current = code;
            }
        }
    }

    // This class is HuffmanNode. It represents a single node in a HuffmanNode tree.
    // It implements the combarable interface.
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        public int cVal;
        public int frequency;
        public HuffmanNode left;
        public HuffmanNode right;

        // Contructs a root node
        public HuffmanNode() { this(-1, null, null); }

        // Contructs a branch node
        public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        // Constructs a leaf node
        public HuffmanNode(int cVal, int frequency) {
            this(frequency, null, null);
            this.cVal = cVal;
        }

        // Inputted HuffmanNode 'other' and compares its frequency to this.HuffmanNode
        // Returns a negative number if other > this
        // Returns zero if other = this
        // Returns a positive number if other < this
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }
}

