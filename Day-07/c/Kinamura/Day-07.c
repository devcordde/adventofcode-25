#include <stdio.h>
#include <stdlib.h>

#define LINE_SIZE 144
#define LINES 144

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-07.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

void parseInput(char input[][LINE_SIZE], int len) {
    int splits = 0, beamPos[len];
    long long *multiPos = calloc(sizeof(long long), len), sum = 0;
    for (int i = 0; i < len; i++) {
        if (input[0][i] == 'S') { beamPos[i] = multiPos[i] = 1; break;}
    }
    for (int i = 2; i < len; i += 2) {
        for (int j = 0; j < len; j++) {
            if (beamPos[j] && input[i][j] == '^') {
                beamPos[j - 1] = beamPos[j + 1] = 1;
                multiPos[j - 1] += multiPos[j];
                multiPos[j + 1] += multiPos[j];
                multiPos[j] = beamPos[j] = 0;
                splits++;
            }
        }
    }
    for (int i = 0; i < len; i++) sum += multiPos[i];
    fprintf(stdout, "Part 1: %d\nPart 2: %lld\n", splits, sum);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    parseInput(input, lineCount);
    return 0;
}
