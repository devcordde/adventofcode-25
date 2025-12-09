defmodule Playground do
  def make_connection(circuits, {box_one, box_two}) do
    case {Enum.find(circuits, &MapSet.member?(&1, box_one)),
          Enum.find(circuits, &MapSet.member?(&1, box_two))} do
      {nil, nil} ->
        [MapSet.new([box_one, box_two]) | circuits]

      {circuit, nil} ->
        List.replace_at(
          circuits,
          Enum.find_index(circuits, &(&1 == circuit)),
          MapSet.put(circuit, box_two)
        )

      {nil, circuit} ->
        List.replace_at(
          circuits,
          Enum.find_index(circuits, &(&1 == circuit)),
          MapSet.put(circuit, box_one)
        )

      {circuit_one, circuit_two} when circuit_one == circuit_two ->
        circuits

      {circuit_one, circuit_two} ->
        List.replace_at(
          circuits,
          Enum.find_index(circuits, &(&1 == circuit_one)),
          MapSet.union(circuit_one, circuit_two)
        )
        |> List.delete(circuit_two)
    end
  end

  def shortest_connections(boxes) do
    combinations(boxes, [])
    |> Enum.map(fn {x, y} -> {{x, y}, distance_squared(x, y)} end)
    |> List.keysort(1)
    |> Enum.map(&elem(&1, 0))
  end

  def distance_squared(boxOne, boxTwo) do
    Integer.pow(elem(boxOne, 0) - elem(boxTwo, 0), 2) +
      Integer.pow(elem(boxOne, 1) - elem(boxTwo, 1), 2) +
      Integer.pow(elem(boxOne, 2) - elem(boxTwo, 2), 2)
  end

  def combinations([], acc), do: acc

  def combinations([head | rest], acc),
    do:
      combinations(
        rest,
        for el <- rest do
          {head, el}
        end ++ acc
      )
end

boxes =
  File.read!("day08.txt")
  |> String.split("\n", trim: true)
  |> Enum.map(fn box ->
    String.split(box, ",") |> Enum.map(&String.to_integer/1) |> List.to_tuple()
  end)

shortest_connections = Playground.shortest_connections(boxes)

# part one
shortest_connections
|> Enum.take(1000)
|> Enum.reduce([], fn connection, circuits ->
  Playground.make_connection(circuits, connection)
end)
|> Enum.map(&Enum.count/1)
|> Enum.sort(:desc)
|> Enum.take(3)
|> Enum.product()
|> IO.puts()

# part two
boxes_count = Enum.count(boxes)

{{x1, _, _}, {x2, _, _}} =
  Enum.reduce_while(shortest_connections, [], fn connection, circuits ->
    circuits = Playground.make_connection(circuits, connection)

    if Enum.at(circuits, 0) |> Enum.count() == boxes_count do
      {:halt, connection}
    else
      {:cont, circuits}
    end
  end)

IO.puts(x1 * x2)
