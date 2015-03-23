README for Insight Data Engineering Challenge

Chang Chen
cc3757@columbia.edu

Note: My program need java-1.7

Word Count:
1. Read each file line by line
2. Parse each line
3. Suppose the are n words in total. Since we need to sort the words, the running time is at least O(n log(n). To keep the order of words, I use a Tree Map to record the words and counts, whele word is the key and count is the value. Each insertion the Tree Map need O(log(n)) time, the total time is O(N log(n)) for N insersion. The space complexity is O(n).

Running Median:

I use two heap to calculate the runnning median. One is a Max Heap to store current smaller half, another is a Min Heap to store the current larger half. Each time, I keep the two heap balanced.
Suppose there is l lines in total, the running time is O(l log(l)), the space complexity is O(l).