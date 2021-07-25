##우선순위 : 3으로 나누기 
#한번에 1은 최대 2개이상 안빠짐
# - > 1개 빠질 때와 2개빠질 때 비교 필요
#o(n)소인수 분해 https://www.geeksforgeeks.org/sieve-eratosthenes-0n-time-complexity/
import sys

isprime = [True]*1001
prime = []
SPF = [None]*1001


def seive(N):
    isprime[0] = isprime[1] = False

    for i in range(2, N):
        if isprime[i] == True:
            prime.append(i)
            SPF[i] = i

        j = 0    
        while (j < len(prime) and
                i * prime[j] < N and
                prime[j] <= SPF[i]):
            isprime[i * prime[j]] = False
            SPF[i * prime[j]] = prime[j] 
            j += 1

def P_factorization(N):
    primes = []
    i = 0
    P = prime[i]
    while N == 1:
        while N % prime[i] != 0:
            N = N / prime[i]
        if i > 1 :
            primes.append(prime[i])
        i += 1
    return primes

if __name__ == "__main__":
    N = int(sys.stdin.readline().rstrip('\n'))
    count = 0
    temp = [N , N-1, N-2]
    temp1 = [None]*3
    for i in range(len(temp)):
        temp1[i] = P_factorization(temp[i])
    
