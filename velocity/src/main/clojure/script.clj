(println "Starting Velocity template processor")
(import '(org.apache.velocity Template VelocityContext)
        '(org.apache.velocity.app Velocity)
        '(org.apache.velocity.exception 
          ParseErrorException ResourceNotFoundException))
(import '(java.io 
          BufferedWriter FileOutputStream OutputStream OutputStreamWriter))
(import '(java.util Date))
(import '(java.sql
          Connection DatabaseMetaData ResultSet DriverManager))

(defn database-connect "Connect to database" []
  (Class/forName "org.postgresql.Driver")
  (DriverManager/getConnection "jdbc:postgresql://localhost/todo" "nil" "nil"))

(defn table-metadata
  "Get database table metadata"
  [connection schema table]
  (def metaData (. connection getMetaData))
  (def rs (. metaData getTables nil schema table 
             (to-array ["TABLE"]))))

(defn table-names
  "Get table names"
  [connection schema]
  (def metaData (. connection getMetaData))
  (let [rs (. metaData getTables nil schema nil (into-array ["TABLE"]))]
    (loop []
      (when (.next rs)
        ;; TODO return a sequence instead of printing
        (println (.getString rs "TABLE_NAME"))
        (recur)))))

(let [connection (database-connect)]
  (table-names connection "public"))

(def outputFileName "outfile.txt")
(def templateFileName "test.vm")
(def context (new VelocityContext))
(def template (Velocity/getTemplate templateFileName))
(def outputStream (new FileOutputStream outputFileName false))
(def writer (new BufferedWriter (new OutputStreamWriter outputStream)))

; Setup context
(. context put "datetime" (. (new Date) toString))

(println "Processing template")
(. template merge context writer)
(. writer flush)
(. writer close)
(println "Finished")