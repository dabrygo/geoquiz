# Utility for turning line-separated data into a csv file

import re

def is_comment(line):
    return line.startswith('#')

def is_new_section(line):
    return re.match(r'^[A-Z]$', line)


csv = []
with open('../rsc/geoquiz/raw_capitals.txt', 'r') as f:
    pieces = []
    for line in f.readlines():
        line = line.strip()
        if is_comment(line) or is_new_section(line):
            continue
        if line:
            pieces.append(line)
        else:
            csv.append(','.join(pieces))
            pieces = []

with open('../rsc/geoquiz/capitals.txt', 'w') as f:
    f.write('\n'.join(csv))

