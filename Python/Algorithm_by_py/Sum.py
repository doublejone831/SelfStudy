# 주어진 범위 안에서 주어진 조건에 맞는 자료들 합


scores = [100, 75, 50, 37, 90, 95]
sum = 0
N = len(scores)
for i in range(0, N):
    if scores[i] >= 80:
        sum = sum + scores[i]
print(f"{N} 명의 점수 중 80점 이상의 총점: {sum}")