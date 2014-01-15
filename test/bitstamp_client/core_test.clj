(ns bitstamp-client.core-test
  (:require [midje.sweet :refer :all] 
            [org.httpkit.client :as http]
            [bitstamp-client.core :as bs]
            [clojure.core.async :as async :refer [chan go >! <! <!!]]
            [org.httpkit.fake :refer [with-fake-http]]))

(def ticker-resp 
  "{\"high\": \"843.60\", \"last\": \"823.19\", \"timestamp\": \"1389664542\", \"bid\": \"820.99\", \"volume\": \"18342.97809951\", \"low\": \"783.11\",
  \"ask\": \"823.18\"}")

(fact "ticker throws exception when resp isn't 200"
  (bs/ticker) => (throws Exception #"Failed")
  (provided 
    (http/get #"ticker") => (atom {:status 404})))

(fact "ticker returns parsed body on 200 resp"
  (keys (bs/ticker)) => (just #{:high :timestamp :last :bid
                                :volume :low :ask})
  (provided
    (http/get #"ticker") => (atom {:status 200
                                   :body ticker-resp})))


(fact "depth throws exception when resp isn't 200"
  (bs/depth) => (throws Exception #"Failed")
  (provided 
    (http/get #"order_book" (as-checker map?)) => (atom {:status 404})))
