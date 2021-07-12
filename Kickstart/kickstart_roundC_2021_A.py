T = int(input())
for i in range(T):
    N, K = map(int, input().split())
    limit = chr(ord('a') + K -1)    
    S = input()
    Str = ""
    #확인용 문자열 생성
    for j in range(len(S)):
        Str = Str + "a"
    middle = len(S) / 2 
    left = 0
    right = len(S) - left - 1
    answer = 1
    #홀수인 경우
    #중간에 가기 전까지

    while True:
        if Str >= S:
            break
        if left != 0 and left != right:
            if chr(ord(Str[left])+1) > limit or Str[:left] + chr(ord(Str[left])+1) + Str[left + 1:right] + chr(ord(Str[right])+1) + Str[right+1:] >= S:
                Str = Str[:left] + 'a' + Str[left + 1:right] + 'a' + Str[right + 1:]
                left += 1
                right -= 1
            else:
                Str = Str[:left] + chr(ord(Str[left])+1) + Str[left + 1:right] + chr(ord(Str[right])+1) + Str[right+1:]
                answer += 1
                left = 0
                right = len(S) -1
        elif left == right:
            if chr(ord(Str[left])+1) > limit or Str[:left] + chr(ord(Str[left])+1) + Str[left + 1:]>= S:
                break
            else:
                Str = Str[:left] + chr(ord(Str[left])+1) + Str[left + 1:]
                answer += 1
                left = 0
                right = len(S) -1
        else:
            while True:
                if chr(ord(Str[left])+1) > limit or chr(ord(Str[left])+1) + Str[left + 1:right] + chr(ord(Str[right])+1) >= S:
                    Str = 'a' + Str[left + 1:right] + 'a'
                    left += 1
                    right -= 1
                    break
                else:
                    Str = chr(ord(Str[left])+1) + Str[left + 1:right] + chr(ord(Str[right])+1)
                    answer += 1
        if left > right:
                    break
    print(f"Case #{i + 1}: {answer}")  
