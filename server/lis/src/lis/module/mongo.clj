(ns lis.module.mongo
  (:use monger.operators)
  (:require [monger.core :as mg]
            [monger.collection :as mgcoll]))

(mg/connect!)
(mg/set-db! (mg/get-db "lis"))

(defn- get-next-seq [source]
  (mgcoll/find-and-modify 
   "counters" {:_id source} {$inc {:seq 1}} :upsert true))

(defn insert [coll-name doc]
  (mgcoll/insert-and-return 
   coll-name
   (assoc doc :_id (:seq (get-next-seq coll-name)))))

(defmulti multi-query (fn [_ x] (class x)))
(defmethod multi-query Long [coll-name id] 
  (mgcoll/find-map-by-id coll-name id))
(defmethod multi-query String [coll-name id] 
  (mgcoll/find-map-by-id coll-name id))
(defmethod multi-query clojure.lang.PersistentArrayMap [coll-name condition]
  (mgcoll/find-maps coll-name condition))

(defn query
  ([coll-name] (mgcoll/find-maps coll-name))
  ([coll-name id-or-condition] (multi-query coll-name id-or-condition)))


(defn delete [coll id]
  (mgcoll/remove-by-id coll id))

(defn update [coll id doc]
  (mgcoll/update-by-id coll id doc))
