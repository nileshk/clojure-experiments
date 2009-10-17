;; If I need to cheat:
;;(ns com.nileshk.euler
;;  (use [clojure.contrib.math :only (round)]))

(def eul1
     (reduce + (filter #(or 
                         (= 0 (mod % 3))
                         (= 0 (mod % 5)))
                       (range 1 1000))))
(= eul1 233168)

(def eul2
     (reduce + (filter #(even? %)
                       (loop [result [2 1]]
                         (let [val1 (first result)
                               val2 (last (take 2 result))
                               valnext (+ val1 val2)]
                           (if (> valnext 4000000)
                             result
                             (recur (cons valnext result))))))))
(= eul2 4613732)

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

(def eul3
     (let [f 600851475143]
       (loop [n (dec f)]
         (if (and 
              (zero? (mod f n))
              (prime? n))
           n
           (recur (dec n))))))
