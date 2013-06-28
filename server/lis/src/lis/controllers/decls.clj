(ns lis.controllers.decls
  (:require [lis.db.mongo :as db]
            [lis.controllers.taxes :as taxes]
            ))

(defn- get-status [decl]
  (if (= (:occurDate decl) (:declDate decl)) "申报" "发生"))

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
    :tax-object (:taxObject decl)
    :income-tax (if (> (count (:taxpayer decl)) 4)
                  "企业所得税" 
                  "个人所得税")
    :region-type (let [region (:region decl)]
                   (subs region (dec (count region))))
    ))

(defn get-taxes-detail [id]
  (let [decl (query id)]
    (taxes/query-taxes  (:taxBasis decl)
                         (get-taxes-related decl)
                         (db/query "rate"))))

;;to do
;;not thread safe
(defn- get-sn []
  (inc 
   (let [decls (query)]
     (if (not-empty decls) 
       (apply max (map #(:sn %) decls)) 
       0))))

(defn create [decl]
  (let [doc (assoc decl
              :taxes (taxes/sum (:taxBasis decl) 
                                  (get-taxes-related decl)
                                  (db/query "rate"))
              :tstatus (get-status decl)
              :sn (get-sn))]
    (db/create "decls" doc)))

(defn update [id decl]
  (db/update "decls" id 
             (assoc (dissoc decl :_id)
               :taxes 
               (taxes/sum (:taxBasis decl) 
                          (get-taxes-related decl)
                          (db/query "rate"))))
  (db/query "decls" id))

