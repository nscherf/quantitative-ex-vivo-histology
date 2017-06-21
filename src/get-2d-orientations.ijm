function getOrientation(input, output, filename){
	open(input+filename+".tif");
	run("Directionality", "method=[Local gradient orientation] nbins=45 histogram=-90 display_table");	
	selectWindow("Directionality histograms for "+filename+" (using Local gradient orientation)");	
	saveAs("Results", output+filename+"-.csv");
	run("Close");
}

input = getDirectory("Choose an input Directory ");
output = getDirectory("Choose an output Directory ");


list = getFileList(input);
for (i = 0; i < list.length; i++)
        getOrientation(input, output, substring(list[i],0,lengthOf(list[i])-4));
setBatchMode(false);



