(ns lis.controller.regions
  (:require [lis.module.mongo :as module]))
  
(defn query []
  (-> (module/query "regions") first :name seq))

(query)
