cd src

chmod a+x WordCount.java
chmod a+x RunningMedian.java

javac WordCount.java
javac RunningMedian.java

java WordCount ../wc_input ../wc_output/wc_result.txt
java RunningMedian ../wc_input ../wc_output/med_result.txt