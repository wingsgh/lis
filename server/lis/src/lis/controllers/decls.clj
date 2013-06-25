(ns lis.controllers.decls
  (:require [lis.db.common :as db]
            [lis.controllers.taxes :as taxes]))

(defn get-tax-objects []
  (map #(:tax-object %) 
       (db/query "rate")))

;; (defn query []
;;   (let [decls (db/query "decls")]
;;     (map #(assoc %
;;            :taxes (taxes/round2 (:taxes %))) 
;;          decls)))
(defn query []
  (db/query "decls"))


(defn- get-taxes-related [decl]
  (assoc {} 
    :tax-object (decl "taxObject")
    :income-tax (if (> (count (:taxpayer decl)) 3)
                  "企业所得税" 
                  "个人所得税")
    :region-type (let [region (decl "region")]
                   (subs region (dec (count region))))
    ))


(defn create [decl]
  (let [doc (assoc decl
              :taxes (taxes/sum (decl "taxBasis") 
                                  (get-taxes-related decl)
                                  (db/query "rate")))]
    (db/create "decls" doc)))


