(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:output-to "deploy/live/main.js"
   :optimizations :advanced})

(System/exit 0)
