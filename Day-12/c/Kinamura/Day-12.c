#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LINE_SIZE 120
#define LINES 1050

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-12.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

void parseInput(char prompts[][LINE_SIZE], int len, int sizes[]) {
    int shapeID = -1, line = 0, validCount = 0;
    while (1) {
        if (prompts[line][0] == '\n') { line++; continue; }
        if (prompts[line][2] == 'x') { break; }
        if (prompts[line][1] == ':') { line++; shapeID++; continue; }
        int yPos = 0;
        while (prompts[line][yPos] != '\n') {
            if (prompts[line][yPos++] == '#') sizes[shapeID]++;
        }
        line++;
    }
    for (int i = line; i < len; i++) {
        char *token, *rest, *rest2;
        token = strtok_r(prompts[i], " ", &rest);
        token = strtok_r(token, "x", &rest2);
        int area = atoi(token) * atoi(rest2), areaSum = 1, sizePos = 0;
        while ((token = strtok_r(rest, " ", &rest)) != NULL) {
            areaSum += (atoi(token) * sizes[sizePos++]);
        }
        if (areaSum <= area) validCount++;
    }
    fprintf(stdout, "Part 1:%d\n", validCount);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    int sizes[6] = {0};
    const int lineCount = readInput(input);
    parseInput(input, lineCount, sizes);
    return 0;
}
