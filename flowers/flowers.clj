(ns solution (:gen-class))

(defn sum [xs] (reduce + xs))
(defn solve 
    ([xs n_friends] (solve (reverse (sort xs)) 0 n_friends))
    ([xs i n]
    (if (empty? xs) 0
        (+ (sum (map (partial * (inc i)) (take n xs)))
            (solve (drop n xs) (inc i) n)))))

(defn read-to-ints [x] (map read-string (clojure.string/split x #" ")))
(defn -main []
    (let [  reader (java.io.BufferedReader. *in*)
            [_, n_friends] (read-to-ints (.readLine reader))
            xs (read-to-ints (.readLine reader))]
        (println (solve xs n_friends))))
