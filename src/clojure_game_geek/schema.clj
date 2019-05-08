(ns clojure-game-geek.schema
  (:require [clojure.java.io :as io]
            [com.walmartlabs.lacinia.util :as util]
            [com.walmartlabs.lacinia.schema :as schema]
            [clojure.edn :as edn]
            [integrant.core :as ig]))

(def cgg-data
  (-> "cgg-data.edn"
      io/resource
      slurp
      edn/read-string))

(defn resolve-game-by-id
  [games-map context args value]
  (let [{:keys [id]} args]
    (get games-map id)))

(defn resolve-board-game-designers
  [designers-map context args board-game]
  (->> board-game
       :designers
       (map designers-map)))

(defn resolve-designer-games
  [games-map context args designer]
  (let [{:keys [id]} designer]
    (->> games-map
         vals
         (filter #(-> % :designers (contains? id))))))

(defn entity-map
  [data k]
  (reduce #(assoc %1 (:id %2) %2)
          {}
          (get data k)))

(defn resolver-map
  []
  (let [entity-map* (partial entity-map cgg-data)
        games-map (entity-map* :games)
        designers-map (entity-map* :designers)]
    {:query_game-by-id (partial resolve-game-by-id games-map)
     :BoardGame_designers (partial resolve-board-game-designers designers-map)
     :Designer_games (partial resolve-designer-games games-map)}))

(defn load-schema
  []
  (-> "cgg-schema.edn"
      io/resource
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map))
      schema/compile))

;;;

(defmethod ig/init-key :clojure-game-geek/schema [_ {:keys [schema-path resolvers]}]
  (-> schema-path
      io/resource
      slurp
      edn/read-string
      (util/attach-resolvers resolvers)
      schema/compile))
