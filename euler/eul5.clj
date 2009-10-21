(defn divisible? [n last]
  (loop [d last]
    (if (not (zero? (mod n d)))
      false
      (if (= d 2)
        true
        (recur (dec d))))))

(defn eul5 [last]
  (loop [n 2520]
    (if (divisible? n last)
      n
      (recur (inc n)))))

(eul5 20)
