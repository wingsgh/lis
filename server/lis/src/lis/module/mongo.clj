(ns lis.module.mongo
  (:use monger.operators)
  (:require [monger.core :as mg]
            [monger.collection :as mgcoll]))

(mg/connect!)
(mg/set-db! (mg/get-db "lis"))

(defn- get-next-seq [source]
  (mgcoll/find-and-modify 
   "counters" {:_id source} {$inc {:seq 1}} :upsert true))

(defn insert [coll doc]
  (mgcoll/insert-and-return 
   coll 
   (assoc doc :_id (:seq (get-next-seq coll)))))

(defn query 
  ([coll] (mgcoll/find-maps coll))
  ([coll id] (mgcoll/find-map-by-id coll id)))

(defn delete [coll id]
  (mgcoll/remove-by-id coll id))

(defn update [coll id doc]
  (mgcoll/update-by-id coll id doc))


