# Utility for turning line-separated data into a csv file
import glob
import os.path
import re

def is_comment(line):
    return line.startswith('#')

def is_new_section(line):
    return re.match(r'^[A-Z]$', line)

def convert_to_csv(infile, verbose=True):
    csv = []
    filename = os.path.basename(infile)
    outName = '_'.join(filename.split('_')[1:])
    outfile = os.path.join(os.path.dirname(infile), outName)
    if verbose:
        print('converting', filename, 'to', outName)
    with open(infile, 'r') as f:
        pieces = []
        for line in f.readlines():
            line = line.strip()
            if is_comment(line) or is_new_section(line):
                continue
            if line:
                no_commas = line.replace(',', '|')
                pieces.append(no_commas)
            elif pieces:
                csv.append(','.join(pieces))
                pieces = []
    print(csv)
    with open(outfile, 'w') as f:
        f.write('\n'.join(csv))

for raw_file in glob.glob('../rsc/geoquiz/raw_*.txt'):
    convert_to_csv(raw_file)
