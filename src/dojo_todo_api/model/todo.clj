(ns dojo-todo-api.model.todo)

(defn create-list [name]
  {:name  name
   :items {}})

(defn create-list-item [name]
  {:name  name
   :done? false})