(defn factorial [n]
  (loop [v n p 1]
    (if (= v 1)
      p
      (recur (dec v) (* p v)))))

(defn eul20 []
  (reduce + (number-digits (factorial 100))))