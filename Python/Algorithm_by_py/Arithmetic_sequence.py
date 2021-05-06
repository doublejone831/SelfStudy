# 1부터 n까지의 정수중 홀수의 합을 구하는 프로그램

#input
sum = 0

#process
for i in range(1, 20):
    if i % 2 == 1:
        sum = sum + i
        print(i, end='  ')

#output
print(f"\n홀수들의 합은 {sum}")