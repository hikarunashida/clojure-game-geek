(ns clojure-game-geek.validators)

(def url?
  (partial re-matches #"https?://[\w/:%#\$&\?\(\)~\.=\+\-]+"))
