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
      (< max-size min-size)
        (.add max-heap (.poll min-heap))
      (and (pos? min-size) (> max-size min-size) (> (.peek max-heap) (.peek min-heap)))
        (let [max-root (.poll max-heap)
              min-root (.poll min-heap)]
          (.add min-heap max-root)
          (.add max-heap min-root)))))

(defn format-answer [x]
  (if (instance? clojure.lang.Ratio x)
    (apply str (reverse (drop-while #(= \0 %) (reverse (format "%f" (double x))))))
    (str x)))

(defn solve [src dest]
  (let [in (clojure.java.io/reader src)
        out (clojure.java.io/writer dest)
        total-lines (.readLine in)
        min-heap (java.util.PriorityQueue. 50005)
        max-heap (java.util.PriorityQueue. 50005 (comparator (fn [x y] (> x y))))]
    (try
      (loop [seen {}]
        (let [op (.read in)
              space (.read in)
              n (Integer/parseInt (.readLine in))]
          (cond 
            (== op 114) ; "r"
              (if (and (contains? seen n)
                        (if (<= n (.peek max-heap))
                          (.remove max-heap n)
                          (.remove min-heap n)))
                (do
                  (balance-heap max-heap min-heap)
                  (.write out (format-answer (median max-heap min-heap)))
                  (.newLine out)
                  (recur seen))
                ; Can't find in either heap, move on
                (do
                  (.write out "Wrong!\n")
                  (recur seen)))
            (== op 97) ; "a"
              (do 
                (.add max-heap n)
                (balance-heap max-heap min-heap)
                (.write out (format-answer (median max-heap min-heap)))
                (.newLine out)
                (recur (assoc seen n true))))))
      (catch Exception e)
      (finally (.flush out)))))

(defn -main []
  (solve *in* *out*))

; (time (solve "/Users/kanwei/Projects/istreet/median/input01.txt" (doto (java.io.File/createTempFile "medians" ".out") .deleteOnExit)))
; (time (solve "/Users/kanwei/Projects/istreet/median/input01.txt" *out*))
