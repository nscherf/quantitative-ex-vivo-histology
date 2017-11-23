(import '(ij.io DirectoryChooser))
(import '(java.io File))
(import '(java.io FileWriter))
(import '(ij.plugin FolderOpener))
(import '(ij.IJ))
(import '(ij.io FileSaver))
(import '(ij ImagePlus))
(import '(ij.plugin Duplicator))
(import '(ij.process ImageProcessor))
(import '(ij.measure Calibration))
(import '(util.opencsv CSVWriter))

(import '(fiji.analyze.directionality Directionality_))

(defn get-pixel-width
  "Extract actual pixel width (Assuming height is the same for now)."
  [imp]
  (let [
        cal (.getCalibration imp)]

   (.pixelWidth cal)))

;;(print (get-pixel-width (ij.WindowManager/getCurrentImage)))

(defn get-output-dir
  "Ask for output directory."
  []
  (let [
        dc (new DirectoryChooser "Select folder to save tiles...")]

   (.getDirectory dc)))

;;(print (get-output-dir))

(defn get-tile-width
  "Ask for desired width of tiles."
  []
  (let [input-dialog (new ij.gui.GenericDialog "Set tiling parameters")]
   (.addNumericField input-dialog "width: " 100  0)
   (.showDialog input-dialog)
   (when-not (.wasCanceled input-dialog)
     (.getNextNumber input-dialog))))
    ;;(IJ/doCommand (.getNextNumber input-dialog))


;;(print (get-tile-width))


(defn create-tile
  "Create new voxel-sized image"
  [imp n]
  (ImagePlus. "voxel-sized-tile" (.. (.getProcessor imp) (createProcessor n n))))


;;utility functions from Kyle's funimage lib https://github.com/funimage/funimage/blob/master/src/funimage/imp.clj


(defn get-pixel-unsafe
  "Get pixel without bounds checking (faster)"
  [^ImagePlus imp x y]
  (.getf ^ImageProcessor (.getProcessor imp) x y))

(defn put-pixel-int-unsafe
  "Put a pixel value at x,y of an imageplus without bounds checking."
  [^ImagePlus imp x y val]
  (.set ^ImageProcessor (.getProcessor imp) x y val)
  imp)

(defn put-pixel-double-unsafe
  "Put a pixel value at x,y of the imageplus."
  [^ImagePlus imp x y val]
  (.setf ^ImageProcessor (.getProcessor imp) x y val)
  imp)

(defn get-calibration
  "Return the calibration of an imageplus."
  [^ImagePlus imp]
  ^ij.measure.Calibration (.getCalibration imp))

(defn set-calibration
 "Set the calibration of an imageplus."
 [^ImagePlus imp ^Calibration cal]
 (.setCalibration imp cal)
 imp)

(defn copy-calibration
 "Copy the calibration from imp-a into imp-b"
 [^ij.ImagePlus imp-a ^ij.ImagePlus imp-b]
 (set-calibration imp-b (get-calibration imp-a))
 imp-b)

(comment
 (let [
       imp (ij.WindowManager/getCurrentImage)
       pixel-width (get-pixel-width imp)
       tile-width (get-tile-width)
       tile-width-pixels (int (/ tile-width pixel-width))
       output-dir (get-output-dir)
       img-width (.getWidth imp)
       img-height (.getHeight imp)
       nx (int (Math/floor (/ img-width tile-width-pixels)))
       ny (int (Math/floor (/ img-height tile-width-pixels)))]

   (print (str nx ", " ny))
   (doseq [x (range nx)
           y (range ny)]
         (let [imp-tmp (create-tile imp tile-width-pixels)]
           (set-calibration imp-tmp (get-calibration imp))
           (print (str x ", " y "\n"))
           (.show imp-tmp)
           (.setRoi imp (* x tile-width-pixels) (* y tile-width-pixels) tile-width-pixels tile-width-pixels)
           (.copy imp false)
           (.paste imp-tmp)
           (Thread/sleep 1000)
          ;;(.updateAndRepaintWindow imp-tmp)
           (.saveAsTiff (ij.io.FileSaver. imp-tmp) (str output-dir "test-" x "-" y ".tif"))
          ;(.saveAsTiff imp-tmp (str output-dir "test-" x "-" y ".tif"))
           (.close imp-tmp)))))





(defn analyze-directionality [imp x y output-dir]
  (let [
        dir (new Directionality_)]

   (.setImagePlus dir imp)
   (.setBinNumber dir 90)
   (.setBinStart dir -90)
   (.setMethod dir fiji.analyze.directionality.Directionality_$AnalysisMethod/FOURIER_COMPONENTS)
   (.computeHistograms dir)

   (let
     [res (vec (.get (.getHistograms dir) 0))
      writer (new CSVWriter (new FileWriter (str output-dir "directionality-histogram-" (format "%05d" x) "-" (format "%05d" y)  ".csv")))]

     (.writeNext writer (into-array String (map str res)))
     (.close writer))))




(defn analyze-major-direction [imp x y output-dir]
  (let [
        dir (new Directionality_)]

   (.setImagePlus dir imp)
   (.setBinNumber dir 90)
   (.setBinStart dir -90)
   (.setMethod dir fiji.analyze.directionality.Directionality_$AnalysisMethod/FOURIER_COMPONENTS)
   (.computeHistograms dir)
   (.fitHistograms dir)

   (let
     [res (vec (.get (.getFitAnalysis dir) 0))
      writer (new CSVWriter (new FileWriter (str output-dir "directionality-" (format "%05d" x) "-" (format "%05d" y)  ".csv")))]

     (.writeNext writer (into-array String (map str res)))
     (.close writer))))




(let [
      imp (ij.WindowManager/getCurrentImage)
      pixel-width (get-pixel-width imp)
      tile-width (get-tile-width)
      tile-width-pixels (int (/ tile-width pixel-width))
      output-dir (get-output-dir)
      img-width (.getWidth imp)
      img-height (.getHeight imp)
      nx (int (Math/floor (/ img-width tile-width-pixels)))
      ny (int (Math/floor (/ img-height tile-width-pixels)))]

  (print (str nx ", " ny))
  (doseq [x (range nx)
          y (range ny)]
        (let [imp-tmp (create-tile imp tile-width-pixels)]
          (set-calibration imp-tmp (get-calibration imp))
          (print (str x ", " y "\n"))
          (.show imp-tmp)
          (doseq [xx (range tile-width-pixels)
                  yy (range tile-width-pixels)]
               (let [val (get-pixel-unsafe imp (+ (* x tile-width-pixels) xx) (+ (* y tile-width-pixels) yy))]
                   (put-pixel-double-unsafe imp-tmp xx yy  val)))


          ;(analyze-major-direction imp-tmp x y output-dir)
          (analyze-directionality imp-tmp x y output-dir)
          ;(.saveAsTiff imp-tmp (str output-dir "test-" x "-" y ".tif"))
          (.close imp-tmp))))
