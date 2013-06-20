(ns lis.controllers.decls
  (:require [lis.db.common :as db]))

(defn- get-rate [] (db/query "rate"))
(defn get-tax-object []
  (map #(:tax-object %) (get-rate)))

(defn- income-tax-filter [doc]
  (fn [rate-doc]
    (or 
     (and 
      (not= (:class rate-doc) "个人所得税") 
      (not= (:class rate-doc) "企业所得税"))
     (= (:class rate-doc) (:income-tax-type doc))
     )))

(defn- city-tax-filter [doc]
  (fn [append-tax-rate]
    (or 
     (not= (:class append-tax-rate) "城建税")
     (= (:subclass append-tax-rate) (:region-type doc))))) 

(defn query [] (db/query "decls"))
(defn create [doc] (db/create "decls" doc))

