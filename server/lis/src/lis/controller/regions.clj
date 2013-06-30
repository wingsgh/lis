(ns lis.controller.regions
  (:require [lis.module.mongo :as module]))
  
(defn query []
  (-> (module/query "regions" "威信县") :name seq))
