;; Euler #3 doesn't work yet
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

(defn next-prime-factor [n]
  (loop [index 0]
    (if (= index primes-length)
      nil
      (let [prime (primes index)]
        (if (zero? (mod n prime))
          prime
          (recur (inc index)))))))

(defn eul3 [number]
  (loop [n number max-factor 0]
    (let [prime-factor (next-prime-factor n)]
      (if (nil? prime-factor)
        max-factor
        (recur (/ number prime-factor)
               (max prime-factor max-factor))))))

(eul3 13195) ;; 29
(eul3 600851475143)
