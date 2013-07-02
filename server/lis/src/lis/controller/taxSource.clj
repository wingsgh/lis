(ns lis.controller.taxSource
  (:require [lis.module.mongo :as module]))

(defn query
  ([] (module/query "taxSource"))
  ([id] (module/query "taxSource" id)))

(defn create [building]
  (module/insert "taxSource" (assoc building :industry "建筑业")))

(defn delete [id]
  (module/delete "taxSource" id)
  {:status 204})

(defn update [id doc]
  (module/update "taxSource" id doc)
  {:status 204})


(query)
