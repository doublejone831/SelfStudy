arr = []
x = ''
i = 0
while x != ']':
    x = input()
    if x == '[' or x == ',':
        continue
    else:
        arr[i] = x

print(arr)