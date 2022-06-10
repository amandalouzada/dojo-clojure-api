(ns dojo-todo-api.db)

(def fake-database
  {"id-list-1" {:title "Stack Nubank"
                :items {"id-item-1" {:title "Flutter" :done? false}
                        "id-item-2" {:title "Clojure" :done? false}
                        "id-item-3" {:title "Datomic" :done? false}}}
   "id-list-2" {:title "Performance Cycle"
                :items {"id-item-1" {:title "Feedback Qulture Rocks 🤟" :done? false}
                        "id-item-2" {:title "Self Assessment 🤯" :done? false}}}})
