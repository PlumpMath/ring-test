(ns ring-test.core
  (:use ring.adapter.jetty)
  (:use ring.util.response)
  (:use ring.middleware.resource)
  (:use ring.middleware.params)
  (:use ring.middleware.file-info)
  (:use clojure.pprint)
  (:use ring.middleware.stacktrace))

(defn handler [request]
  (-> (response "Hello World! :)")
      (content-type "text/plain")))

(defn wrap-content-type [handler content-type]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Content-Type"] content-type))))

(defn wrap-spy [handler]
  (fn [request]
    (println "------")
    (println "Incoming request:")
    (pprint request)
    (let [response (handler request)]
      (println "Response map:")
      (pprint response)
      (println "-----")
      response)))

(def app
  (-> #'handler
      (wrap-params)
      (wrap-resource "public")
      (wrap-file-info)
      (wrap-stacktrace)
      (wrap-spy)));needs to wrap around wrap-resource or wrap-file

(defonce server (run-jetty #'app {:port 3000 :join? false}))

;http://stackoverflow.com/questions/2706044/how-do-i-stop-jetty-server-in-clojure
;REPL usage
;(use 'ring.adapter.jetty)
;Evaluate the functions
;(.start server)
;(.stop server)
;re-evaluate changed handler/app
;http://mmcgrana.github.com/ring/ring.util.response.html
;