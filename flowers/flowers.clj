(ns solution (:gen-class))

(defn solve-rec [xs i n]
    (if (empty? xs) 0
        (+ (reduce + (map (partial * (+ 1 i)) (take n xs)))
            (solve-rec (drop n xs) (+ 1 i) n))))

(defn solve [xs n_friends]
    (solve-rec (reverse (sort xs)) 0 n_friends))

(defn read-to-ints [x] (map read-string (clojure.string/split x #" ")))
(defn -main []
    (let [  reader (java.io.BufferedReader. *in*)
            [_, n_friends] (read-to-ints (.readLine reader))
            xs (read-to-ints (.readLine reader))]
        (println (solve xs n_friends))))
