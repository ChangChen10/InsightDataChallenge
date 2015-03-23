import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Insight Data Engineering Challenge 1
* 
* Running Median
* 
* @author Chang Chen
*/

public class RunningMedian {
	private PriorityQueue<Integer> minHeap; // record the larger half
    private PriorityQueue<Integer> maxHeap; // record the smaller half
    private double median; // record median
    private int size; // record size
    public RunningMedian() {
    	this.minHeap = new PriorityQueue<Integer>(10000);
    	this.maxHeap = new PriorityQueue<Integer>(10000, new MaxComparator());
    	this.median = 0;
    	this.size = 0;
    }
    
    public void add(int num) {
    	// edge cases:
    	if (this.size == 0) {
    		this.median = num;
    		this.maxHeap.add(num);
    		this.size ++;
    		return;
    	}
    	if (this.size == 1) {
    		if (num >= this.median) {
    			this.minHeap.add(num);
    		}
    		else {
    			this.minHeap.add(this.maxHeap.poll());
    			this.maxHeap.add(num);
    		}
    		this.median = (this.median + num) / 2;
			this.size ++;
			return;
    	}
    	
    	// Step 1: Add next item to one of the heaps
    	// Step 2: Rebalance
    	if (num < this.maxHeap.peek()) {
    		this.maxHeap.add(num);
    		if (this.maxHeap.size() - this.minHeap.size() > 1) {
    			this.minHeap.add(this.maxHeap.poll());
    		}
    	}
    	else if (num > this.minHeap.peek()){
    		this.minHeap.add(num);
    		if (this.minHeap.size() - this.maxHeap.size() > 1) {
    			this.maxHeap.add(this.minHeap.poll());
    		}
    	}
    	else {
    		// this.maxHeap.peek() <= num <= this.minHeap.peek()
    		if (this.minHeap.size() < this.maxHeap.size()) {
    			this.minHeap.add(num);
    		}
    		else {
    			this.maxHeap.add(num);
    		}
    	}
    	
    	// update size:
    	this.size ++;
    	
    	// update median:
    	if (this.maxHeap.size() > this.minHeap.size()) {
    		this.median = this.maxHeap.peek();
    	}
    	else if (this.maxHeap.size() < this.minHeap.size()) {
    		this.median = this.minHeap.peek();
    	}
    	else {
    		// when this.maxHeap.size() == this.minHeap.size():
    		this.median = (this.maxHeap.peek() + this.minHeap.peek()) * 1.0 / 2;
    	}
    }
    
    public void calculate(String inFilePath, String outFilePath) {
		/*
		 * calculate median in a file
		 */
		FileInputStream inputStream = null;
		Scanner sc = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(outFilePath,true);
		    inputStream = new FileInputStream(inFilePath);
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
		        int num = 0;
		        while (m.find()) {
		        	num ++;
		        }
		        this.add(num);
                int i = (int) this.median;
		        String str = Integer.toString(i) + "." + (int)((this.median - i) * 10);
		        fw.write(str + "\n");
		    }
		    // note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
				fw.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
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
    
    public double median() {
    	return this.median;
    }
    
    public int size() {
    	return this.size;
    }
    
    public class MaxComparator implements Comparator<Integer> {
    	// Comparator for maxHeap
    	@Override
        public int compare(Integer x, Integer y) {
    		return y - x;
        }
    }
    public static void main(String[] args) {
    	createDirectory("wc_output");
    	String inDir = "./wc_input";
    	String outPath = "./wc_output/med_result.txt";
    	if (args.length < 2) {
    		System.out.println("Wrong arguments!");
    		return;
    	}
    	else {
    		inDir = args[0];
    		outPath = args[1];
    	}
    	deleteFile(outPath);
    	File folder = new File(inDir);
    	File[] listOfFiles = folder.listFiles();
        RunningMedian rm = new RunningMedian();
    	for (File file : listOfFiles) {
    	    if (file.isFile()) {
    	        rm.calculate(inDir + "/" + file.getName(), outPath);
    	    }
    	}
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
    
    public static void deleteFile(String filename) {
    	// remove exist file
    	try{	      
            //Delete if file exists
            File file = new File(filename);
              if (file.exists()){
                 file.delete();
              }   
          }catch(Exception e){
             // if any error occurs
             e.printStackTrace();
          }
    }
}
