num_items, diff = STDIN.gets.split(" ").collect {|x| x.to_i}
nums = STDIN.gets.split(" ").collect {|x| x.to_i}

hash = {}

pairs = 0
nums.each_with_index { |n, i|
	break if i == num_items
	if hash[n]
		pairs += hash[n]
		hash[n] = nil
	end
	[n+diff, n-diff].find_all{|x| x > 0 && x < 1000000000}.each {|neighbor|
		# puts "#{n}\t#{neighbor}"
		hash[neighbor] ||= 0
		hash[neighbor] += 1
	}
}
puts pairs