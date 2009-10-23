;; Use string-to-vector from eul8.clj

(defn exponent [x n]
  (loop [v x count 1]
    (if (= count n)
      v
      (recur (* v x) (inc count)))))

(defn eul11 []
  (reduce + (map #(new Integer %) (string-to-vector (str (exponent 2 1000))))))
