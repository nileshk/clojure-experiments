(defn string-halves
  "Splits string in half.  If string is not evenly divisable returns nil."
  [s]
  (let [s-len (.length s)]
    (if (zero? (mod s-len 2))
      (let [middle (/ s-len 2)]
        [(.substring s 0 middle) (.substring s middle s-len)])
      nil)))

(defn string-reverse "Reverse a string" [s]
  (apply str (map str (reverse s))))

(defn palindrome? [n]
  (let [halves (string-halves (str n))]
    (and
     (not (nil? halves))
     (= (halves 0) (string-reverse (halves 1))))))

(defn eul4 []
  (loop [n1 999 n2 999 max 0]
    (let [p (* n1 n2)]
      (if (and (= n1 1) (= n2 1))
        max
        (recur
         (if (= n2 1) (dec n1) n1)
         (if (= n2 1) 999 (dec n2))
         (if (and (palindrome? p) (> p max)) p max))))))

(eul4)
