;; TOO SLOW

;; Use prime? function from #3

(defn eul10 []
  (loop [i 3 s 2]
    (if (>= i 2000000)
      s
      (recur (+ i 2)
             (if (prime? i) (+ s i) s)))))