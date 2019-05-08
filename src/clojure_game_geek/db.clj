(ns clojure-game-geek.db
  (:require [clojure.java.io :as io]
            [integrant.core :as ig]
            [clojure.edn :as edn]))

(defmethod ig/init-key :clojure-game-geek/db [_ {:keys [initial-data-path]}]
  (-> initial-data-path
      io/resource
      slurp
      edn/read-string))
