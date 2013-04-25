(ns blog-o-matic.core
  (:require [clojure.string :as str]))

(defn build-frequency-map
  ([tokens] (build-frequency-map {} tokens))
  ([freqs tokens]
     (if (or (empty? tokens)
             (empty? (rest tokens)))
       freqs
       (let [curr-tok      (first tokens)
             next-tok      (first (rest tokens))
             next-tok-map  (get freqs curr-tok {})
             next-tok-freq (inc (get next-tok-map next-tok 0))]
         (recur (assoc freqs
                  curr-tok
                  (assoc next-tok-map next-tok next-tok-freq))
                (rest tokens))))))

(defn random-next-token [token freqs]
  (let [next-tok-map (get freqs token)
        candidates   (mapcat (fn [[next-tok freq]] (replicate freq next-tok))
                             next-tok-map)]
    (if (empty? candidates)
      nil
      (rand-nth candidates))))

(defn random-first-token [freqs]
  (rand-nth (filter #(Character/isUpperCase (first %))
                    (keys freqs))))

(defn random-token-stream
  ([freqs] (random-token-stream freqs (random-first-token freqs)))
  ([freqs token]
     (cons token
           (if-let [next-token (random-next-token token freqs)]
             (lazy-seq (random-token-stream freqs next-token))
             nil))))

(def punctuation ["." "," ";" ":" "!" "?"])

(defn format-tokens
  ([tokens] (format-tokens tokens []))
  ([tokens result]
     (cond
      (empty? tokens)         (apply str result)
      (empty? (rest tokens))  (recur nil (conj result (first tokens)))
      :else
      (let [curr-tok               (first tokens)
            next-tok               (first (rest tokens))
            space-between-tokens?  (not-any? (partial = next-tok)
                                             punctuation)]
        (if space-between-tokens?
          (recur (rest tokens) (conj result curr-tok " "))
          (recur (rest tokens) (conj result curr-tok)))))))

(defn random-sentence-tokens
  ([freqs] (random-sentence-tokens freqs (random-token-stream freqs) []))
  ([freqs tokens result]
     (if (empty? tokens)
       result
       (if (some (partial = (first tokens)) ["." "!" "?"])
         (conj result (first tokens))
         (recur freqs
                (rest tokens)
                (conj result (first tokens)))))))

(defn random-sentences [freqs]
  (repeatedly (fn [] (format-tokens (random-sentence-tokens freqs)))))
