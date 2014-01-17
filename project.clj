(defproject io.curtis/bitstamp-client "0.1.2"
  :description "Client for the public (currently, private in the future) bitstamp api."
  :url "https://github.com/cgag/bitstamp-client"
  :license {:name "Apache license, Version 2.0"
            :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [cheshire "5.3.1"]
                 [http-kit "2.1.16"]
                 [http-kit.fake "0.2.1"]
                 [midje "1.6.0"]])
