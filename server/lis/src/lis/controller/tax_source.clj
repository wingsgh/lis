(ns lis.controller.tax-source
  (:require [lis.module.mongo :as module]))

(defn query
  ([] (module/query "taxSource"))
  ([condition] (module/query "taxSource" condition)))

(defn create [doc]
  (module/insert "taxSource" doc))

(defn delete [id]
  (module/delete "taxSource" id)
  {:status 204})

(defn update [id doc]
  (module/update "taxSource" id doc)
  {:status 204})
