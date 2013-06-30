(ns lis.controller.decl
  (:require [lis.module.mongo :as module]
            [lis.controller.taxes :as taxes]))

(defn create [decl]
  (let [doc (assoc decl
              :taxes (taxes/sum decl)
              :taxStatus (if (= (:occurDate decl) (:declDate decl)) "申报" "发生")
              :taxesDetail (taxes/get-taxes-detail decl))]
    (module/insert "decls" doc)))

(defn delete [id]
  (module/delete "decls" id)
  {:status 204})

(defn query
  ([] (module/query "decls"))
  ([id] (module/query "decls" id)))

(defn update [id doc]
  (module/update "decls" id
                 (assoc doc
                   :taxes (taxes/sum doc)
                   :taxesDetail (taxes/get-taxes-detail doc)))
  (module/query "decls" id))
