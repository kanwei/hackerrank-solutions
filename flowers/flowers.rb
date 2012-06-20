_, n_friends = STDIN.gets.split(" ").collect(&:to_i)
prices = STDIN.gets.split(" ").collect(&:to_i)
sum = 0
prices.sort.reverse.each_with_index { |price, i|
	sum += (price * (1 + i/n_friends))
}
puts sum