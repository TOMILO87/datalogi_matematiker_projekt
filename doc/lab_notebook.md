#### 220512
- Extracted compressed datafile
- Test: `wc -l data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 # result is 64 056 772 lines`
- Test: `head -n 15 data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 # confirm 12 columns (11 some rows?)`
- Create smaller datafile: `head -n 50 data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 > data/Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4`
- Test: `wc -l data/Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4 # 50 correct number of rows`
- Remove unused columns and save in new file: `awk '{print $1,$2,$6,$7,$8,$10,$11,$12}' data/Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4 > data/Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col.olp.m4`

Considering the advise, I want to translate the contig string identifiers into integers to save RAM memory
- They look like "fp.3.Luci_01A01.ctg.ctg7180000038386" (first line, first column) and "fermi840434137_16102906550" (last line, first column).
- Also try an arbitraty line `sed '76939q;d' Spruce_fingerprint_2017-03-10_16.48.olp.m4` which gives "fp.3.Luci_04A06.ctg.ctg7180000082120" (76939 line, second column).
- Another arbitrary line: `sed '26000300q;d' data/Spruce_fingerprint_2017-03-10_16.48.olp.m4`, which takes long time to run (maybe 15 minutes), gives "fp.3.Luci_09F11.ctg.ctg7180000163788" (26000300 line, first column)
- Idea is to simply remove non-integers. But this might take away some relevant inforamtion contained in the latters.
- Another idea is to replace a=1, b=2, c=3,... but not sure if this would save memory.

#### 220513
Consider test file "data/Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col.olp.m4"

Interpretation of "fp.3.Luci_01A01.ctg.ctg7180000038386 fp.3.Luci_02C06.ctg.ctg7180000060335 6835 7893 28201 0 1058 1058" (first row)
- After the two vertex (DNA segment=contig) identifiers we have 6835 7893 28201 meaning that the overlap starts at position (?) 6835 and ends at 7893 and the length (number of positions) is 28201
- Further 0 1058 1058 means that for the second coting we have overlap at all positions
- We note that 7893-6835=1058, which makes sense since overlap length should be the same in both cotings. //correction: may differ slightly "due to length differences"//
- It is written "If two DNA segments have long and significant similarity at their ends, then we create an edge between their corresponding vertice"
- We have 1058>1000 but overlap does not take place at end of first coting so no edge. //correction: that is not relevant here, enough that overlap "has length at least 1000 in both sequences"//
- We will write a shell script called "edges_test_file.txt" which lists lines in input data which are edges ("each line is a potential edge") in output file  //correction not useful//
- First version "find_edges.txt" written (now just possible to loop the lines)

#### 220514
//Correction: Below not relevant//
- Finished "find_edges.txt". It takes two arguments input_file and (name) of output_file e.g. "data/output.txt". The input file shuld have columns as "data/Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col.olp.m4"
- Running `bash find_edges.txt data/Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col.olp.m4 data/Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col_edges.olp.m4` gives a file with 5 edges
- A version of "find_edges.txt" called "count_edges" returns number of edges in "edges in data/Spruce_fingerprint_2017-03-10_16.48.olp.m4".
- But `bash count_edges.txt` takes too long to run (no result after approx. 3 hours). So will need another method.

#### 220515
We remove columns which will not be used
- `awk '{print $1,$2,$6,$7,$8,$10,$11,$12}' data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 > data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4`
 
Above reduces file size from 7.3 GB to 6.4 GB.

We come back to task of translating the contig string identifiers into integers:

Identify unique contings
- step 1) `awk '{print $1}' data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4 > data/col1.olp.m4`
- step 2) `awk '{print $2}' data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4 > data/col2.olp.m4`
- step 3) `mv data/col1.olp.m4 data/identifiers.olp.m4`
- step 4) `cat data/col2.olp.m4 >> data/identifiers.olp.m4`
- ckeck) `cat data/identifiers.olp.m4 | sort | uniq | wc -l # gives 11393435 as expected`
- step 5) `cat data/identifiers.olp.m4 | sort | uniq > data/identifiers.olp.m4`//

//Correction below not working or irrelevant//
Idea: Replace cotings with integers by looping through "data/identifiers.olp.m4" and for each line:
`cat data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4 | tr line id > data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4; id += 1` 
Above steps and replacement might also be included in shell script. Maybe last step is to slow because of `cat`.
Found `sed -i '/old text/ s//new text/g' gigantic_file.sql` om stack exchange which might be useful for large file.

#### 220516
- We conjungture that the graph is sparse and therefore a adjacency list might be apropriate for storage.
- Above shell scripts "count_edges.txt" for finding number of edges ineffcient (probably due to "print"). We instead use
- `awk '($4 - $3 > 999) && ($7 - $6 > 999)' data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4 | wc -l # Number edges is 63 962 895`
- Conclude that share of potential edges which are actual edges is 63 962 895 / 64 056 772 = 99.85%.

The following adds line number to each (unique) identifier which corresponds to translating string identifiers into integers
- To add `nl data/identifiers.olp.m4 > data/identifiers_id.olp.m4`

