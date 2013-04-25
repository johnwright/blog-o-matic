(ns blog-o-matic.core-test
  (:use [blog-o-matic.core]
        [clojure.test]))

(deftest builds-frequency-map
  (is (= {"It"    {"was" 1}
          "was"   {"the" 2}
          "the"   {"best" 1, "worst" 1}
          "best"  {"of" 1}
          "of"    {"times" 2}
          "times" {"," 1, "." 1}
          ","     {"it" 1}
          "it"    {"was" 1}
          "worst" {"of" 1}})
      (build-frequency-map ["It" "was" "the" "best" "of" "times" ","
                            "it" "was" "the" "worst" "of" "times" "."])))
