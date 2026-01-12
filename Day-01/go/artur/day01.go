package main

import (
	"os"
	"strconv"
	"strings"
)

func main() {
	input, _ := os.ReadFile("input01.txt")
	lines := strings.Split(string(input), "\n")

	countP1 := 0
	countP2 := 0
	dial := int64(50)
	for _, line := range lines {
		increase, _ := strconv.ParseInt(line[1:], 10, 64)

		for ; increase != 0; increase-- {
			if line[0] == 'R' {
				dial = (dial + 1) % 100
			} else {
				dial = (dial - 1) % 100
			}
			if dial == 0 {
				countP2++
			}
		}
		if dial == 0 {
			countP1++
		}
	}
	println(countP1)
	println(countP2)
}
