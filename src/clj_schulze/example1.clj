(def candidates1 #{:a :b :c :d})

(def ballots1
  (concat (repeat 8 [:a :c :d :b])
        (repeat 2 [:b :a :d :c])
        (repeat 4 [:c :d :b :a])
        (repeat 4 [:d :b :a :c])
        (repeat 3 [:d :c :b :a])))

(def defeats1 (total-pairwise-defeats
                (validate-and-canonicalize ballots1 candidates1)))
