;; Euler #3 doesn't work yet
(defn half-of [n]
  (if (zero? (mod n 2))
    (/ n 2)
    (/ (- n 1) 2)))

(defn half-of-odd [n]
  (let [h (half-of n)]
    (if (even? h) (inc h) h)))

(defn prime? [n]
  (if (or (= 1 n) (= 2 n))
    true
    (if (even? n)
      false
      (loop [d (half-of-odd n)]
        (if (= d 1)
          true
          (if (= 0 (mod n d))
            false
            (recur (- d 2))))))))

(defn eul3 [number]
  (loop [n (half-of number)]
    (if (and 
         (zero? (mod number n))
         (prime? n))
      n
      (recur (dec n)))))

(eul3 13195) ;; 29
(eul3 600851475143)
