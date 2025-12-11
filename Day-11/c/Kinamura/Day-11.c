#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LINE_SIZE 120
#define LINES 600

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-11.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

int getId(char nodes[][4], int len, char tar[4]) {
    for (int i = 0; i < len; i++) {
        if (strcmp(tar, nodes[i]) == 0) return i;
    }
    if (strcmp(tar, "out")) {printf("%s\n", tar); return -1;}
    return len;
}

void parseInput(char prompts[][LINE_SIZE], int len, int adjMatr[][len + 1], char nodes[][4]) {
    for (int i = 0; i < len; i++) {
        char temp[LINE_SIZE];
        strcpy(temp, prompts[i]);
        char *token = strtok(temp, ":");
        strcpy(nodes[i], token);
        nodes[i][3] = '\0';
    }
    for (int i = 0; i < len; i++) {
        char temp[LINE_SIZE], *token, *rest = temp;
        strcpy(temp, prompts[i]);
        strtok_r(rest, ":", &rest);
        rest++;
        while ((token = strtok_r(rest, " ", &rest)) != NULL) {
            token[3] = '\0';
            int id = getId(nodes, len, token);
            if (id != -1) adjMatr[i][id] = 1;
        }
    }
}

long long traverseGraph(int len, int adjMatr[][len + 1], char nodes[][4], int start, int tar, long long mem[]) {
    if (start == tar) return 1;
    if (mem[start] != -1) return mem[start];
    long long sum = 0;
    for (int i = 0; i < len + 1; i++) {
        if (adjMatr[start][i] == 1) sum += traverseGraph(len, adjMatr, nodes, i, tar, mem);
    }
    mem[start] = sum;
    return sum;
}

long long prepTraversal(int len, int adjMatr[][len + 1], char nodes[][4], char * start, char * tar) {
    long long mem[len];
    for (int i = 0; i < len; i++) mem[i] = -1;
    int startID = getId(nodes, len, start);
    int endID = getId(nodes, len, tar);
    return traverseGraph(len, adjMatr, nodes, startID, endID, mem);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    int adjMatr[lineCount][lineCount + 1];
    for (int i = 0; i < lineCount; i++) {
        for (int j = 0; j < lineCount + 1; j++) adjMatr[i][j] = 0;
    }
    char nodes[lineCount][4];
    parseInput(input, lineCount, adjMatr, nodes);
    int startID = getId(nodes, lineCount, "you");
    fprintf(stdout, "Part 1:%lld\n", prepTraversal(lineCount, adjMatr, nodes, "you", "out"));
    long long part2 = prepTraversal(lineCount, adjMatr, nodes, "fft", "out") *
                      prepTraversal(lineCount, adjMatr, nodes, "dac", "fft") *
                      prepTraversal(lineCount, adjMatr, nodes, "svr", "dac") +
                      prepTraversal(lineCount, adjMatr, nodes, "dac", "out") *
                      prepTraversal(lineCount, adjMatr, nodes, "fft", "dac") *
                      prepTraversal(lineCount, adjMatr, nodes, "svr", "fft");
    fprintf(stdout, "Part 2:%lld\n", part2);
    return 0;
}
