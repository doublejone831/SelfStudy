#counting algorithm: 주어진 조건에 해당하는 다료들의 갯수를 구함
#1부터 1000 까지의 정수중 13의 배수의 갯수를 구함

#input
count = 0

#process
for i in range(1,1000):
    if i % 13 == 0:
        count += 1

#output
print(f"\n 1부터 1000까지의 정수중 13의 배수의 개수 : {count}")
