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

(defn get-table-names
  "Get table names"
  [connection schema]
  (def metaData (. connection getMetaData))
  (let [rs (. metaData getTables nil schema nil (into-array ["TABLE"]))]
    (loop [result []]
      (if (not (.next rs))
        result
        (recur (conj result (.getString rs "TABLE_NAME")))))))

(defn get-column-data
  "Get table column data"
  [connection schema table metaData]
  (let [rs (. metaData getColumns nil schema table nil)]
    (loop [result []]
      (if (not (.next rs))
        result
        (recur (conj result
                     {:name (.getString rs "TABLE_NAME")}))))))

(defn get-table-data
  "Get table data"
  [connection schema table]
  (def metaData (. connection getMetaData))
  (let [rs (. metaData getTables nil schema nil (into-array ["TABLE"]))]
    (loop [result []]
      (if (not (.next rs))
        result
        (recur (conj result
                     {:schema schema
                      :name (.getString rs "TABLE_NAME")
                      :tableType (.getString rs "TABLE_TYPE")
                      :remarks (.getString rs "REMARKS")
                      :columns (get-column-data 
                                connection schema table metaData)}))))))

(def tableNames 
     (let [connection (database-connect)]
       (get-table-names connection "public")))

(def tableData 
     (let [connection (database-connect)
           schema "public"]
       (loop [result []
              tn (get-table-names connection schema)]
         (if (empty? tn)
           result
           (recur (conj result (get-table-data connection schema (first tn)))
                  (rest tn))))))

(def outputFileName "outfile.txt")
(def templateFileName "test.vm")
(def context (new VelocityContext))
(def template (Velocity/getTemplate templateFileName))
(def outputStream (new FileOutputStream outputFileName false))
(def writer (new BufferedWriter (new OutputStreamWriter outputStream)))

; Setup context
(. context put "datetime" (. (new Date) toString))
(. context put "tableNames" tableNames)
(. context put "tableData" tableData)

(println "Processing template")
(. template merge context writer)
(. writer flush)
(. writer close)
(println "Finished")