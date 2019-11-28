import sys

fileArg1 = sys.argv[1]
fileArg2 = sys.argv[2]

fileOpener1 = open(fileArg1,"r")
fileOpener2 = open(fileArg2,"r")

sum1 = 0
sum2 = 0


for line in fileOpener1.readlines() :
    values = line.split()
    for value in values :
        sum1 += int(value)

print(sum1)

for line in fileOpener2.readlines() :
    values = line.split()
    for value in values :
        sum2 += int(value)

print(sum2)

print(sum1 + sum2)
