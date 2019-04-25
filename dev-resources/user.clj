(ns user
  (:require [clojure-game-geek.schema :as s]
            [clojure.walk :as walk]
            [com.walmartlabs.lacinia :as lacinia]
            [com.walmartlabs.lacinia.pedestal :as pedestal]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.cors :refer [allow-origin]])
  (:import (clojure.lang IPersistentMap)))

(def schema (s/load-schema))

(defn sanitize-node
  [node]
  (cond
    (instance? IPersistentMap node)
    (into {} node)

    (seq? node)
    (vec node)

    :else
    node))

(defn simplify
  [m]
  (walk/postwalk sanitize-node m))

(defn q
  [query-string]
  (-> (lacinia/execute schema query-string nil nil)
      simplify))

;; perflight route
(defn add-perflight-route
  [options]
  (let [perflight-route ["/graphql" :options (fn [_] {:status 200}) :route-name ::perflight]]
    (-> schema
        (pedestal/graphql-routes options)
        (conj perflight-route)
        route/expand-routes)))

;; add allow-origin interceptor and perflight route
(defn intercept-cors-option
  [route]
  (->> {:allowed-origins some?}
       allow-origin
       (partial cons)
       (update-in route [:interceptors])))

(defn construct-route
  [options]
  (->> options
       add-perflight-route
       (map intercept-cors-option)
       (assoc options :routes)))

(defn start-server
  []
  (let [route (construct-route {:graphiql true})]
    (-> schema
        (pedestal/service-map route)
        http/create-server
        http/start)
    nil))
