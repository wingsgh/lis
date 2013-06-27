(ns lis.controllers.decls
  (:use clojure.walk)
  (:require [lis.db.common :as db]
            [lis.controllers.taxes :as taxes]
            ))

(defn get-status [decl]
  (if (= (decl "occurDate") (decl "declDate")) "申报" "发生"))


(defn delete [id]
  (db/delete "decls" id)
  {:status 204})

(defn get-tax-objects []
  (map #(:tax-object %) 
       (db/query "rate")))

(defn query
  ([] (db/query "decls"))
  ([id] (db/query "decls" id)))

(defn- get-taxes-related [decl]
  (assoc {} 
    :tax-object (or (decl "taxObject") (:taxObject decl))
    :income-tax (if (> (count (or (:taxpayer decl) (decl "taxpayer"))) 3)
                  "企业所得税" 
                  "个人所得税")
    :region-type (let [region (or (decl "region") (:region decl))]
                   (subs region (dec (count region))))
    ))

(defn get-taxes-detail [id]
  (let [decl (query id)]
    (taxes/query-taxes (or (:taxBasis decl) (decl "taxBasis"))
                         (get-taxes-related decl)
                         (db/query "rate"))))

(defn create [decl]
  (let [doc (assoc decl
              :taxes (taxes/sum (decl "taxBasis") 
                                  (get-taxes-related decl)
                                  (db/query "rate"))
              :tstatus (get-status decl))]
    (db/create "decls" doc)))

(defn update [id decl]
  (db/update "decls" id 
             (let [new (keywordize-keys decl)]
               (assoc  (dissoc new :_id)
                 :taxes 
                 (taxes/sum (:taxBasis new) 
                            (get-taxes-related new)
                            (db/query "rate")))
               ))
  (db/query "decls" id))

