nodes = list(map(lambda line: (line.split(": ")[0], set(line.split(": ")[1].split())), open("input11.txt").read().split("\n")))
node_paths = {node[0]: node[1] for node in nodes}
node_paths["out"] = set()

node_values = {node[0]: [0, 0, 0, 0] for node in nodes}
node_values["out"] = [1, 0, 0, 0]

finished = {"out"}

while True:
    for node in node_values:
        if node not in finished:
            for i in finished.intersection(node_paths[node]):
                if node == "dac":
                    node_values[node][1] += node_values[i][0]
                    node_values[node][1] += node_values[i][1]
                    node_values[node][3] += node_values[i][2]
                    node_values[node][3] += node_values[i][3]
                elif node == "fft":
                    node_values[node][2] += node_values[i][0]
                    node_values[node][3] += node_values[i][1]
                    node_values[node][2] += node_values[i][2]
                    node_values[node][3] += node_values[i][3]
                else:
                    node_values[node][0] += node_values[i][0]
                    node_values[node][1] += node_values[i][1]
                    node_values[node][2] += node_values[i][2]
                    node_values[node][3] += node_values[i][3]

                node_paths[node].remove(i)

            if len(node_paths[node]) == 0:
                finished.add(node)

    if "svr" in finished and "you" in finished:
        break

print("Part 1:", sum(node_values["you"]))
print("Part 2:", node_values["svr"][3])
