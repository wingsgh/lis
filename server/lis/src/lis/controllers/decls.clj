(ns lis.controllers.decls
  (:require [lis.db.common :as db]))

(defn- get-rate-doc [] (db/query "rate"))

(defn- income-tax-filter [doc]
  (fn [rate-doc]
    (or 
     (and 
      (not= (:class rate-doc) "个人所得税") 
      (not= (:class rate-doc) "企业所得税"))
     (= (:class rate-doc) (:income-tax-type doc)))))

(defn- city-tax-filter [doc]
  (fn [append-tax-rate]
    (or 
     (not= (:class append-tax-rate) "城建税")
     (= (:subclass append-tax-rate) (:region-type doc))))) 

(defn- get-rate-by-tax-object [doc]
  (:rate 
   (first 
    (filter
     #(= (:tax-object %) (:tax-object doc))
    (get-rate-doc)))))

(def doc {:tax-object "建筑业－建筑" ,
          :income-tax-type "个人所得税",
          :region-type "镇"})

(map #(if (:append-tax %)
        (update-in % [:append-tax]
                   (fn [x] (filter (city-tax-filter doc) x))) %) 
     (get-rate-by-tax-object doc))

(defn get-tax-object []
  (map #(:tax-object %) (get-rate-doc)))



(defn query [] (db/query "decls"))
(defn create [doc] (db/create "decls" doc))







