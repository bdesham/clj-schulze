(ns clj-schulze.test.core
  (:use [clj-schulze.core] :reload)
  (:use [clojure.test]))

(deftest ballot-validation
         (let [candidates #{:a :b :c :d}]
           (is (false? (valid-ballot? '(:a :b :c :d) candidates)))
           (is (false? (valid-ballot? [:a :b :c :d :c] candidates)))
           (is (false? (valid-ballot? [:a #{:b :c} :d :c] candidates)))
           (is (false? (valid-ballot? [:a :b :c #{:d :c}] candidates)))
           (is (false? (valid-ballot? [:a :b :e :c :d] candidates)))
           (is (false? (valid-ballot? [:a :b #{:c #{:d}}] candidates)))
           (is (false? (valid-ballot? [:a :b :c #{#{}} :d] candidates)))
           (is (false? (valid-ballot? [:a :b #{:c :d :e}] candidates)))
           (is (true? (valid-ballot? [:a :b :c :d] candidates)))
           (is (true? (valid-ballot? [:a :b #{:c :d}] candidates)))
           (is (true? (valid-ballot? [#{:a :b :c :d}] candidates)))
           (is (true? (valid-ballot? [:a #{:b :c} #{} :d] candidates)))))

(deftest candidate-validation
         (is (false? (valid-candidates? '(:a :b :c :d))))
         (is (false? (valid-candidates? #{:a :b :c :d "e"})))
         (is (false? (valid-candidates? #{:a :b :c :d #{:e}})))
         (is (true? (valid-candidates? #{:a :b :c :d}))))

(deftest canonical-ballots
         (is (= (canonical-ballot [:a :b :c :d :e])
                [#{:a} #{:b} #{:c} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} :d :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} #{:d} :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [:a #{:b :c} #{} :d :e])
                [#{:a} #{:c :b} #{:d} #{:e}]))
         (is (= (canonical-ballot [#{:a :b :c :d :e}])
                [#{:a :b :c :d :e}])))

(deftest valid-and-canonical
         (is (= (validate-and-canonicalize
                  [[:a :b :c :d],
                   [:a #{:b :c} :d],
                   [#{:b :e} :c],
                   [:b :a :e]]
                  #{:a :b :c :d :e})
                {[#{:a} #{:b} #{:c} #{:d} #{:e}] 1,
                 [#{:a} #{:c :b} #{:d} #{:e}] 1,
                 [#{:b :e} #{:c} #{:a :d}] 1,
                 [#{:b} #{:a} #{:e} #{:c :d}] 1})))

(deftest wikipedia-example
         (let [candidates #{:a :b :c :d :e},
               ballots (concat (repeat 5 [:a :c :b :e :d])
                                (repeat 5 [:a :d :e :c :b])
                                (repeat 8 [:b :e :d :a :c])
                                (repeat 3 [:c :a :b :e :d])
                                (repeat 7 [:c :a :e :b :d])
                                (repeat 2 [:c :b :a :d :e])
                                (repeat 7 [:d :c :e :b :a])
                                (repeat 8 [:e :b :a :d :c])),
               defeats (total-pairwise-defeats
                         (validate-and-canonicalize ballots candidates)),
               strongest-paths (strongest-paths defeats candidates)]
           (is (= defeats
                  {[:e :d] 31, [:b :c] 16, [:c :b] 29, [:d :e] 14, [:a :c] 26,
                   [:a :b] 20, [:b :d] 33, [:c :d] 17, [:b :e] 18, [:c :e] 24,
                   [:a :d] 30, [:a :e] 22, [:e :a] 23, [:d :a] 15, [:e :c] 21,
                   [:e :b] 27, [:d :c] 28, [:d :b] 12, [:b :a] 25, [:c :a] 19})
               (= strongest-paths
                  {[:e :d] 31, [:b :c] 28, [:c :b] 29, [:d :e] 24, [:a :c] 28,
                   [:a :b] 28, [:b :d] 33, [:c :d] 29, [:b :e] 24, [:c :e] 24,
                   [:a :d] 30, [:a :e] 24, [:e :a] 25, [:d :a] 25, [:e :c] 28,
                   [:e :b] 28, [:d :c] 28, [:d :b] 28, [:b :a] 25, [:c :a] 25}))))

(deftest schulze-example1
         (let [candidates #{:a :b :c :d},
               ballots (concat (repeat 8 [:a :c :d :b])
                               (repeat 2 [:b :a :d :c])
                               (repeat 4 [:c :d :b :a])
                               (repeat 4 [:d :b :a :c])
                               (repeat 3 [:d :c :b :a])),
               defeats (total-pairwise-defeats
                         (validate-and-canonicalize ballots candidates))]
           (is (= defeats
                  {[:b :c] 6, [:c :b] 15, [:a :c] 14, [:a :b] 8, [:b :d] 2,
                   [:c :d] 12, [:a :d] 10, [:d :a] 11, [:d :c] 9, [:d :b] 19,
                   [:b :a] 13, [:c :a] 7}))))
