N, M = map(int, input().split())

count = 0;

count = (min(N, M)-1) + (min(N, M) * (max(N, M)-1))

print(count)

