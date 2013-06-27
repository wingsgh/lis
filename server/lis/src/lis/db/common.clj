(ns lis.db.common
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import [org.bson.types ObjectId]))

(mg/connect!)
(mg/set-db! (mg/get-db "lis"))

(defn query 
  ([coll] 
     (map 
      #(assoc % :_id (str (:_id %))) 
      (mc/find-maps coll)))
  ([coll id] 
     (let [doc (mc/find-one-as-map coll {:_id (ObjectId. id)})]
       (assoc doc :_id (str (:_id doc))))))

;;to do
;;not thread safe
(defn- get-sn [coll]
  (inc 
   (apply max 
          (or (map #(or (:sn %) 0) (query coll))) '(0))))
(defn- get-sn [coll]
  (inc 
   (let [decls (query coll)]
     (if (not-empty decls) 
       (apply max (map #(:sn %) decls))
       0))))


(defn create [coll doc]
  (mc/insert-and-return 
   coll 
   (assoc doc :_id (ObjectId.)
          :sn (get-sn coll))))

(defn delete [coll id]
  (mc/remove coll {:_id (ObjectId. id)}))

(defn update [coll id doc]
  (mc/update-by-id coll (ObjectId. id) doc))
