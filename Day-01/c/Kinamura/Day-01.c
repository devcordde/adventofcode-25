#include <stdio.h>
#include <stdlib.h>

#define LINE_SIZE 6

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-01.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

void parseInput(char input[][LINE_SIZE], int lines) {
    int safePos = 50, p2 = 0, zeroes = 0;
    char * end;
    for (int i = 0; i < lines; i++) {
        int curr = strtol(input[i] + 1, &end, 10), t = 0;
        if (input[i][0] == 'L') {
            if (safePos == 0) t--;
            safePos -= curr;
            t += (-safePos + 99) / 100;
            safePos = (100 - ((-safePos) % 100)) % 100;
            if (safePos == 0) t++;
        } else {
            safePos += curr;
            t += safePos / 100;
            safePos %= 100;
        }
        p2 += t;
        if (safePos == 0) zeroes++;
    }
    fprintf(stdout, "%d, %d\n", zeroes, p2);
}

int main(int argc, char *argv[]) {
    char input[5000][LINE_SIZE];
    const int lineCount = readInput(input);
    parseInput(input, lineCount);
    return 0;
}
