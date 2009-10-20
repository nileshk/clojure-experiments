(defn eul1 []
  (reduce + (filter #(or 
                      (= 0 (mod % 3))
                      (= 0 (mod % 5)))
                    (range 1 1000))))

(eul1)
