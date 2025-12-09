#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define LINE_SIZE 30
#define LINES 1000

typedef struct {
    long x, y;
} Point;

typedef struct Edge {
    Point start;
    long len;
} Edge;

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-09.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

int compareVertEdges(const void *a, const void *b) {
    Edge e1 = *(const Edge *)a, e2= *(const Edge *)b;
    if (e1.start.x > e2.start.x) return 1;
    if (e1.start.x < e2.start.x) return -1;
    return 0;
}

void parseInput(char input[][LINE_SIZE], int len, Point points[], int lineCount) {
    for (int i = 0; i < len; i++) {
        char *token, *rest = input[i];
        token = strtok_r(rest, ",", &rest);
        points[i].x = atoi(token);
        points[i].y = atoi(rest);
    }
}

long findSquare(Point points[], int len) {
    long currMax = 0;
    for (int i = 0; i < len - 1; i++) {
        for (int j = i + 1; j < len; j++) {
            if ((long)(points[i].x - points[j].x + 1) * (long)(points[i].y - points[j].y + 1) > currMax)
                currMax = (long)(points[i].x - points[j].x + 1) * (long)(points[i].y - points[j].y + 1);
        }
    }
    return currMax;
}

int checkPoint(long x, long y, Edge vert[], int numVert, Edge horiz[], int numHoriz) {
    int up = 0, down = 0;
    for (int i = 0; i < numVert; i++) {
        Edge e = vert[i];
        if (e.start.x > x) return (up || down) ? 1 : 0;
        if (y >= e.start.y && y < (e.start.y + e.len)) {
            if (e.start.x == x)  return 1;
            up = !up;
        }
        if (y > e.start.y && y <= (e.start.y + e.len)) {
            if (e.start.x == x) return 1;
            down = !down;
        }
    }
    return (up || down) ? 1 : 0;
}

int checkRect(Point points[], Point point1, Point point2, int numPoints, Edge vert[], int numVert, Edge horiz[], int numHoriz) {
    long top = (point1.y < point2.y) ? point1.y : point2.y;
    long left = (point1.x < point2.x) ? point1.x : point2.x;
    long right = (point1.x > point2.x) ? point1.x : point2.x;
    long bot = (point1.y > point2.y) ? point1.y : point2.y;
    for (int i = 0; i < numPoints; i++) {
        Point point = points[i];
        if (point.y >= top && point.y <= bot && point.x >= left && point.x <= right) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if ((point.y + i >= top && point.y + i <= bot && point.x + j >= left && point.x + j <= right)
                        && !checkPoint(point.x + j, point.y + i, vert, numVert, horiz, numHoriz)) {
                        return 0;
                    }
                }
            }
        }
    }
    for (int i = 0; i < numVert; i++) {
        Edge e = vert[i];
        if (e.start.x > left && e.start.x < right && e.start.y < top && e.start.y + e.len > bot) return 0;
    }
    for (int i = 0; i < numHoriz; i++) {
        Edge e = horiz[i];
        if (e.start.y > top && e.start.y < bot && e.start.x < left && e.start.x + e.len > right) return 0;
    }
    return 1;
}

long findInsideRect(Point points[], int numPoints) {
    Edge vert[500], horiz[500];
    int numVert = 0, numHoriz = 0;
    long maxSize = 0;
    Point prev = points[numPoints - 1];
    for (int i = 0; i < numPoints; i++) {
        Point start;
        long length;
        if (points[i].x == prev.x) {
            if (points[i].y < prev.y) start = points[i];
            else start = prev;
            length = abs(points[i].y - prev.y);
            vert[numVert++] = (Edge){start, length};
        }
        else {
            if (points[i].x < prev.x) start = points[i];
            else start = prev;
            length = abs(points[i].x - prev.x);
            horiz[numHoriz++] = (Edge){start, length};
        }
        prev = points[i];
    }
    qsort(vert, numVert, sizeof(Edge), compareVertEdges);
    for (int i = 0; i < numPoints; i++) {
        for (int j = 0; j < numPoints; j++) {
            long size = (abs(points[i].x-points[j].x) + 1) * (abs(points[i].y-points[j].y) + 1);
            if (checkRect(points, points[i], points[j], numPoints, vert, numVert, horiz, numHoriz))
                maxSize = (maxSize > size) ? maxSize : size;
        }
    }
    return maxSize;
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    Point points[500];
    parseInput(input, lineCount, points, lineCount);
    fprintf(stdout, "Part 1: %ld\n", findSquare(points, lineCount));
    fprintf(stdout, "Part 2: %ld\n", findInsideRect(points, lineCount));
    return 0;
}
