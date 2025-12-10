#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <stdbool.h>

#include <z3/z3.h>

#define LINE_SIZE 300
#define LINES 1000

int readInput(char prompts[][LINE_SIZE]){
    FILE *input = fopen("./Day-10.txt", "r");
    int i = 0;
    if (input != NULL){
        while (fgets(prompts[i++],LINE_SIZE,input) != NULL) {}
    }
    return (fclose(input) & 0) | (i - 1);
}

int calculateMapping(int numLamps, uint32_t lampMask, uint32_t opsMask[], int numOps) {
    uint32_t best = 0;
    int bestPop = 1000000, total = 1 << numOps;
    for (int mask = 0; mask < total; mask++) {
        uint32_t acc = 0;
        for (int k = 0; k < numOps; k++) {
            if (mask & (1u << k)) acc ^= opsMask[k];
        }
        if (acc != lampMask) continue;
        int pc = __builtin_popcount(mask);
        if (pc < bestPop) {
            bestPop = pc;
            best = (uint32_t)mask;
        }
    }
    return bestPop;
}

int solve_with_z3(int numLamps, uint32_t opsMask[], int numOps, short jolt[]) {
    Z3_config cfg = Z3_mk_config();
    Z3_context ctx = Z3_mk_context(cfg);
    Z3_del_config(cfg);
    Z3_optimize opt = Z3_mk_optimize(ctx);
    Z3_optimize_inc_ref(ctx, opt);
    Z3_sort int_sort = Z3_mk_int_sort(ctx);
    Z3_ast x[32];

    for (int k = 0; k < numOps; k++) {
        char name[16];
        snprintf(name, sizeof(name), "x%d", k);
        Z3_symbol s = Z3_mk_string_symbol(ctx, name);
        x[k] = Z3_mk_const(ctx, s, int_sort);
        Z3_ast zero = Z3_mk_int(ctx, 0, int_sort);
        Z3_ast ge0  = Z3_mk_ge(ctx, x[k], zero);
        Z3_optimize_assert(ctx, opt, ge0);
    }

    for (int i = 0; i < numLamps; i++) {
        Z3_ast terms[32];
        unsigned tcount = 0;
        for (int k = 0; k < numOps; k++) {
            if ((opsMask[k] >> i) & 1u) {
                Z3_ast coeff = Z3_mk_int(ctx, 1, int_sort);
                Z3_ast prod  = Z3_mk_mul(ctx, 2, (Z3_ast[]){ coeff, x[k] });
                terms[tcount++] = prod;
            }
        }
        Z3_ast sum;
        if (tcount == 0) sum = Z3_mk_int(ctx, 0, int_sort);
        else if (tcount == 1) sum = terms[0];
        else sum = Z3_mk_add(ctx, tcount, terms);
        Z3_ast rhs = Z3_mk_int(ctx, jolt[i], int_sort);
        Z3_ast eq  = Z3_mk_eq(ctx, sum, rhs);
        Z3_optimize_assert(ctx, opt, eq);
    }

    Z3_ast total_terms[32];
    for (int k = 0; k < numOps; k++) total_terms[k] = x[k];
    Z3_ast total;
    if (numOps == 0) total = Z3_mk_int(ctx, 0, int_sort);
    else if (numOps == 1) total = x[0];
    else total = Z3_mk_add(ctx, numOps, total_terms);
    Z3_optimize_minimize(ctx, opt, total);
    Z3_lbool res = Z3_optimize_check(ctx, opt, 0, NULL);
    int result = -1;

    if (res == Z3_L_TRUE) {
        Z3_model m = Z3_optimize_get_model(ctx, opt);
        Z3_model_inc_ref(ctx, m);
        int sum = 0;
        for (int k = 0; k < numOps; k++) {
            Z3_ast val;
            if (Z3_model_eval(ctx, m, x[k], true, &val)) {
                int v;
                if (Z3_get_numeral_int(ctx, val, &v)) sum += v;
            }
        }
        result = sum;
        Z3_model_dec_ref(ctx, m);
    } else result = -1;

    Z3_optimize_dec_ref(ctx, opt);
    Z3_del_context(ctx);
    return result;
}

void parseInput(char input[][LINE_SIZE], int len) {
	int sum = 0;
	long sum2 = 0;
	char lampsChar[40], opsChar[20][40], joltChar[40];
	for (int i = 0; i < len; i++) {
		int pos = 0;
		char *token, *rest;
		rest = input[i];
		while((token = strtok_r(rest, " ", &rest)) != NULL) {
			if (token[0] == '[') strcpy(lampsChar, token);
			if (token[0] == '(') strcpy(opsChar[pos++], token);
			if (token[0] == '{') strcpy(joltChar, token);
		}
		short lamp[50] = {-1}, ops[20][20], jolt[50] = {-1};
		uint32_t lampMask = 0, opsMask[20] = {0};
		int numLamps = 0;
		for (int i = 0; i < strlen(lampsChar); i++) {
			if (lampsChar[i] == '#' || lampsChar[i] =='.') {
				if (lampsChar[i] == '#') lampMask |= (1u << numLamps);
			numLamps++;
			}
		}
		for (int i = 0; i < pos; i++) {
			int count = 0;
			rest = opsChar[i];
			while((token = strtok_r(rest, ",", &rest)) != NULL) {
				if(token[0] == '(') token++;
				opsMask[i] |= (1u << atoi(token));
			}
		}
		rest = joltChar;
		int count = 0;
		while((token = strtok_r(rest, ",", &rest)) != NULL) {
			if(token[0] == '{') token++;
			jolt[count++] = atoi(token);
		}
		int curr = calculateMapping(numLamps, lampMask, opsMask, pos);
	    int currJolt = solve_with_z3(numLamps, opsMask, pos, jolt);
		sum += curr;
		sum2 += currJolt;
	}
	fprintf(stdout, "Part 1:%d\nPart 2:%ld\n", sum, sum2);
}

int main(int argc, char *argv[]) {
    char input[LINES][LINE_SIZE];
    const int lineCount = readInput(input);
    parseInput(input, lineCount);
    return 0;
}
