;; Euler #3 doesn't work yet
(defn prime? [n]
  (if (or (= 1 n) (= 2 n))
    true
    (loop [d (- n 1)]
      (if (= d 1)
        true
        (if (= 0 (mod n d))
          false
          (recur (dec d)))))))

(defn eul3 []
     (let [f 600851475143]
       (loop [n (dec f)]
         (if (and 
              (zero? (mod f n))
              (prime? n))
           n
           (recur (dec n))))))

(eul3)
