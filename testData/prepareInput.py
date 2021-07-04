import os
import random
import sys

domain_names = ["octeth.com", "amazon.com", "google.com", "ebay.com", "hotmail.com", "yahoo.com", "example.com",
        "twitter.com", "aws.amazon.com", "udemy.com", "coursera.org", "edx.com", "udacity.com", "gmail.com", "protonmail.com",
        "eksisozluk.com", "sendloop.com", "trt.com", "reddit.com", "quora.com", "dev.to", "khanacademy.org"]

outputfile = ""

f = open("input.txt", "w")

for i in range(int(sys.argv[1])):
    ran = random.randint(0, len(domain_names)-1)
    domain = domain_names[ran]
    outputfile += domain + '\n'

f.write(outputfile)
f.close()


