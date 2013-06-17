(ns lis.db.common
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(mg/connect!)
(mg/set-db! (mg/get-db "lis"))

(defn query [coll] 
  (map 
   #(assoc % :_id (str (:_id %))) 
   (mc/find-maps coll)))

(defn create [coll doc]
  (mc/insert-and-return coll (assoc doc :_id (ObjectId.))))
