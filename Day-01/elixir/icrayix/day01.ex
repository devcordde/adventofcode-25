rotations =
  File.read!("day01.txt")
  |> String.split("\n", trim: true)
  |> Enum.map(fn
    <<"L", num::binary>> -> -String.to_integer(num)
    <<"R", num::binary>> -> String.to_integer(num)
  end)

{_, partOne, partTwo} =
  Enum.reduce(rotations, {50, 0, 0}, fn rotation, {curr, partOneCount, partTwoCount} ->
    next = Integer.mod(curr + rotation, 100)
    step = if rotation > 0, do: 1, else: -1

    {next, partOneCount + if(next == 0, do: 1, else: 0),
     partTwoCount +
       Enum.count((curr + step)..(curr + rotation)//step, fn num ->
         Integer.mod(num, 100) == 0
       end)}
  end)

IO.puts(partOne)
IO.puts(partTwo)
