(ns lis.controller.decl
  (:require [lis.module.mongo :as module]
            [lis.controller.rate :as rate]))

(defn create [decl]
  (let [doc (assoc decl
              :taxes (taxes/sum (:taxBasis decl) 
                                  (get-taxes-related decl)
                                  (db/query "rate"))
              :taxStatus (get-status decl)
              :taxesDetail (get-taxes-detail decl))]
    (db/create "decls" doc)))




;; (defn- get-status [decl]
;;   (if (= (:occurDate decl) (:declDate decl)) "申报" "发生"))

;; (defn delete [id]
;;   (db/delete "decls" id)
;;   {:status 204})



;; (defn query
;;   ([] (db/query "decls"))
;;   ([id] (db/query "decls" id)))



;; (defn get-taxes-detail-by-id [id]
;;   (let [decl (query id)]
;;     (taxes/query-taxes  (:taxBasis decl)
;;                          (get-taxes-related decl)
;;                          (db/query "rate"))))

;; (defn get-taxes-detail [decl]
;;   (get-taxes-detail-by-id (:_id decl)))





;; (defn update [id decl]
;;   (db/update "decls" id 
;;              (assoc (dissoc decl :_id)
;;                :taxes 
;;                (taxes/sum (:taxBasis decl) 
;;                           (get-taxes-related decl)
;;                           (db/query "rate"))))
;;   (db/query "decls" id))

