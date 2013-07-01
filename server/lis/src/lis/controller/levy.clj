(ns lis.controller.levy
  (:require [lis.module.mongo :as module]))

(defn create [doc]
  (module/update "decls" (:_id doc)
                 (assoc 
                     (merge (module/query "decls" (:_id doc)) doc)
                   :taxStatus "完税"))
  {:status 204})

(defn query 
  ([] (filter #(= (:taxStatus %) "完税") (module/query "decls")))
  ([id] (first (filter #(= (:taxStatus %) "完税") (query)))))

(defn update [id doc]
  (create doc))



