(ns dojo-todo-api.core
  (:use clojure.pprint)
  (:require [dojo-todo-api.db :as db]
            [dojo-todo-api.controller.todo-list :as controller.todo-list]
            [dojo-todo-api.model.todo :as model.todo]))


(defn create-id [id-type]
  (str (gensym id-type)))

;(pprint db/fake-database)

;
;(pprint (model.todo/create-list "Stack Nubank"))
;(pprint (model.todo/create-list-item "Dojo Flutter"))

;(def lista
;  (let [list-1 (create-id "list") list-2 (create-id "list")]
;    {list-1 (assoc (model.todo/create-list "Stack Nubank")
;              :items {"id-item-1" (model.todo/create-list-item "Dojo Flutter")
;                      "id-item-2" (model.todo/create-list-item "Dojo Clojure")})
;     list-2 (model.todo/create-list "Performance Cycle")})
;  )


(defn adiciona-lista [name]
  (assoc db/fake-database (create-id "list") (model.todo/create-list name)))


(defn adiciona-item [list-id name]
  (if (contains? db/fake-database list-id)
    (assoc-in db/fake-database [list-id :items (create-id "item")] (model.todo/create-list-item name))
    ))


(pprint (adiciona-item "id-list-2" "Datomic"))
(pprint db/fake-database)
