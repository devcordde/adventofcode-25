lines = File.read!("day07.txt") |> String.split("\n", trim: true)

splitter_layers =
  Enum.map(lines, fn line ->
    String.to_charlist(line)
    |> Enum.with_index()
    |> Enum.filter(fn {c, _} -> c == ?^ end)
    |> Enum.map(fn {_, i} -> i end)
  end)

start =
  Enum.at(lines, 0) |> String.to_charlist() |> Enum.find_index(fn c -> c == ?S end)

# part one
Enum.reduce(splitter_layers, {[start], 0}, fn splitters, {beams, splits} ->
  {beams, splits} =
    Enum.reduce(beams, {[], splits}, fn beam, {beams, splits} ->
      case Enum.member?(splitters, beam) do
        true -> {[beam - 1, beam + 1] ++ beams, splits + 1}
        false -> {[beam | beams], splits}
      end
    end)

  {Enum.uniq(beams), splits}
end)
|> elem(1)
|> IO.puts()

# part two
Enum.reduce(splitter_layers, [{start, 1}], fn splitters, beams ->
  Enum.reduce(beams, [], fn {beam, count}, beams ->
    case Enum.member?(splitters, beam) do
      true -> [{beam - 1, count}, {beam + 1, count}] ++ beams
      false -> [{beam, count} | beams]
    end
  end)
  |> Enum.group_by(fn {beam, _} -> beam end)
  |> Enum.map(fn {beam, counts} -> {beam, Enum.sum_by(counts, &elem(&1, 1))} end)
end)
|> Enum.sum_by(&elem(&1, 1))
|> IO.puts()
