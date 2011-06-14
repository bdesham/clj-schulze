#!/usr/bin/python

C = 5

d = [[] for i in range(C)]
p = [[0 for j in range(C)] for i in range(C)]

d[0] = [0,20,26,30,22]
d[1] = [25,0,16,33,18]
d[2] = [19,29,0,17,24]
d[3] = [15,12,28,0,14]
d[4] = [23,27,21,31,0]

for i in range(C):
	for j in range(C):
		if (i != j):
			if (d[i][j] > d[j][i]):
				p[i][j] = d[i][j]
			else:
				p[i][j] = 0

print p
 
for i in range(C):
	for j in range(C):
		if (i != j):
			for k in range(C):
				if (i != k and j != k):
					p[j][k] = max (p[j][k], min (p[j][i], p[i][k]))
#					print "looking at p[%s][%s], p[%s][%s], p[%s][%s]" % (j, k, j, i, i, k)
#					if p[j][k] > 0:
#						print "  seen p[%s][%s] %s times" % (j, k, p[j][k])
#					if p[j][i] > 0:
#						print "  seen p[%s][%s] %s times" % (j, i, p[j][i])
#					if p[i][k] > 0:
#						print "  seen p[%s][%s] %s times" % (i, k, p[i][k])
#					p[j][k] += 1

#print p
#
#names = ["A", "B", "C", "D", "E"]
#
#for i in range(C):
#	for j in range(i):
#		if p[i][j] > p[j][i]:
#			print "%s beats %s" % (names[i], names[j])
