#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define LINE_SIZE 500

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-02.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

int parseInput(char input[][LINE_SIZE], int lines, long ranges[][2]) {
    char * token;
    char * rest;
    int entries = 0;
    rest = input[0];
    while((token = strtok_r(rest, ",", &rest))) {
        char * p2rest;
        char * p2token;
        p2rest = token;
        p2token = strtok_r(p2rest, "-", &p2rest);
        ranges[entries][0] = strtol(p2token, &token, 10);
        ranges[entries][1] = strtol(p2rest, &token, 10);
        entries++;
    }
    return entries;
}

void findIDs(long ranges[][2], int entries) {
    long long sum = 0;
    for(int i = 0; i < entries; i++) {
        for(long j = ranges[i][0]; j <= ranges[i][1]; j++) {
            long split = (log10(j) + 1)/2, div = pow(10,split);
            if (j/div == j%div)   sum += j;
        }
    }
    fprintf(stdout, "Part 1:%lld\n", sum);
}

int checkID(long id) {
    for(int i = 1; i <= (log10(id) + 1) / 2; i++) {
        long div = pow(10,i), part = id % div, curr = id /div;
        while(curr >= part) {
            if(curr%div != part || part < div/10) break;
            if(curr == part) return 1;
            curr /= div;
        }
    }
    return 0;
}

void findNewIDs(long ranges[][2], int entries) {
    long long sum = 0;
    for (int i = 0; i < entries; i++) {
        for (long j = ranges[i][0]; j <= ranges[i][1]; j++) {
            if(checkID(j)) sum += j;
        }
    }
    fprintf(stdout, "Part 2:%lld\n", sum);
}

int main(int argc, char *argv[]) {
    char input[2][LINE_SIZE];
    const long lineCount = readInput(input);
    long ranges[1000][2];
    int entries = parseInput(input, lineCount, ranges);
    findIDs(ranges, entries);
    findNewIDs(ranges, entries);
    return 0;
}
