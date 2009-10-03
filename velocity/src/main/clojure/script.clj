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

(defn if-not-nil "Returns value if not nil, blank string otherwise" [str]
  (if (nil? str) "" str))
  
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
                     {"name" (.getString rs "COLUMN_NAME")}))))))

(defn get-table-data
  "Get table data"
  [connection schema table]
  (def metaData (. connection getMetaData))
  (let [rs (. metaData getTables nil schema nil (into-array ["TABLE"]))]
      (if (.next rs)
        {"schema" schema
         "name" (.getString rs "TABLE_NAME")
         "tableType" (.getString rs "TABLE_TYPE")
         "remarks" (if-not-nil (.getString rs "REMARKS"))
         "columns" (get-column-data 
                    connection schema table metaData)})))

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

(def all-tables-context
     (let [context (new VelocityContext)]
       (. context put "datetime" (. (new Date) toString))
       (. context put "tableNames" tableNames)
       (. context put "tableData" tableData)
       context))

(defn process-template "Process a Velocity template and output a file"
  [fileMap context]
  (let [templateFileName (:template fileMap)
        outputFileName (:out fileMap)
        template (Velocity/getTemplate templateFileName)
        outputStream (new FileOutputStream outputFileName false)]
    (with-open 
        [writer (new BufferedWriter (new OutputStreamWriter outputStream))]
      (println "Processing template")
      (. template merge context writer)
      (println "Finished"))))

(defn process-template-mappings "Process all given template mappings"
  [template-mappings]
  (println "Processing files...")
  (loop [mappings template-mappings]
    (if (not-empty mappings) 
      (process-template (first mappings) all-tables-context))
    (if (empty? mappings)
      nil
      (recur (rest mappings))))
  (println "Finished processing files..."))

(def file-mappings [ { :template "test.vm" :out "outfile.txt" } ])
(process-template-mappings file-mappings)
