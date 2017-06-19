## outline


### hMRI ex vivo samples
- analysis of histology data
- ROI based feature mapping histology - MRI
- manual registration of histology to MRI
- *optional: automated registration + voxel-wise mapping from micro- to macrostructure*
- *optional: implementation of 2D data analysis in Python*

___________

**overview of data**

01 | Image name | Intended purpose | Stain | resolution / pixel size
-- | ---------- | ---------------- | ----- | -----
02 | 7 B r-USS-_2016-10-06 13_29_06.scn | Free iron detection | Modified Perls (dx.doi.org/10.1073/pnas.0911177107) |
03 | ImageCollection_0000079348_2016-08-23 09_28_45.scn | Astrocytes | GFAP |
04 | ImageCollection_0000079349_2016-08-23 16_32_56.scn | Microglia | Iba1 |
05- | ImageCollection_0000079702_2016-08-24 09_21_17.scn | Neuron density |  NeuN |
06- | ImageCollection_0000079703_2016-08-24 09_15_29.scn | Neurofilaments | NF200 |
07 | ImageCollection_0000079704_2016-08-24 09_10_29.scn | Myelin density | SMI-94 |
08 | ImageCollection_0000079705_2016-08-24 09_06_29.scn | Phosphorylated neurofilaments | SMI-31 |
09 | ImageCollection_0000080053_2016-08-26 06_20_48.scn | Neuronal density | SMI-32 |
10 | ImageCollection_0000080054_2016-08-26 06_20_48.scn | Cytochrome oxidase | [http://www.abcam.com/mtco1-antibody-1d6e1a8-ab14705.html] |
11 | ImageCollection_0000082605_2016-09-30 19_12_14.scn | Neuronal density | GAP43 |
12- | ImageCollection_0000082606_2016-09-30 19_06_06.scn | Ferritin iron | Ferritin? |
13 | ImageCollection_0000082828_2016-09-30 23_07_00.scn | Ferritin iron | Ferritin [http://www.abcam.com/ferritin-light-chain-antibody-ab69090.html] |
14- | ImageCollection_0000083223_2016-10-06 13_23_45.scn | Test modified Perls protocol | Modified Perls [dx.doi.org/10.1073/pnas.0911177107] |
15- | ImageCollection_0000083225_2016-10-06 13_30_57.scn | Test Perls protocol | Perls |
16- | QX16-3127 A 1 2_2016-10-07 11_52_46.scn | Test modified Perls protocol | Perls |
MK1 |  I5_BOD_20x.jp2 | myelinated structures | Bodian | 0.39 micrometer sidelength of pixel



-----------


## current workflow
- FIJI/ImageJ tools:
### color processing (optional):
- color deconvolution
	- http://imagej.net/Colour_Deconvolution
	- https://www.fmhs.auckland.ac.nz/assets/fmhs/sms/biru/docs/Using%20Colour%20Deconvolution%20in%20ImageJ.pdf
### Machine Learning based segmentation
- trainable WEKA segmentation: http://imagej.net/Trainable_Weka_Segmentation [^1 [](https://academic.oup.com/bioinformatics/article-abstract/doi/10.1093/bioinformatics/btx180/3092362/Trainable-Weka-Segmentation-a-machine-learning)]
	- use probability maps for downstream steps
- alternative: Ilastik pixel classification workflow [^2 [] (http://ilastik.org/)]

### cytology statistics

#### cell density
- threshold probability maps (t > 0.5)
- A)
	- label connected components
	- split touching cells / nuclei
	- count number of objects 
	- number of objects / area of image
- B) 
	- number of white pixels / number of pixels (total)
#### myelin density
- split image into regions the size of an MR voxel (use macro *split-img-into-MR-voxels-2D.ijm*) 
- threshold probability maps of cell(foreground) class (t>0.5)
- fraction of white pixels / total pixels

#### myelin orientation
- split image into regions the size of an MR voxel (use macro *split-img-into-MR-voxels-2D.ijm*) 
- compute structure tensor at pixel level and export distribution of orientation directions (use macro *get-2d-orientations.ijm*)

#### alternatives:
	- to do statistics on detected cell regions
		- http://imagej.net/MorphoLibJ
	- fibre density and orientation
		- MVEF, Vesselness Filter
			- http://imagej.net/Frangi
			- https://www.longair.net/edinburgh/imagej/tubeness/

- network properties
	- tbd (see work by Karsten Winter)
		

# alternative options to incorporate into workflow

- https://imagej.nih.gov/ij/plugins/ihc-toolbox/index.html
- cell counting
	- cell nuclei detection
		- ITCN
			- https://www.youtube.com/watch?v=PqHFsmS1_JY
		- http://imagej.net/Particle_Analysis
		- http://imagej.net/Nuclei_Watershed_Separation
	- blob detection
		- https://imagej.net/IJ_Blob
	- other options:
		- https://imagej.net/Getting_started_with_TrackMate
		- http://mosaic.mpi-cbg.de/?q=downloads/imageJ
		- https://imagej.net/IJ_Blob
- [tools for digital pathology:](https://qupath.github.io/)

# available scripts and macros
		
- FIJI macros
	- color-deconvolution
		- takes the currently active/open image
		- applies Color Deconvolution
		- saves the resulting 3 images to a folder
	- deconvolove-files-from-dir 
		- opens a directory
		- gets all the file names of the files contained in that folder
		- applies the color-deconvolution macro to each of the images in succession
	- split image into tiles
	- tubeness and Franghi vesselness
	- split image into square regions of size corresponding to MR voxels 
	- run directionality analysis on a folder of images and save orientation distribution as csv files


## necessary preparation: literature, tools:

### reading


#### some tutorials on image analysis

- a nice introduction to digital images and analysis with ImageJ/FIJI
	- https://blogs.qub.ac.uk/ccbg/files/2014/05/2014-05_Analyzing_fluorescence_microscopy_images.pdf
- introductory slides for ImageJ/FIJI
	- https://imagej.github.io/presentations/fiji-introduction/#/23
- some thoughts on images and image analysis
	- http://imagej.net/Principles
- processing the image values
	- http://imagej.net/Image_Intensity_Processing

####  neuro-anatomy:

#### technical papers:

- Arganda-Carreras, Ignacio, Verena Kaynig, Curtis Rueden, Kevin W. Eliceiri, Johannes Schindelin, Albert Cardona, and H. Sebastian Seung. 2017. “Trainable Weka Segmentation: A Machine Learning Tool for Microscopy Pixel Classification.” Bioinformatics , March. doi:10.1093/bioinformatics/btx180.
- Frangi, Alejandro F., Wiro J. Niessen, Koen L. Vincken, and Max A. Viergever. 1998. “Multiscale Vessel Enhancement Filtering.” In Medical Image Computing and Computer-Assisted Intervention — MICCAI’98, edited by William M. Wells, Alan Colchester, and Scott Delp, 130–37. Lecture Notes in Computer Science 1496. Springer Berlin Heidelberg.
- Wilson, Greg, Jennifer Bryan, Karen Cranston, Justin Kitzes, Lex Nederbragt, and Tracy K. Teal. 2016. “Good Enough Practices in Scientific Computing.” arXiv [cs.SE]. arXiv. http://arxiv.org/abs/1609.00037.

## goals
- mapping of brain microstructural features from ex vivo histology to MRI data 
- computational tool that bundles functionality: the extraction of cytological and processual features from 2D histological sections:
	1. collection of FIJI/ImageJ macro/plugin for processing
	2. an Python script or notebook for analysing structural features from probability maps

##potential risks

- extraction of neurites/axons/processes might be complicated using current histological data
	- start simple and then improve
- automated registration could be hard or impossible to achieve

## important aspects
- careful documentation of analysis
- thorough testing

## experiment plan
- neuronal density:
	- use GAP43 staining: 2016-09-30-19-12-14
- fibre density 
	- use SMI-94 stainting: 2016-08-24-09-10-29
- extracting cortical layers
	- (manual) delineation of gray matter/CSF boundary and white matter/gray matter boundary
		- https://github.com/fiji/Live_Wire


## histological staining protocols used

- Neuronal density: NeuN, Nissl, GAP43, SMI-32
- Myelin density: SMI-94
- COx: QSH established antibody detail: http://www.abcam.com/mtco1-antibody-1d6e1a8-ab14705.html
- Neurofilament: NF200, SMI-31 (phosphorylated)
- Astrocytes: GFAP, Iba1 (microglia)
- Tau: AT8 antibody recognises phosphorylated PHF tau, 
- A-beta: A-beta used for plaques in the brain, neurofibrillary tangles and cerebrovascular deposits
- Free iron detection: try using method from http://dx.doi.org/10.1073/pnas.0911177107
- Ferritin: this antibody would be suitable: http://www.abcam.com/ferritin-light-chain-antibody-ab69090.html ,   
