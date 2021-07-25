
T = int(input())

for i in range(T):
   
    N = int(input())
    S = input()
    count = 1 #첫 번째는 무조건 1이으로 카운트는 기본 1부터 시작
    print(f"Case #{i+1}:", end = ' ') #케이스 별 결과 출력 포맷
    for j in range(N): #O(n)으로 각각 갯수 확인
        if j != 0: #0인경우는 첫번째로  count는 무조건 1
            if S[j-1] < S[j]: #strictly increase인 경우
                count += 1
                answer = count
            else: # increase가 깨진 경우
                count = 1
        print(f"{count}", end= ' ')
    print('') #각 케이스의 마지막에 줄바꿈용
