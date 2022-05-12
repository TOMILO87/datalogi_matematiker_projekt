220512
- Extracted datafile
- Test: wc -l Spruce_fingerprint_2017-03-10_16.48.olp.m4 # no result maybe file to big
- Test: head -n 15 Spruce_fingerprint_2017-03-10_16.48.olp.m4 # confirm 12 columns (11 some rows?)
- Create smaller datafile: head -n 50 Spruce_fingerprint_2017-03-10_16.48.olp.m4 > Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4
- Test: wc -l Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4 # 50 correct number of rows
- Remove unusedcolumns: awk '{print ,,,,,0,1,2}' Spruce_fingerprint_2017-03-10_16.48_head_50.olp.m4 > Spruce_fingerprint_2017-03-10_16.48_head_50_rm_col.olp.m4

Translation into integer identifiers
