cur_hour, cur_min = map(int, input().split())
deltamin = int(input())

E_hour = 0
E_min = cur_min + deltamin
E_hour = cur_hour
while E_min >= 60:
    E_hour = E_hour + 1
    E_min = E_min - 60



if E_hour >= 24:
    E_hour = E_hour - 24
    
print(f"{E_hour} {E_min}")
