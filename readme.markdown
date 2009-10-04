Clojure Experiments
===================

These are my experiments with [Clojure](http://clojure.org/).

Each folder at the root level is a project:

`servlet`
---------

* Servlet based on code from [this
blog](http://robert.zubek.net/blog/2008/04/26/clojure-web-server/)
which describes how to run Jetty from Clojure.
* This also includes Maven config for launching a Clojure script.  I'd
like to eventually just use the Maven Jetty plugin instead (which
would likely require compiling Clojure classes and I'm not sure if I
could use the REPL that way).

`velocity`
----------

* This is currently a simple code generation tool which uses
  [Velocity](http://velocity.apache.org/) templates.  The templates
  are passed database metadata.
* The ultimate goal is to have a flexible code generation toolkit that:
  * Has pluggable input sources
  * Has pluggable output sources (e.g. be able to use different
    template libraries)
  * Can be comfortably used from the REPL
