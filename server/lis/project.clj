(defproject lis "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [com.novemberain/monger "1.5.0"]
                 [ring/ring-json "0.2.0"]
                 [cheshire "5.2.0"]]
  :plugins [[lein-ring "0.8.5"]]
  :ring {:handler lis.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.5"]]}})
