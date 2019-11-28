import sys

fileArg = sys.argv[1]

fileOpener = open(fileArg,"r")

sum = 0


for line in fileOpener.readlines() :
    values = line.split()
    for value in values :
        sum += int(value)
    print(sum)

open("aFileThatDoesntExistsAndThatWillHopfullyNeverExistsBecauseItsUsfullForBugging.something","r");
