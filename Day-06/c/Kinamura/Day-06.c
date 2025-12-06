#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

#define LINE_SIZE 3800
#define LINES 5

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-06.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

void calculateSum(long long val[][4], char op[], int entries) {
    unsigned long long sum = 0, tempSum = 0;
    for (int i = 0; i < entries; i++) {
        if (op[i] == '+') sum += val[i][0] + val[i][1] + val[i][2] + val[i][3];
        else sum += val[i][0] * val[i][1] * val[i][2] * val[i][3];
    }
    fprintf(stdout, "Part 1:%llu\n", sum);
}

void readBackwards(char inputs[][LINE_SIZE], char op[], int maxPos) {
    int pos = maxPos - 1;
    long long sum = 0, tempSum = (op[pos] == '*') ? 1 : 0, temp = 0;
    for (int i = strlen(inputs[0]) - 2; i >= 0; i--) {
        for (int j = 0; j < 4; j++) {
            if (inputs[j][i] > 47 && inputs[j][i] < 58) temp = temp * 10 + inputs[j][i] - 48;
        }
        if (temp > 0) {
            tempSum = (op[pos] == '*') ? tempSum * temp : tempSum + temp;
            temp = 0;
        } else {
            sum += tempSum;
            pos--;
            tempSum = (op[pos] == '*') ? 1 : 0;
        }
    }
    fprintf(stdout, "Part 2:%llu\n", sum + tempSum);
}

void parseInput(char inputs[][LINE_SIZE], int lines, long long values[][4], char op[]) {
    int pos = 0, ops = 0;
    char *token, *rest;
    for (int i = 0; i < lines - 1; i++) {
        pos = 0;
        rest = inputs[i];
        while ((token = strtok_r(rest, " ", &rest)) != NULL) values[pos++][i] = atoi(token);
    }
    int maxPos = pos;
    pos = 0;
    while (pos  < maxPos) {
        if (inputs[lines - 1][ops] == '+' || inputs[lines - 1][ops] == '*') op[pos++] = inputs[lines - 1][ops];
        ops++;
    }
    calculateSum(values, op, maxPos);
    readInput(inputs);
    readBackwards(inputs, op, maxPos);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    long long values[1200][4];
    char operation[1200];
    parseInput(input, lineCount, values, operation);
    return 0;
}
