f = File.open("input00.txt")

num_cases = f.gets.to_i

# b1 b2 b3 b4 b5 b6 b7 b8
# a1 a2 a3 a4 a5 a6 a7 a8


def sanitize(pos)
	return pos[0].ord-97, pos[1].to_i-1
end

def remove_impossible(coords)
	return coords.find_all {|(x,y)| x >= 0 && x < 8 && y >= 0 && y < 8}
end

def king_influence(pos)
	x, y = pos
	remove_impossible([
		[x-1, y+1], [x, y+1], [x+1, y+1],
		[x-1, y]  , [x, y],   [x+1, y],
		[x-1, y-1], [x, y-1], [x+1, y-1]
	])
end

def rook_influence(pos)
	x, y = pos
	valid = []
	(-10..10).each { |i|
		valid << [x+i, y]
		valid << [x, y+i]
	}
	remove_impossible(valid)
end

def flip_turn(turn)
	turn == :whiteturn ? :blackturn : :whiteturn
end

states = []
states_seen = []

num_cases.times {
	wk, wr, bk = f.gets.split(" ").collect {|x| sanitize(x.split("")) }
	states << [wk, wr, bk, :whiteturn]
	while state = states.shift
		wk, wr, bk, turn = state
		sorted = [wk, wr, bk].sort + [turn]
		next if states_seen.include?(sorted)
		states_seen << sorted
		# p "== State == #{state}"
		bk_possible = king_influence(bk) - king_influence(wk) - rook_influence(wr)
		wr_possible = rook_influence(wr) - wk - king_influence(bk)
		wk_possible = king_influence(wk) - wr - king_influence(bk)
		if bk_possible.empty?
			puts "OHSHIT"
			# exit
		end
		if turn == :whiteturn
			wr_possible.each { |wr_pos| states << [wk, wr_pos, bk, :blackturn] }
			wk_possible.each { |wk_pos| states << [wk_pos, wr, bk, :blackturn] }
		else
			bk_possible.each { |bk_pos| states << [wk, wr, bk_pos, :whiteturn] }
		end
	end

	exit
}
