(require '[figwheel-sidecar.repl :as r]
         '[figwheel-sidecar.repl-api :as ra])

(ra/start-figwheel!
 {:figwheel-options {}
  :build-ids ["dev"]
  :all-builds
  [{:id "dev"
    :figwheel {:websocket-host "127.0.0.1"}
    :repl false
    :source-paths ["src"]
    :compiler {:main 'cryptoquants.core
               :asset-path "js"
               :output-to "resources/public/js/main.js"
               :output-dir "resources/public/js"
               :externs ["autobahnjs.js"]
               :verbose true}}]})

(ra/cljs-repl "dev")
