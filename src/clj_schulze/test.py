#!/usr/bin/python

C = 5

for i in range(1, C+1):
	for j in range(1, C+1):
		if (i != j):
			if (d[i][j] > d[j][i]):
				p[i][j] = d[i][j]
			else:
				p[i][j] = 0
 
for i in range(1, C+1):
	for j in range(1, C+1):
		if (i != j):
			for k in range(1, C+1):
				if (i != k and j != k):
					p[j][k] = max (p[j][k], min (p[j][i], p[i][k]))

