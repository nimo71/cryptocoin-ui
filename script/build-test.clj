(require 'cljs.build.api)

(cljs.build.api/build "src"
  {:main 'cryptoquants.core
   :output-to "deploy/test/public/js/main.js"
   :output-dir "deploy/test/public/js"})
