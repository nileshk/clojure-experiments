(defn eul2 []
  (reduce + (filter #(even? %)
                    (loop [result [2 1]]
                      (let [val1 (first result)
                            val2 (last (take 2 result))
                            valnext (+ val1 val2)]
                        (if (> valnext 4000000)
                          result
                          (recur (cons valnext result))))))))

(eul2)
