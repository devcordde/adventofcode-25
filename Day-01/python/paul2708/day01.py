from shared.paul2708.input_reader import *
from shared.paul2708.output import write

lines = read_plain_input(day=1)

code = 50

simple_password = 0
enhanced_password = 0

for line in lines:
    rotation = int(line[1:])

    for i in range(rotation):
        if line.startswith("R"):
            code = (code + 1) % 100
        else:
            code = (code - 1) % 100

        if code == 0:
            enhanced_password += 1

    if code == 0:
        simple_password += 1

write(f"The password to open the door is <{simple_password}>.")
write(f"The enhanced password to open the door is <{enhanced_password}>.")
