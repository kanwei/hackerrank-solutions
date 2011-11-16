OTHER = {
	"ab" => "c",
	"ac" => "b",
	"bc" => "a"
}

def solve(str)
	ary = str.split("")
	if ary.length == 1
		return 1, true
	end
	if ary.length == 2 && ary[0] == ary[1]
		return 2, true
	end
	min = ary.length
	(ary.length-1).times { |i|
		if ary[i] != ary[i+1]
			other_char = OTHER[ [ary[i], ary[i+1]].sort.join("") ]
			b = (ary[0...i] + [other_char] + ary[i+2..-1]).join("")
			# puts "#{str}\t#{b}"
			val, stop = solve(b)
			return val, stop if stop
			min = [val, min].min
		end			
	}
	min
end

STDIN.gets.to_i.times {
	min, stop = solve STDIN.gets.strip
	puts min
}