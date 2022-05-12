#### 220512
- Extracted compressed datafile
- Test: `wc -l data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 # result is 64 056 772 lines`
- Test: `head -n 15 data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 # confirm 12 columns (11 some rows?)`
- Create smaller datafile: `head -n 50 data/Spruce_fingerprint_2017-03-10_16.48.olp.m4 > data/Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4`
- Test: `wc -l data/Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4 # 50 correct number of rows`
- Remove unused columns and save in new file: `awk '{print $1,$2,$6,$7,$8,$10,$11,$12}' data/Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4 > data/Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col.olp.m4`

Considering the advise, I want to translate the contig string identifiers into strings to save RAM memory
- They look like "fp.3.Luci_01A01.ctg.ctg7180000038386" (first line, first column) and "fermi840434137_16102906550" (last line, first column).
- Also try an arbitraty line `sed '76939q;d' Spruce_fingerprint_2017-03-10_16.48.olp.m4` which gives "fp.3.Luci_04A06.ctg.ctg7180000082120" (second column).
- Another arbitrary line: `sed '26000300q;d' data/Spruce_fingerprint_2017-03-10_16.48.olp.m4`, which takes long time to run (maybe 15 minutes), gives "fp.3.Luci_09F11.ctg.ctg7180000163788"
- Idea is to simply remove non-integers. But this might take away some relevant inforamtion contained in the latters.


#### To do list
- (220512) Data should contain 11 393 435 contings. I should later check that this is indeed number of unique identifers. 
