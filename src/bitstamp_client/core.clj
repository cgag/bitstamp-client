(ns bitstamp-client.core
  (:require [org.httpkit.client :as http]
            [clojure.set :refer [rename-keys]]
            [cheshire.core :as json]
            [clojure.core.async :as async :refer [chan go >! <! <!!]]))


;; Public api methods only right now

;; TODO: coercions?  Optional according to dynamic var?
;; numbers to bigdec, trade times to Date (inst)?

(def ticker-uri "https://www.bitstamp.net/api/ticker/")
(def order-books-uri  "https://www.bitstamp.net/api/order_book/")
(def transactions-uri "https://www.bitstamp.net/api/transactions/")
(def eur-usd-conv-uri "https://www.bitstamp.net/api/eur_usd/")

(defn failed-request? [resp]
  (or (:status resp)
      (:error  resp)))

(defn success? [resp]
  (= 200 (:status resp)))

(defn ticker-async []
  (let [p (promise)]
    (http/get ticker-uri 
              (fn [resp] 
                (if (success? resp) 
                  (deliver p (json/parse-string (:body resp) true))
                  (deliver p resp))))
    p))

(defn order-book-async
  "Params: 
  group (optional): true or false
  group orders with the same price 
  Default: true."
  [& [group]]
  (let [p (promise)]
    (http/get order-books-uri 
              {:query-params {:group (if group 1 0)}}
              (fn [resp]
                (if (success? resp)
                  (deliver p (json/parse-string (:body resp) true))
                  (deliver p resp))))
    p))

(defn transactions-async
  "Return all transactions in the last minute or hour according to
  optional param t. Defaults to :hour.

  t can either be :minute or :hour. " 
  [& [t]]
  (let [p (promise)]
    (http/get transactions-uri 
              {:query-params 
               {:time (when t (name t))}}
              (fn [resp]
                (if (success? resp)
                  (deliver p (json/parse-string (:body resp) true))
                  (deliver p resp))))))

(defn eur-usd-conv-rate-async
  "Return map of currenty :buy and :sell conversion rates"
  []
  (let [p (promise)]
    (http/get eur-usd-conv-uri
              (fn [resp]
                (if (success? resp) 
                  (deliver p (json/parse-string (:body resp) true))
                  (deliver p resp))))))

;; TODO: better exceptions, more info.  Use slingshot?  Look at clj-http.
(defn fail-for [api resp]
  (let [{:keys [status error]} resp] 
    (if error
      (throw error) 
      (throw (Exception. (str "Failed request for " api  
                              " with status " status)  )))))

(defn get-sync [name resp-p]
  (let [resp @resp-p]
    (if-not (failed-request? resp) 
      resp 
      (fail-for name resp)))) 

(defn ticker []
  (get-sync "ticker" (ticker-async)))

(defn transactions [& [t]]
  (get-sync "transactions" (transactions-async t)))

(defn order-book [& [group]]
  (get-sync "order-book" (order-book-async group)))

(defn eur-usd-conv-rate []
  (get-sync "eur-usd-conv-rate" (eur-usd-conv-rate-async)))
