A, B = map(str, input().split())
answer = 0
A_count = [0 for i in range(10)]
B_count = [0 for i in range(10)]

for i in range(len(A)):
    A_count[int(A[i])] += 1

for i in range(len(B)):
    B_count[int(B[i])] += 1

for i in range(10):
    for j in range(10):
        answer += i*j*A_count[i]*B_count[j]
print(answer)
