(defn squared [x] (* x x))

(defn abs [x] (if (>= x 0) x (- 0 x)))

(defn eul6 [last]
  (let [end-range (+ last 1)
        r (range 1 end-range)]
    (abs (- (squared (reduce + r))
            (reduce + (map #(squared %) r))))))

(eul6 100)