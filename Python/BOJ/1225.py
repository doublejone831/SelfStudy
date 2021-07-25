#O(A * B) 풀이가 기본적으로 생각 나는 풀이
#하지만 각각의 숫자들의 갯수를 세어 준 후 계산하면 o(A + B) 풀이가 가능하다
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
