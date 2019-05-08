(ns clojure-game-geek.resolvers
  (:require [integrant.core :as ig]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.util :as util]
            [com.walmartlabs.lacinia.schema :as schema]
            [clojure.edn :as edn]))

(defn- entity-map
  [data k]
  (reduce #(assoc %1 (:id %2) %2)
          {}
          (get data k)))

(defn- resolve-game-by-id
  [db context args value]
  (let [{:keys [id]} args]
    (-> db
        (entity-map :games)
        (get id))))

(defn- resolve-board-game-designers
  [db context args board-game]
  (let [designers-map (entity-map db :designers)]
    (->> board-game
         :designers
         (map designers-map))))

(defn- resolve-designer-games
  [db context args designer]
  (let [{:keys [id]} designer]
    (->> (entity-map db :games)
         vals
         (filter #(-> % :designers (contains? id))))))

(defmethod ig/init-key :clojure-game-geek/resolvers [_ {:keys [db]}]
  {:query_game-by-id (partial resolve-game-by-id db)
   :BoardGame_designers (partial resolve-board-game-designers db)
   :Designer_games (partial resolve-designer-games db)})
