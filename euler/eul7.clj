; functions taken from eul3.clj
(defn half-of [n]
  (if (zero? (mod n 2))
    (/ n 2)
    (/ (- n 1) 2)))

(defn half-of-odd [n]
  (let [h (half-of n)]
    (if (even? h) (inc h) h)))

(defn prime? [n]
  (if (= 1 n)
    false
    (if (= 2 n)
      true
      (if (even? n)
        false
        (loop [d (half-of-odd n)]
          (if (= d 1)
            true
            (if (= 0 (mod n d))
              false
              (recur (- d 2)))))))))

(defn eul7 [n]
  (last (take n (filter prime? (iterate inc 1)))))

(eul7 10001)
