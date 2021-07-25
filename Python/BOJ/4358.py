Trees = []
total = 0
while True:
    try:
        temp = input()
        total += 1
        Flag = False
        #empty list를 확인하는 implicit and pythonic way
        if not Trees: 
            Trees.append([temp, 1])
        else:
            flag = False
            for i in range(len(Trees)):
                if Trees[i][0] == temp:
                    Trees[i][1] += 1
                    Flag = True
                    break
            else:
                Trees.append([temp, 1]) 
    except EOFError:
        break
Trees.sort(key=lambda x:x[0])

for i in range(len(Trees)):
    num = format(round(Trees[i][1]/total*100,6),".4f")
    print(f"{Trees[i][0]} {num}")
