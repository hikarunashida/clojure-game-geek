(ns clojure-game-geek.pedestal
  (:require [integrant.core :as ig]
            [clojure.walk :as walk]
            [com.walmartlabs.lacinia :as lacinia]
            [com.walmartlabs.lacinia.pedestal :as pedestal]
            [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [io.pedestal.http.cors :refer [allow-origin]])
  (:import (clojure.lang IPersistentMap)))

;; perflight route
(defn- add-perflight-route
  [schema options]
  (let [perflight-route ["/graphql" :options (fn [_] {:status 200}) :route-name ::perflight]]
    (-> schema
        (pedestal/graphql-routes options)
        (conj perflight-route)
        route/expand-routes)))

;; add allow-origin interceptor and perflight route
(defn- intercept-cors-option
  [route]
  (->> {:allowed-origins some?}
       allow-origin
       (partial cons)
       (update-in route [:interceptors])))

(defn- construct-route
  [schema options]
  (->> options
       (add-perflight-route schema)
       (map intercept-cors-option)
       (assoc options :routes)))

(defn- create-server-state
  [schema]
  (->> {:graphiql true} ;; temporary
       (construct-route schema)
       (pedestal/service-map schema)
       http/create-server
       atom))

(defmethod ig/init-key :clojure-game-geek/pedestal [_ {:keys [schema graphiql]}]
  (let [server (create-server-state schema)]
    (http/start @server)
    server))
