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

//Correction below not working or relevant//
Idea: Replace cotings with integers by looping through "data/identifiers.olp.m4" and for each line:
`cat data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4 | tr line id > data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4; id += 1` 
Above steps and replacement might also be included in shell script. Maybe last step is to slow because of `cat`.
Found `sed -i '/old text/ s//new text/g' gigantic_file.sql` om stack exchange which might be useful for large file.

#### 220516
- We conjungture that the graph is sparse and therefore a adjacency list might be apropriate for storage.
- Above shell scripts for finding number of edges ineffcient (probably due to "print"). We instead use
- `awk '($4 - $3 > 999) && ($7 - $6 > 999)' data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4 | wc -l # Number edges is 63 962 895`
- Conclude that share of potential edges which are actual edges are 63 962 895 / 64 056 772 = 99.85%.

The following adds line number to each (unique) identifier which corresponds to translating string identifiers into integers
- To add `nl data/identifiers.olp.m4 > data/identifiers_id.olp.m4`

But problem remains to "replace" the strings in the data with the line numbers from above. Tried with no success
- `awk 'NR==FNR{a[$2]=$1;next}{$1=a[$1]}1' data/identifiers_id.olp.m4 data/Spruce_fingerprint_2017-03-10_16.48_rm_col.olp.m4

#### To do list
- (Done) Data should contain 11393435 contings. I should later check that this is indeed number of unique identifers.
- (220513) Would be nice to create some plots showing (parts of) the graph. It was previously recommended to use Gephi, so maybe come back to that.
- (220514) "find_edges.txt" produces annoying empty line in end of file. Maybe fix that later. //Not relevant anymore due to corrections 220513//
- (220515) Put steps in shell script
- (220516) Need to justify sparsity conjuncture.
 
#### Misc notes
- To store edges and verticies some options are: adjacency list, adjacency matrix. Will consider sparseness to decide which type to store in.
- If number of edges is proportional to "size" of graph (guess number of nodes) then often seen as spared accroding to lecture note.