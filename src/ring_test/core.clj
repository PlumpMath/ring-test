(ns ring-test.core
  (:use ring.adapter.jetty)
  (:use ring.util.response)
  (:use ring.middleware.resource))

(defn handler [request]
  (-> (response "Hello World")
      (content-type "text/plain")))

(defn wrap-content-type [handler content-type]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Content-Type"] content-type))))

(def app
  (-> handler
      (wrap-content-type "text/plain")
      (wrap-resource "public")))

(defonce server (run-jetty #'app {:port 3000 :join? false}))

;http://stackoverflow.com/questions/2706044/how-do-i-stop-jetty-server-in-clojure
;REPL usage
;(use 'ring.adapter.jetty)
;(use 'ring-test.core)
;(.start server)
;(.stop server)
;http://mmcgrana.github.com/ring/ring.util.response.html
