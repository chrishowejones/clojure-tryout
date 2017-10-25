(defproject try-clojure "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [funcool/cats "1.2.1"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]

                 [org.clojure/tools.logging "0.4.0"]]
  :plugins [[cider/cider-nrepl "0.14.0"]]
  :main ^:skip-aot clojure-tryout.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.6.3"]]}})
