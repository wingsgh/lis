(ns lis.controllers.regions
  (:require [lis.db.mongo :as db]))
  
(defn query []
  (-> (db/query "regions") first :names seq))
