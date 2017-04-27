from ij import IJ, ImageStack, ImagePlus
import random
from ij.gui import GenericDialog
from ij.io import DirectoryChooser, FileSaver



imp = IJ.getImage()
stack = imp.getStack()
max_slices = imp.getNSlices()

gd = GenericDialog("extract random subslices from stack")
gd.addNumericField("number of slices to extract: ", 1, 0);
gd.showDialog()

n_slices=int(gd.getNextNumber())

dc = DirectoryChooser("Choose directory to write slices to...")
exp_dir = dc.getDirectory()

print(max_slices)
print(exp_dir)

rand_ids=[random.randint(0, max_slices-1) for k in range(n_slices)]
print(rand_ids)
for id in rand_ids:
	my_slice = stack.getProcessor(id)
	new_imp=ImagePlus("my slice "+str(id) , my_slice)
	new_imp.show()
	fs = FileSaver(new_imp)
	fs.saveAsTiff(exp_dir+"/extracted_slice_"+str(id)+".tif")
	

	

