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

(def primes-length 2000)

; Vector of 2000 primes
(def primes (vec (take primes-length (filter prime? (iterate inc 1)))))

(defn eul3 [number]
  (loop [index 0 max-factor 0]
    (if (= index primes-length)
      max-factor
      (let [prime (primes index)]
        (recur (inc index)
               (if (zero? (mod number prime))
                 (max prime max-factor)
                 max-factor))))))

;(eul3 13195) ;; 29
(eul3 600851475143)
