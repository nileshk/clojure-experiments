(defn eul9 []
  (loop [a 1 b 1]
    (let [c (Math/sqrt (+ (* a a) (* b b)))]
      (if (and (= (Math/ceil c) c)
               (= (+ a b c) 1000))
        (* a b c)
        (recur
         (if (= b 700) (inc a) a)
         (if (= b 700) 1 (inc b)))))))