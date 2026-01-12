package main

import (
	"os"
	"strconv"
	"strings"
)

func main() {
	input, _ := os.ReadFile("input02.txt")
	doubleIds := strings.Split(string(input), ",")
	result := 0
	for _, idRange := range doubleIds {
		idSplit := strings.Split(idRange, "-")
		id1, _ := strconv.Atoi(idSplit[0])
		id2, _ := strconv.Atoi(idSplit[1])
		for ; id1 <= id2; id1++ {
			id1String := strconv.Itoa(id1)
			if len(id1String)%2 == 1 {
				continue
			}
			if id1String[:len(id1String)/2] == id1String[len(id1String)/2:] {
				result += id1
			}
		}
	}
	println(result)
}
