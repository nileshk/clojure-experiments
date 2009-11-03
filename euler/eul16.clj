;; Use number-digits from eul8.clj

(defn exponent [x n]
  (loop [v x count 1]
    (if (= count n)
      v
      (recur (* v x) (inc count)))))

(defn eul11 []
  (reduce + (number-digits (exponent 2 1000))))
