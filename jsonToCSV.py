#!/usr/bin/python

import sys
import re
import io
import json

def flatten_json(y):
    out = {}
    def flatten(x, name=''):
        if type(x) is dict:
            for a in x:
                flatten(x[a], name + a + '.')
        elif type(x) is list:
            i = 0
            for a in x:
                flatten(a, name + str(i) + '.')
                i += 1
        else:
            out[name[:-1]] = x

    flatten(y)
    return out

with io.open(sys.argv[1], encoding='utf8') as fileObj:
	a = flatten_json(json.load(fileObj));
	lines = []
	with open('React.csv', 'w') as fileToSave:
		for key in a:
			value = a[key]
			fileToSave.write('"' + str(key) + '","' + str(value).replace('\n', '\\n')+ '"\n')
