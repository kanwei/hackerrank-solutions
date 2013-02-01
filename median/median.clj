(ns solution (:gen-class))
(use 'clojure.test)

(defn median-sorted-drop [xs]
  (let [len (count xs)
        secondhalf (drop (quot (dec len) 2) xs)]
    (cond 
      (zero? len) nil
      (odd? len) (first secondhalf)
      (even? len) (/ (reduce + (take 2 secondhalf)) 2))))

(defn median-sorted [xs]
  ; {:pre [(vector? xs)]}
  (let [len (count xs)
        v1 (quot (dec len) 2)]
    (cond 
      (zero? len) nil
      (odd? len) (first (subvec xs v1 (inc v1)))
      (even? len) (/ (reduce + (subvec xs v1 (+ 2 v1))) 2))))

(deftest medians
  (is (= (median-sorted []) nil))
  (is (= (median-sorted [1]) 1))
  (is (= (median-sorted [1,2]) 3/2)) ; 0, 2
  (is (= (median-sorted [1,1,2]) 1)) ; 1, 2
  (is (= (median-sorted [1,2,3,4]) 5/2)) ; 1, 3
  (is (= (median-sorted [1 2 30 49 80 100]) 79/2))
  (is (= (median-sorted [1,2,3,4,5]) 3)))

(defn insert [n alon]
  (loop [alon alon, result []]
    (cond
     (empty? alon) (conj result n)
     (<= n (first alon)) (into result (cons n alon))
     :else (recur (rest alon) (conj result (first alon))))))

(defn remove-first-slow [el xs]
  (let [index_el (.indexOf xs el)]
    (if (neg? index_el)
      xs
      (let [[fsplit ssplit] (split-at index_el xs)]
        (concat fsplit (drop 1 ssplit))))))

(defn remove-first [el xs]
  ; {:pre [(vector? xs)]}
  (let [index_el (.indexOf xs el)]
    (if (neg? index_el)
      xs
      (let [left (subvec xs 0 index_el)
            right (subvec xs (inc index_el))]
        (into left right)))))

(deftest remove-firsts
  (is (= (remove-first 1 [3,1,2,3,4]) [3,2,3,4]))
  (is (= (remove-first 10 [3,1,2,3,4]) [3,1,2,3,4])))

(defn solve-rec [lines numbers output]
  ; (println lines numbers output)
  (if (empty? lines) output
    (let [rest_lines (rest lines)
          [op n_str] (clojure.string/split (first lines) #" ")
          n (read-string n_str)]
      (cond (= op "r")
              (let [n_removed (remove-first n numbers)]
                (if (or (empty? n_removed) (= numbers n_removed))
                  (recur rest_lines n_removed (conj output "Wrong!"))
                  (recur rest_lines n_removed (conj output (median-sorted n_removed)))))
            (= op "a")
              (let [new_numbers (insert n numbers)]
                (recur rest_lines new_numbers (conj output (median-sorted new_numbers))))))))

(defn format-answer [x]
  (if (instance? clojure.lang.Ratio x)
    (let [s (format "%f" (double x))]
      (apply str (reverse (drop-while #(= \0 %) (reverse s)))))
    (str x)))

(defn solve [src]
  (let [f (line-seq (clojure.java.io/reader src))
        nlines (read-string (first f))
        answers (solve-rec (rest f) [] [])]
    ; (dorun (map (comp println format-answer)))
    ))

(defn -main []
  (solve *in*))

(time (solve "/Users/kanwei/Projects/istreet/median/input02.txt"))
