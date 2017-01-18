# Utility for turning line-separated data into a csv file

import re

def is_comment(line):
    return line.startswith('#')

def is_new_section(line):
    return re.match(r'^[A-Z]$', line)

def convert_to_csv(infile, outfile):
    csv = []
    with open(infile, 'r') as f:
        pieces = []
        for line in f.readlines():
            line = line.strip()
            if is_comment(line) or is_new_section(line):
                continue
            if line:
                no_commas = line.replace(',', '|')
                pieces.append(no_commas)
            else:
                csv.append(','.join(pieces))
                pieces = []

    with open(outfile, 'w') as f:
        f.write('\n'.join(csv))

convert_to_csv('../rsc/geoquiz/raw_country_codes.txt', '../rsc/geoquiz/country_codes.txt')
