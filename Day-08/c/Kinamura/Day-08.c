#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define LINE_SIZE 30
#define LINES 1000

typedef struct Pos {
    int x,y,z;
} pos_t;

typedef struct Edge {
    int pos1, pos2;
    double dist;
} edge_t;

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-08.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

void parseInput(char input[][LINE_SIZE], int len, pos_t pos[]) {
    char *rest, *token;
    for (int i = 0; i < len; i++) {
        rest = input[i];
        token = strtok_r(rest, ",", &rest);
        pos[i].x = atoi(token);
        token = strtok_r(rest, ",", &rest);
        pos[i].y = atoi(token);
        pos[i].z = atoi(rest);
    }
}

int sortEdges(const void *a, const void *b) {
    const edge_t *edge1 = (const edge_t *)a;
    const edge_t *edge2 = (const edge_t *)b;
    if (edge1->dist < edge2->dist) return -1;
    if (edge1->dist > edge2->dist) return 1;
    return 0;
}

int sortInts(const void *a, const void *b) {
    int valA = *(const int *)a;
    int valB = *(const int *)b;
    return valB - valA;
}

int buildDistances(pos_t pos[], int len, edge_t edge[]) {
    int edgeCount = 0;
    for (int i = 0; i < len - 1; i++) {
        for (int j = i + 1; j < len; j++) {
            if (i == j) continue;
            edge[edgeCount].pos1 = i;
            edge[edgeCount].pos2 = j;
            edge[edgeCount].dist = sqrt(pow(pos[i].x - pos[j].x, 2) + pow(pos[i].y - pos[j].y, 2) + pow(pos[i].z - pos[j].z, 2));
            edgeCount++;
        }
    }
    return edgeCount;
}

int find(int x, int parent[]) {
    return (parent[x] != x) ? find(parent[x], parent) : parent[x];
}

int unite(int x, int y, int parent[], int rank[], int groups) {
    int rootX = find(x, parent), rootY = find(y, parent);
    if (rootX != rootY) {
        if (rank[rootX] < rank[rootY]) parent[rootX] = rootY;
        else if (rank[rootX] > rank[rootY]) parent[rootY] = rootX;
        else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        groups--;
    }
    return groups;
}

void unionFind(pos_t pos[], int len, edge_t edges[]) {
    int parent[1000], rank[1000], groups = len, i = 0;
    for (int i = 0; i < len; i++) {
        parent[i] = i;
        rank[i] = 0;
    }
    for (i; i < 1000; i++) groups = unite(edges[i].pos1, edges[i].pos2, parent, rank, groups);
    int groupSize[1000] = {0};
    for (int i = 0; i < len; i++) groupSize[find(i, parent)]++;
    qsort(groupSize, len, sizeof(int), sortInts);
    fprintf(stdout,"Part 1: %d \n",groupSize[0] * groupSize[1] * groupSize[2]);
    while (groups > 1) {
        groups = unite(edges[i].pos1, edges[i].pos2, parent, rank, groups);
        i++;
    }
    fprintf(stdout, "Part 2: %ld\n", (long)pos[edges[i -1].pos1].x * (long)pos[edges[i -1].pos2].x);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    pos_t pos[lineCount];
    parseInput(input, lineCount, pos);
    edge_t edges[(lineCount * (lineCount - 1)) / 2 + 1];
    int numEdges = buildDistances(pos, lineCount, edges);
    qsort(edges, numEdges, sizeof(edge_t), sortEdges);
    unionFind(pos, lineCount, edges);
    return 0;
}
