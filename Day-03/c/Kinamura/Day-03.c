#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LINE_SIZE 102
#define LINES 201

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-03.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

long long getMax(char input[][LINE_SIZE], int lines, int length) {
    long long sum = 0;
    int bankSize = strlen(input[0]);
    for(int i = 0; i < lines; i++) {
        char * rest;
        char * number = calloc(sizeof(char), length);
        int currPos = 0;
        while(input[i][currPos] != '\n' && input[i][currPos] != '\0') {
            for(int j = 0; j < length; j++) {
                if(input[i][currPos] > number[j] && bankSize - currPos - 1 > length - j - 1) {
                    number[j] = input[i][currPos];
                    for(int k = j + 1; k < length; k++) number[k] = '0';
                    break;
                }
            }
            currPos++;
        }
        sum += strtoll(number, &rest, 10);
        free(number);
    }
    return sum;
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    fprintf(stdout, "Part1: %lld\n", getMax(input, lineCount, 2));
    fprintf(stdout, "Part2: %lld\n", getMax(input, lineCount, 12));
    return 0;
}
