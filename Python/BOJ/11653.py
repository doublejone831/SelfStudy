#소인수 분해

K = int(input())
i = 2
while i*i <= K :
    if(K % i) == 0:
        print(i)
        K = K / i
    else: i += 1
if K > 1:
    print(int(K))
