(println "Starting Velocity template processor")
(import '(org.apache.velocity Template VelocityContext)
        '(org.apache.velocity.app Velocity)
        '(org.apache.velocity.exception 
          ParseErrorException ResourceNotFoundException))
(import '(java.io 
          BufferedWriter FileOutputStream OutputStream OutputStreamWriter))
(import '(java.util Date))

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