(ns lis.controllers.regions
  (:require [lis.db.common :as db]))

(defn query []  (db/query "regions"))
  
(defn query [] 
  (-> (db/query "regions")
      (first)
      (:name)
      (seq)))
