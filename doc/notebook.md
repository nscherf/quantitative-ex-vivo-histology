## outline


### hMRI ex vivo samples
- analysis of histology data
- ROI based feature mapping histology - MRI
- manual registration of histology to MRI
- *optional: automated registration + voxel-wise mapping from micro- to macrostructure*
- *optional: implementation of 2D data analysis in JAVA as plugin for FIJI or alternatively in Python*

-----------

**some tutorials on image analysis**

- a nice introduction to digital images and analysis with ImageJ/FIJI
	- https://blogs.qub.ac.uk/ccbg/files/2014/05/2014-05_Analyzing_fluorescence_microscopy_images.pdf
- introductory slides for ImageJ/FIJI
	- https://imagej.github.io/presentations/fiji-introduction/#/23
- some thoughts on images and image analysis
	- http://imagej.net/Principles
- processing the image values
	- http://imagej.net/Image_Intensity_Processing


##ideas
- FIJI/ImageJ tools:
	- https://imagej.nih.gov/ij/plugins/ihc-toolbox/index.html
- color processing:
	- color deconvolution
		- http://imagej.net/Colour_Deconvolution
		- https://www.fmhs.auckland.ac.nz/assets/fmhs/sms/biru/docs/Using%20Colour%20Deconvolution%20in%20ImageJ.pdf
- cytology statistics
	- cell counting
		- cell nuclei detection
			- http://imagej.net/Particle_Analysis
			- http://imagej.net/Nuclei_Watershed_Separation
		- blob detection
			- https://imagej.net/IJ_Blob
		- other options:
			- https://imagej.net/Getting_started_with_TrackMate
			- http://mosaic.mpi-cbg.de/?q=downloads/imageJ
	- fibre density and orientation
		- MVEF, Vesselness Filter
			- http://imagej.net/Frangi
			- https://www.longair.net/edinburgh/imagej/tubeness/
	- network properties
		- ... (see work by Karsten)


##necessary preparation: literature, tools:

###reading

- neuro-anatomy:
	- look up stainings
	- cyto- and myeloarchitecture of V1 / V2
- software tools:
	- Python
	- Jupyter
	- ImageJ / FIJI
- technical papers:
	- MVEF
	- color deconvolution
	- [] cell nuclei detection?
	- [] ask Luke for more...

- canonical papers
	- Good Enough Practices in Scientific Programming
	- 

### software tools:

- FIJI / ImageJ
	- Python 
		- Jupyter / IPython
		- scikit learn
		- skimage
		- OpenCV
		- pandas
		- search for more...
	- Matlab
	- R
	- Git / GitHub

##goals
- mapping of fine-structural features to MRI data based on manually defined ROIs
- simple software tool that bundles functionality: the extraction of cytological and processual features from 2D histological sections:
	1. a Python package
	2. a Python/Jupyter notebook
	3. a FIJI/ImageJ macro/plugin

##potential risks
- too much manual work might feel too boring (!?)
- extraction of cell nuclei might not be straightforward or not accurate enough
	- take care by testing on data with known answer
- extraction of neurites/axons/processes might be complicated (and inaccurate)
	- start simple (low hanging fruits first) and then improve
- automated registration could be hard to achieve

# important aspects
- care about documentation of structure of computational analysis:
	- see GEPfSCP
- test steps of analysis:
	- manually validated data
	- simulated data
	- look for annotated data from Grand Challenges or so...
- 

# work plan
- neuronal density:
	- use GAP43 staining: 2016-09-30-19-12-14
	- open image in ... resolution
	- run spot detection (using TrackMate)
	- export xml (?) result

# run ImageJ on compute servers
- 

# histological staining protocols used

- Neuronal density: NeuN, Nissl, GAP43, SMI-32
- Myelin density: SMI-94
- COx: QSH established antibody detail: http://www.abcam.com/mtco1-antibody-1d6e1a8-ab14705.html
- Neurofilament: NF200, SMI-31 (phosphorylated)
- Astrocytes: GFAP, Iba1 (microglia)
- Tau: AT8 antibody recognises phosphorylated PHF tau, 
- A-beta: A-beta used for plaques in the brain, neurofibrillary tangles and cerebrovascular deposits
- Free iron detection: try using method from http://dx.doi.org/10.1073/pnas.0911177107
- Ferritin: ARL wonders if this antibody would be suitable: http://www.abcam.com/ferritin-light-chain-antibody-ab69090.html , she will ask Sebastian.   
