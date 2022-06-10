(ns dojo-todo-api.server
  (:require [dojo-todo-api.diplomat.http-server :as diplomat.http-server]
            [io.pedestal.http :as http]))

(def service-map
  {::http/routes diplomat.http-server/routes
   ::http/type   :jetty
   ::http/port   8890})

(defn start []
  (http/start (http/create-server service-map)))