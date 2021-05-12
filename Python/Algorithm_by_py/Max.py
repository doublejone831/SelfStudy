# 주어진 범위에 주어진 조건의 자료중 가장 큰값 골라내기
import sys

def main():
    numbers = [ -2, -5, -3, -7, -1 ]
    N = len(numbers)
    Max = -sys.maxsize -1  #정수형이 가지는 가장작은값으로 초기화
    
    for i in range(0, N):
        if numbers[i] > Max:
            Max = numbers[i]
            
    print(f"최댓값 : {Max}")
    

if __name__ == "__main__":
    main()