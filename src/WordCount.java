import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Insight Data Engineering Challenge 1
 * 
 * Word Count
 * 
 * @author Chang Chen
 */

public class WordCount {
	
	private TreeMap<String, MutableInteger> dict; // Dictionary recording words and counts
	
	public WordCount() { // Constructor for WordCount
		this.dict = new TreeMap<String, MutableInteger>();
	}
	
	public void add(String word) {
		if (this.dict.containsKey(word)) {
			// The word is already in the dictionary, increase the count
			this.dict.get(word).val++;
		}
		else {
			// The word is new, put 1 to the count
			this.dict.put(word, new MutableInteger(1));
		}
	}
	
	public void countWord(String filePath) {
		/*
		 * count words in a file
		 */
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
		    inputStream = new FileInputStream(filePath);
		    // scan file one line each time
		    sc = new Scanner(inputStream, "UTF-8");
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        // Parse the line:
		        
		        // Remove all hyphens
		        line = line.replaceAll("[-']", "").toLowerCase();
		        // pattern of word
		        Pattern Word = Pattern.compile("[a-z]+");
		        Matcher m = Word.matcher(line);
		        while (m.find()) {
		        	this.add(m.group());
		        }
		        
		    }
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		    if (inputStream != null) {
		        try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		    }
		    if (sc != null) {
		        sc.close();
		    }
		}
	}
	
	public void output(String filePath) {
		PrintWriter writer;
		try {
			// Open the output file
			writer = new PrintWriter(filePath, "UTF-8");
			// Iterate over dictionary in alphabetical order, and write result to the file
			for (Map.Entry<String, MutableInteger> entry: this.dict.entrySet()) {
				writer.println(entry.getKey() + "\t" + entry.getValue().val);
			}
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public class MutableInteger {
		// A class re-implementing Integer to make it mutable
	    public int val;
	    public MutableInteger(int value) {
	    	this.val = value;
	    }
	}
	
    public static void main(String[] args) {
    	createDirectory("wc_output");
    	String inDir = "wc_input";
    	String outPath = "wc_output/wc_result.txt";
    	// input
    	if (args.length < 2) {
    		System.out.println("Wrong arguments!");
    		System.out.println(args.length);
    		return;
    	}
    	else {
    		inDir = args[0];
    		outPath = args[1];
    	}
    	// word count:
    	File folder = new File(inDir);
    	File[] listOfFiles = folder.listFiles();
        WordCount wc = new WordCount();
    	for (File file : listOfFiles) {
    	    if (file.isFile()) {
    	        wc.countWord(inDir + "/" + file.getName());
    	    }
    	}
    	// output counts:
    	wc.output(outPath);
    }
    
    public static void createDirectory(String dirname) {
    	File theDir = new File(dirname);

    	// if the directory does not exist, create it
    	if (!theDir.exists()) {
    	    System.out.println("creating directory: " + dirname);
    	    boolean result = false;

    	    try{
    	        theDir.mkdir();
    	        result = true;
    	    } 
    	    catch(SecurityException se){
    	        //handle it
    	    }        
    	    if(result) {    
    	        System.out.println("DIR created");  
    	    }
    	}
    }
}
