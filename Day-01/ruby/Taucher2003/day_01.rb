input = ARGF.read.chomp
instructions = input.split("\n").map do |line|
  splitted = line.split(//, 2)
  [splitted[0], splitted[1].to_i]
end

def part1(instructions)
  current_dial = 50
  times_at_zero = 0

  instructions.each do |instruction|
    if instruction[0] == 'L'
      current_dial -= instruction[1]
    else
      current_dial += instruction[1]
    end

    times_at_zero += 1 if current_dial % 100 == 0
  end

  times_at_zero
end

def part2(instructions)
  current_dial = 50
  times_at_zero = 0

  instructions.each do |instruction|
    direction = instruction[0] == 'L' ? -1 : 1

    instruction[1].times do
      current_dial += direction
      times_at_zero += 1 if current_dial % 100 == 0
    end
  end

  times_at_zero
end

puts "Part 1: #{part1(instructions)}"
puts "Part 2: #{part2(instructions)}"
