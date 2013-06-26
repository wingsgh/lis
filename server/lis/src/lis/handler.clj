(ns lis.handler
  (:use [compojure.core]
        [cheshire.core])
  (:require [compojure.handler :as handler]
            [ring.middleware.json :as middleware]
            [compojure.route :as route]
            [lis.controllers.decls :as decls]
            [lis.controllers.regions :as regions]))

(decls/query "51c948042736c67391653e35")

(defroutes app-routes

  (context "/decls" [] 
           (defroutes decls-routes
             (GET "/" [] (decls/query))
             (POST "/" {body :body} (decls/create body))
             (context "/:id" [id] 
                      (defroutes decls-routes
                        (GET "/" [] {:body (decls/query id)})
                        (DELETE "/" [] (decls/delete id))))))
  
  (context "/regions" [] 
           (defroutes regions-routes
             (GET "/" [] (regions/query))))

  (context "/tax-objects" []
           (defroutes tax-object-routes
             (GET "/" [] (decls/get-tax-objects))))
  )
  

(def app
  (-> 
   (handler/api app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-response)
   ))
