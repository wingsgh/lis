(ns lis.handler
  (:use [compojure.core]
        [cheshire.core])
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [lis.controllers.decls :as decls]
            [lis.controllers.regions :as regions]))

(defroutes app-routes
  ;; (context "/decls" []
  ;;          (defroutes regions-routes
  ;;            (GET "/" [] (regions/query))))
  (context "/decls" [] 
           (defroutes decls-routes
             (GET "/" [] (decls/query))
             (POST "/" {body :body} (decls/create body))))

  (context "/regions" [] 
           (defroutes regions-routes
             (GET "/" [] (regions/query))))
  )
  

(def app
  (-> 
   (handler/api app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-response)
   ))
