import sys
import time

def soma(a, b):
    return a + b

a = int(sys.argv[1])
b = int(sys.argv[2])
resultado = soma(a, b)
print(resultado)
time.sleep(1)