(ns lis.controllers.levy
  (:require [lis.db.mongo :as db]
            [lis.controllers.decls :as decls]))

(defn create [levy]
  (let [decl (decls/query (:_id levy))]
    (db/update "decls" (:_id levy) (dissoc (merge decl levy) :_id))))

(defn query 
  ([] (assoc {} ))
  )

(def vv (decls/query "51cd45c02736992fbf88929a"))

(select-keys vv [:taxpayer :payDate])
