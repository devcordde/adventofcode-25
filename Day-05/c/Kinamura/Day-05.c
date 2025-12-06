#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LINE_SIZE 33
#define LINES 1200

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-05.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

int compare_ranges(const void *a, const void *b) {
    unsigned long long *range_a = (unsigned long long *)a;
    unsigned long long *range_b = (unsigned long long *)b;
    return (range_a[0] < range_b[0]) ? -1: 1;
}

void countFresh(unsigned long long ranges[][2], unsigned long long ids[], int rSize, int iSize) {
    int freshCount = 0;
    for (int i = 0; i < iSize; i++) {
        for (int j = 0; j < rSize; j++) {
            if (ids[i] >= ranges[j][0] && ids[i] <= ranges[j][1]) {
                freshCount++;
                break;
            }
        }
    }
    fprintf(stdout, "Part 1:%d\n", freshCount);
}

void countMaxFresh(unsigned long long ranges[][2], int rSize) {
    unsigned long long maxCount = 0;
    qsort(ranges, rSize, sizeof(unsigned long long[2]), compare_ranges);
    unsigned long long (*merged)[2] = calloc(sizeof(unsigned long long[2]), rSize);
    merged[0][0] = ranges[0][0];
    merged[0][1] = ranges[0][1];
    int mergedCount = 1;
    for (int i = 1; i < rSize; i++) {
        unsigned long long curr_start = ranges[i][0], curr_end = ranges[i][1];
        unsigned long long * last_merged = merged[mergedCount - 1];
        if (curr_start <= last_merged[1] + 1) {
            if (curr_end > last_merged[1]) last_merged[1] = curr_end;
        } else {
            merged[mergedCount][0] = curr_start;
            merged[mergedCount][1] = curr_end;
            mergedCount++;
        }
    }
    for (int i = 0; i < mergedCount; i++) {
        maxCount += merged[i][1] - merged[i][0] + 1;
    }
    free(merged);
    fprintf(stdout, "Part 2:%llu\n", maxCount);
}

void parseInput(char inputs[][LINE_SIZE], int lines, unsigned long long ranges[][2], unsigned long long ids[]) {
    int step = 0, id_idx = 0, range_idx = 0;
    for (int i = 0; i < lines; i++) {
        if (strlen(inputs[i]) == 1) { step++; continue; }
        if (step) ids[id_idx++] = strtoll(inputs[i], NULL, 10);
        else {
            char *p1, *p2, *rest;
            p2 = inputs[i];
            p1 = strtok_r(p2, "-", &p2);
            ranges[range_idx][0] = strtoull(p1, &rest, 10);
            ranges[range_idx++][1] = strtoull(p2, &rest, 10);
        }
    }
    countFresh(ranges, ids, range_idx, id_idx);
    countMaxFresh(ranges, range_idx);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    unsigned long long ids[LINES], ranges[LINES][2];
    parseInput(input, lineCount, ranges, ids);
    return 0;
}
