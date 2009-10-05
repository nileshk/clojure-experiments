(import '(javax.swing JFrame JPanel)
        '(java.awt Color Graphics)
        '(java.awt.image BufferedImage))

(def dimensions [500 300])

(let [frame (JFrame.)]
  (doto frame 
    (.add (new JPanel))
    .pack
    (.setSize (dimensions 0) (dimensions 1))
    .show
    (.setDefaultCloseOperation JFrame/EXIT_ON_CLOSE)))
