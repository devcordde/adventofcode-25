#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LINE_SIZE 140
#define LINES 140

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-04.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

int countAccessibleRolls(char input[][LINE_SIZE], int lines, int replace) {
    int count = 0, ls = strlen(input[0]) - 1;
    for (int i = 0; i < lines; i++) {
        for (int j = 0; j < ls; j++) {
            if (input[i][j] != '@') continue;
            int adj = 0;
            if (i - 1 >= 0 && j - 1 >= 0 && input[i - 1][j - 1] == '@') adj++;
            if (i + 1 < lines && j - 1 >= 0 && input[i + 1][j - 1] == '@') adj++;
            if (i - 1 >= 0 && input[i - 1][j] == '@') adj++;
            if (i + 1 < lines && input[i + 1][j] == '@') adj++;
            if (i - 1 >= 0 && j + 1 < ls && input[i - 1][j + 1] == '@') adj++;
            if (i + 1 < lines && j + 1 < ls && input[i + 1][j + 1] == '@') adj++;
            if (j - 1 >= 0 && input[i][j - 1] == '@') adj++;
            if (j + 1 < ls && input[i][j + 1] == '@') adj++;
            if (adj < 4) count++;
            if (replace && adj < 4) input[i][j] = 'x';
        }
    }
    return count;
}

int loopReplacement(char input[][LINE_SIZE], int lines) {
    int sum = 0, newReplace;
    do {
        newReplace = 0;
        newReplace = countAccessibleRolls(input, lines, 1);
        sum += newReplace;
    } while (newReplace != 0);
    return sum;
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    fprintf(stdout, "Part 1: %d\n", countAccessibleRolls(input, lineCount, 0));
    fprintf(stdout, "Part 2: %d\n", loopReplacement(input, lineCount));
    return 0;
}
