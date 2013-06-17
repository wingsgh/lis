(ns lis.controllers.decls
  (:require [lis.db.common :as db]))

(defn query [] (db/query "decls"))
(defn create [doc] (db/create "decls" doc))
