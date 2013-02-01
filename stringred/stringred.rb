OTHER = {
	["a", "b"] => "c",
	["b", "a"] => "c",
	["a", "c"] => "b",
	["c", "a"] => "b",
	["b", "c"] => "a",
	["c", "b"] => "a"
}

min = 1.0/0.0
solve = lambda { |ary|
	if ary.length == 1
		return 1, true
	elsif ary.length == 2 && ary[0] == ary[1]
		return 2, true
	end
	min = ary.length
	(ary.length-1).times { |i|
		if other_char = OTHER[ ary[i..i+1] ]
			b = (ary[0...i] + [other_char] + ary[i+2..-1])
			val, stop = solve.call(b)
			return val, stop if stop
			min = [val, min].min
		end			
	}
	min
}

STDIN.gets.to_i.times {
	min, stop = solve.call STDIN.gets.strip.split("")
	puts min
}