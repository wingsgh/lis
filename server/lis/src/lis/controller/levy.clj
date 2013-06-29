(ns lis.controllers.levy
  (:require [lis.db.mongo :as db]
            [lis.controllers.decls :as decls]))

(defn create [levy]
  (let [decl (decls/query (:_id levy))]
    (db/update "decls" (:_id levy) 
               (dissoc (assoc (merge decl levy) :tstatus "完税") :_id))))



(defn query
  ([] (map 
       select-levy-keys
       (filter #(= (:tstatus %) "完税") (decls/query)))))

(defn query-by-sn [sn]
  (let [levy 
        (first 
         (filter #(and (not= (:tstatus %) "完税") (= (:sn %) (Long. sn))) (decls/query)))]
    (assoc levy :taxes-detail (decls/get-taxes-detail levy))
    ))



(defn update [levy])

(defn delete [id]
  )

(query-by-sn "11")
