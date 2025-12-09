input = ARGF.read.chomp
sections = input.split("\n\n")
fresh_ranges = sections[0].lines
ingredients = sections[1].lines

fresh_ranges.map! do |line|
  start, stop = line.split('-')
  Range.new(start.to_i, stop.to_i)
end

ingredients.map!(&:to_i)

fresh_ingredients = ingredients.select { |ingredient| fresh_ranges.any? { |range| range.include?(ingredient) } }
all_fresh_ingredient_count = fresh_ranges
  .sort_by(&:begin)
  .each_with_object([]) do |r, acc|
    if acc.empty? || acc.last.end < r.begin - 1
      acc << r
    else
      acc[-1] = (acc.last.begin..[acc.last.end, r.end].max)
    end
  end
  .sum(&:size)

puts "Part 1: #{fresh_ingredients.size}"
puts "Part 2: #{all_fresh_ingredient_count}"
