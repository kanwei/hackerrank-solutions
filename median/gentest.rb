rand = Random.new()
n = 50000
puts n
n.times.each { |i|
    op = ["a", "r"].sample 
    puts "#{op} #{rand.rand(10000000)}"
}