(defproject clojure-game-geek "0.1.0-SNAPSHOT"
  :description "A tiny boardGameGeek clone written in Clojure Lancinia"
  :url "https://github.com/walmartlabs/clojure-game-geek"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.walmartlabs/lacinia-pedestal "0.11.0"]
                 [io.aviso/logging "0.3.2"]
                 [umlaut "0.6.2"]]
  :plugins [[lein-umlaut/lein-umlaut "0.5.3"]]
  :repl-options {:init-ns user})
