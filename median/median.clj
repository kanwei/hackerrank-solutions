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

(defn format-answer [x]
  (if (instance? clojure.lang.Ratio x)
    (apply str (reverse (drop-while #(= \0 %) (reverse (format "%f" (double x))))))
    (str x)))

(defn solve-rec [lines ^java.util.PriorityQueue max-heap ^java.util.PriorityQueue min-heap out]
  ; (println max-heap min-heap (first lines))
  (when-not (empty? lines)
    (let [rest-lines (rest lines)
          [op n_str] (clojure.string/split (first lines) #" ")
          n (Integer. n_str)]
      (cond 
        (= op "r")
          (if-not (or (.remove max-heap n) (.remove min-heap n))
            ; Can't find in either heap, move on
            (do
              (.write out "Wrong!\n")
              (recur rest-lines max-heap min-heap out))
            (do
              (balance-heap max-heap min-heap)
              (.write out (format-answer (median max-heap min-heap)))
              (.newLine out)
              (recur rest-lines max-heap min-heap out)))
        (= op "a")
          (do 
            (.add max-heap n)
            (balance-heap max-heap min-heap)
            (.write out (format-answer (median max-heap min-heap)))
            (.newLine out)
            (recur rest-lines max-heap min-heap out))))))

(defn solve [src dest]
  (let [f (line-seq (clojure.java.io/reader src))
        out (clojure.java.io/writer dest)
        min-heap (java.util.PriorityQueue. 51000)
        max-heap (java.util.PriorityQueue. 51000 (comparator (fn [x y] (> x y))))]
    (solve-rec (rest f) max-heap min-heap out)
    (.flush out)))

(defn -main []
  (solve *in* *out*))

; (time (solve "/Users/kanwei/Projects/istreet/median/input01.txt" (doto (java.io.File/createTempFile "medians" ".out") .deleteOnExit)))
; (time (solve "/Users/kanwei/Projects/istreet/median/input01.txt" *out*))
