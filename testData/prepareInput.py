import os
import random
import sys

domain_names = ["octeth.com", "amazon.com", "google.com", "ebay.com", "hotmail.com", "yahoo.com", "example.com"]

outputfile = ""

f = open("testData/input.txt", "w")

for i in range(int(sys.argv[1])):
	ran = random.randint(0, 6)
	domain = domain_names[ran]
	outputfile += domain + '\n'

f.write(outputfile)
f.close()


