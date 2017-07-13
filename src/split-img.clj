(import '(ij.io DirectoryChooser))
(import '(java.io File))
(import '(java.io FileWriter))
(import '(ij.plugin FolderOpener))
(import '(ij.IJ))
(import '(ij.io FileSaver))
(import '(ij ImagePlus))
(import '(ij.plugin Duplicator))
(import '(ij.process ImageProcessor))

(defn get-pixel-width 
	"Extract actual pixel width (Assuming height is the same for now)."
	[imp]
	(let [
	cal (.getCalibration imp)
	 ]
	 (.pixelWidth cal))
)
;;(print (get-pixel-width (ij.WindowManager/getCurrentImage)))

(defn get-output-dir 
	"Ask for output directory."
	[]
	(let [
		dc (new DirectoryChooser "Select folder to save tiles...")
		]
	(.getDirectory dc))
)
;;(print (get-output-dir))
 
(defn get-tile-width 
	"Ask for desired width of tiles."
	[]
	(let [input-dialog (new ij.gui.GenericDialog "Set tiling parameters")]
	(.addNumericField input-dialog "width: " 0  0)
	(.showDialog input-dialog)
	(when-not (.wasCanceled input-dialog)
		(.getNextNumber input-dialog)
		;;(IJ/doCommand (.getNextNumber input-dialog))
	)
))
;;(print (get-tile-width))


(defn create-tile 
	"Create new voxel-sized image"
	[imp n]	
	(ImagePlus. "voxel-sized-tile" (.. (.getProcessor imp) (createProcessor n n))))


;;utility functions from Kyle's funimage lib https://github.com/funimage/funimage/blob/master/src/funimage/imp.clj
(defn save-imp-as-tiff
  "Save an image as a tiff."
  [imp filename]
  (IJ/saveAsTiff ^ImagePlus imp ^string filename)
  imp)

(defn get-pixel-unsafe
  "Get pixel without bounds checking (faster)"
  [^ImagePlus imp x y]
  (.get ^ImageProcessor (.getProcessor imp) x y))

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

(let [
	imp (ij.WindowManager/getCurrentImage)
	pixel-width (get-pixel-width imp)
	tile-width (get-tile-width)
	tile-width-pixels (int (/ tile-width pixel-width))
	output-dir (get-output-dir)
	img-width (/ (.getWidth imp) pixel-width)
	img-height (/ (.getHeight imp) pixel-width)
	nx (int (Math/floor (/ img-width tile-width-pixels)))
	ny (int (Math/floor (/ img-height tile-width-pixels)))
	]
	(print (str nx ", " ny))
	(doseq [x (range nx) 
			y (range ny)]
		    (let [imp-tmp (create-tile imp tile-width-pixels)]
		    	(print (str x ", " y "\n"))
		    	(.show imp-tmp)
		    	(.setRoi imp (* x tile-width-pixels) (* y tile-width-pixels) tile-width-pixels tile-width-pixels)
		    	(.copy imp false)
		    	(.paste imp-tmp)
		    	(Thread/sleep 1000) 
		    	;;(.updateAndRepaintWindow imp-tmp)
		    	(IJ/saveAsTiff imp-tmp (str output-dir "test-" x "-" y ".tif"))
		    	(.close imp-tmp)
		    )
	)
)

