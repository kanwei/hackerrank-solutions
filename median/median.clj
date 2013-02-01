(ns solution (:gen-class))

(defn median [^java.util.PriorityQueue max-heap ^java.util.PriorityQueue min-heap]
  (let [max-size (.size max-heap)
        min-size (.size min-heap)]
    (cond
      (and (zero? max-size) (zero? min-size)) "Wrong!"
      (== max-size min-size)
        (/ (+ (.peek min-heap) (.peek max-heap)) 2)
      :else (.peek max-heap))))

(defn balance-heap [^java.util.PriorityQueue max-heap ^java.util.PriorityQueue min-heap]
  ; (println "balance: " max-heap min-heap)
  (let [max-size (.size max-heap)
        min-size (.size min-heap)]
    (cond
      (zero? max-size) nil
      (> max-size (inc min-size))
        (.add min-heap (.poll max-heap))
      (and (pos? min-size) (> max-size min-size) (> (.peek max-heap) (.peek min-heap)))
        (let [max-root (.poll max-heap)
              min-root (.poll min-heap)]
          (.add min-heap max-root)
          (.add max-heap min-root))
      (< max-size min-size)
        (.add max-heap (.poll min-heap)))))

(defn solve-rec [lines ^java.util.PriorityQueue max-heap ^java.util.PriorityQueue min-heap answers]
  ; (println max-heap min-heap (first lines) answers)
  (if (empty? lines) 
    answers
    (let [rest-lines (rest lines)
          [op n_str] (clojure.string/split (first lines) #" ")
          n (Integer. n_str)]
      (cond 
        (= op "r")
          (if-not (or (.remove max-heap n) (.remove min-heap n))
            ; Can't find in either heap, move on
            (recur rest-lines max-heap min-heap (conj answers "Wrong!"))
            (do
              (balance-heap max-heap min-heap)
              (recur rest-lines max-heap min-heap (conj answers (median max-heap min-heap)))))
        (= op "a")
          (do 
            (.add max-heap n)
            (balance-heap max-heap min-heap)
            (recur rest-lines max-heap min-heap (conj answers (median max-heap min-heap))))))))

(defn format-answer [x]
  (if (instance? clojure.lang.Ratio x)
    (apply str (reverse (drop-while #(= \0 %) (reverse (format "%f" (double x))))))
    (str x)))

(defn solve [src]
  (let [f (line-seq (clojure.java.io/reader src))
        min-heap (java.util.PriorityQueue.)
        max-heap (java.util.PriorityQueue. 10 (comparator (fn [x y] (> x y))))
        answers (solve-rec (rest f) max-heap min-heap [])
        print-answer (comp println format-answer)]
    #_(doseq [a answers] (print-answer a))))

(defn -main []
  (solve *in*))

(time (solve "/Users/kanwei/Projects/istreet/median/input00.txt"))
