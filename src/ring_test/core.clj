(ns ring-test.core
  (:use ring.adapter.jetty)
  (:use ring.util.response))

(defn handler [request]
  {:status 200
   :headers {}
   :body "Hello World!"})

(defn wrap-content-type [handler content-type]
  (fn [request]
    (let [response (handler request)]
      (assoc-in response [:headers "Content-Type"] content-type))))

(def app
  (-> handler
      (wrap-content-type "text/plain")))

(defonce server (run-jetty #'app {:port 3000 :join? false}))