(import '(org.apache.commons.math.random JDKRandomGenerator))
(import '(org.mortbay.jetty Server))
(import
 '(java.io File)
 '(javax.servlet.http HttpServlet HttpServletRequest HttpServletResponse)
 '(org.mortbay.jetty Server Handler Connector NCSARequestLog)
 '(org.mortbay.jetty.handler HandlerCollection ContextHandlerCollection RequestLogHandler)
 '(org.mortbay.jetty.nio BlockingChannelConnector)
 '(org.mortbay.jetty.servlet Context DefaultServlet ServletHolder SessionHandler))

(println "Starting up app. Here's a number: " (.nextInt (JDKRandomGenerator.)))
;;(new Server)

(defn process [#^HttpServletRequest req #^HttpServletResponse resp]
  (let [out (. resp (getOutputStream))]
    (. out (println (str "Test Successful at " (new java.util.Date)))))) 

;; implementation of an HttpServlet, overriding just one function:
;;   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
(defn make-test-servlet []
     (proxy [HttpServlet] []
       (doGet [#^HttpServletRequest req #^HttpServletResponse resp]
          (process req resp))))

;; server port
(def *port* 8080)

;; static pages will be served from this directory
(def *wwwdir* "/Users/nil/tmp/clojure/www")

;; log files go here
(def *logdir* "/Users/nil/tmp/clojure/log")

;; specifies naming pattern for log files
(def *logfiles* "log.yyyy_mm_dd.txt")

;; create connector on the specified port
(defn make-connectors [port]
  (let [conn (new BlockingChannelConnector)]
    (. conn (setPort port))
    (into-array [conn])))

;; configures a default servlet to serve static pages from the "/" directory
(defn configure-context-handlers [contexts]
  (let [context (new Context contexts "/" (. Context NO_SESSIONS))]
    (. context (setWelcomeFiles (into-array ["index.html"])))
    (. context (setResourceBase (. (new File *wwwdir*) (getPath))))
    (. context (setSessionHandler (new SessionHandler)))
    (. context (addServlet (new ServletHolder (new DefaultServlet)) "/*"))
    (. context (addServlet (new ServletHolder (make-test-servlet)) "/test"))
    context))

;; set up handlers: the context ones, and a logger to track all http requests
(defn make-handlers [contexts]
  (let [handlers (new HandlerCollection)
        logger (new RequestLogHandler)
        logfile (new File *logdir* *logfiles*)]

    (. logger (setRequestLog (new NCSARequestLog (. logfile (getPath)))))
    (. handlers (addHandler logger))
    (. handlers (addHandler contexts))
    handlers))

;; make an empty collection of context handlers - we'll configure it later
(defn make-contexts []
  (new ContextHandlerCollection))

;; make an instance of the http server
(defn make-server
  ([] (make-server *port*))
  ([port]
     (let [contexts (make-contexts)
           connectors (make-connectors port)
           handlers (make-handlers contexts)
           server (new Server)]
       (. server (setConnectors connectors))
       (. server (setHandler handlers))
       (configure-context-handlers contexts)
       server)))

(def server (make-server))
(. server (start))