But problem remains to "replace" the strings in the data with the line numbers from above. Tried with no success (based on https://stackoverflow.com/a/22253586/5733571)
- `awk 'NR==FNR{a[$2]=$1;next}{$1=a[$1]}1' data/identifiers_id.olp.m4 data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4`

#### 220517
The following script (from yesterday) was actually useful, but when doing tests on smaller files I got confusing results because I hadn't made sure unix line-endings in the files
- `awk 'NR==FNR{a[$2]=$1;next}{$1=a[$1]}1' data/identifiers_id.olp.m4 data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4`
- Converting from Windows-style to UNIX-style line endings: https://support.nesi.org.nz/hc/en-gb/articles/218032857-Converting-from-Windows-style-to-UNIX-style-line-endings

#### 220518
In the shell script "convert_data_to_edges_integer_format.txt" we include all commands necessary to to convert raw data into file with only two columns representing edges, where all values are integers identifying nodes. The resulting file is called "data/edges_processed.olp.m4"

We next create a smaller dataset with 10000 elements based on "data/edges_processed.olp.m4".
- `head -10000 data/edges_processed.olp.m4 > data/edges_processed_subset.olp.m4'

Next task is to create a graph representation in java (adjacency list) and e.g. explore degree distribution for the smaller dataset. Some intial structure for this is added in src.

#### 220519
- Defined graph class in Java providing an array of linked list.
- It is possible to add edges to verticies (contings) using method "add_edge"
- It is now possible to use method "get_size" on the graph with an vertex (contig) as argument to get number of edges connecting to the vertex. This will help us find the node degree distribution.
- Next we will look into number of component with at least theree vertices.

#### 220520
- Added code for getting neighbors of nodes. Also started working on code for finding components, try to use depth first serach based on pseudo code shown in one of the lecture vidoes.

#### 220521
- Java code for finding components done.
- Now print node degree distribution. Further freuency table almost done, need to also get nodes with zeros degree included print though. Intervals based on max value and number bins free parameter (e.g. 10).
- Also find and print number of compontent and cliques.
- Above results are for test data. Need to scan conting data and produce the corresponding results, to make sure no effciency issus.

- Fixed frequency table bug.
- When analyzing the real dataset it works for 1000000 elements, then when analyzing whole datset run into "Exception in thread "main" java.lang.StackOverflowError" which seems to happen when we are counting components.
- Also realized that my frequency table with intervals of even size might not be apropriate becuase most nodes have a small degree and therefore are clustered in bin 1. I might mix in a later version.

- Tried to fix "Exception in thread "main" java.lang.OutOfMemoryError: Java heap space: failed reallocation of scalar replaced objects". After some reserch I found that thet I probably do not use maximum RAM in my Virtual Machine (Ubuntu on Windows).
- This site provided more information and a soultion https://medium.com/@hwimalasiri/how-to-increase-maximum-heap-size-of-jvm-in-ubuntu-e836b15284eb:
- When running `java -XshowSettings:vm` I got result "VM settings: Max. Heap Size (Estimated): 3.10G Using VM: OpenJDK 64-Bit Server VM".
- Then using the command `export _JAVA_OPTIONS=-Xmx6000m` (6000 for 6 GB) we get  "VM settings: Max. Heap Size (Estimated): 5.86G Using VM: OpenJDK 64-Bit Server VM". Then it works. Says "Picked up _JAVA_OPTIONS: -Xmx6000m" when running other commands.

#### 220522
- Next error we get (when analyzing beyond about 1200000 lines), for e.g. `head -1500000 data/edges_processed.olp.m4 | java -jar Graph.jar`, is "Exception in thread "main" java.lang.StackOverflowError".
- The above error message further refers to "projekt.Graph.get_components_visited(Graph.java:112)" and seems to have to do with the help function for depth first serach.
- We suspect that the error has to do with us storing too many components, where compoents elements are stored in HashSets, inside an array.
- We have two ideas how to fix it. Either use something simialr to Pythons "yield" keyword and return componetns recursively. Or we may simply count number of components and cliques without keping track of the actual elements.

- Related to "java.lang.StackOverflowError" I found this post on stack overflow: https://stackoverflow.com/a/47831474/5733571
- The above post says: "The error java.lang.StackOverflowError is thrown to indicate that the application’s stack was exhausted, due to deep recursion i.e your program/script recurses too deeply."
- So we will need to avoid going too deep into the recursion, or maybe I can increase the stack size on the virtual machine similar to what we did for the heap causing the "java.lang.OutOfMemoryError:".
- The following command gives the stack size `ulimit -a` "Inside Ubuntu". Result is "stack size (kbytes, -s) 8192".
- We try to increase the stack size. This post https://askubuntu.com/a/319159/1595503 suggests the command 'ulimit -s 16000'. Indeed we get stack size (kbytes, -s) 16000".
- Also after increasing stack size to 16000 still get "StackOverflowError". Get the same result when increasing to 5000000 as well so seems not to be the solution.

Since the recursion depth seems to be the problem we will instead implement an iterative depth search first:
- Based on the following post: https://www.geeksforgeeks.org/iterative-depth-first-traversal/ and using a modified version of the stack from Lab 2 we now can get component for each vertex seperatly without using recursion.
- Still run into some error becuase the stack is of too small size but since we defined the Stack class ourselves we may easily adjust the stack size to a very large number which solves the problem.
- We check then check the iterative depth work for the whole dataset using `cat data/edges_processed.olp.m4 | java -jar Graph.jar` (it is slow but works) and also clean up code.

#### To do list
- (Done) Data should contain 11393435 contings. I should later check that this is indeed number of unique identifers.
- (-) Would be nice to create some plots showing (parts of) the graph. It was previously recommended to use Gephi, so maybe come back to that.
- (Done) "find_edges.txt" produces annoying empty line in end of file. Maybe fix that later. //Not relevant anymore due to corrections 220513//
- (Done) Put steps for processing the raw data in shell script
- (Done) Need to justify sparsity conjuncture.
- (-) Issue with counting components for large dataset, works for 1000000 first lines and is fairly quick otherwise. Also will look in to frequewncy table appareance.

#### Misc notes
- To store edges and verticies some options are: adjacency list, adjacency matrix. Will consider sparseness to decide which type to store in.
- If number of edges is proportional to "size" of graph (guess number of nodes) then often seen as sparse accroding to lecture note.