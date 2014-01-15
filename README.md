# bitstamp-client

Simple client I made for personal use but might be useful for other.  Has synchronous functions for each api all that either returned
the a parsed json map from the api or throw exceptions.  Has async 
versions of each function that return a promise, which will either contain the parsed
json map from the api or a raw response map from http-kit, which will either have a :status
other than 200, or its :error key will map to an exception.  You might find the `failed-request?`
and `fail-for` functions helpful for handling error cases when using the async api. 

## Usage

Clojars: 

;; get current ticker
(defn ticker [] ...)

;; get all transactions in the last minute or hour. Defaults to hour, pass in :minute if you want in the last minute.
(defn transactions [& [time]] ...)

;; get current order book, by default order asks/bids for the same price are grouped.  During my testing I never actually hit any open orders that had the same price so I don't know what this looks like, you probably want to pass this `false` to turn off grouping.
(defn order-book [& [group]] ...)

;; gets current eur to usd converstion rate in teh form of
;; {:buy "buy-price", :sell "sell-price"}
(defn eur-usd-conv-rate [] ...)

All these have corresponding methods with -async attached to the name
that return promises. See the first section.

## License

Copyright © 2014 Curits Gagliardi

Distributed under the Apache 2.0 license. See LICENSE file for full license.
