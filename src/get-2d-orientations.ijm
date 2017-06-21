function getOrientation(input, output, filename){
	open(input+filename+".png");
	run("Directionality", "method=[Local gradient orientation] nbins=45 histogram=-90 display_table");	
	selectWindow("Directionality histograms for "+filename+" (using Local gradient orientation)");	
	saveAs("Results", output+filename+"-.csv");
	close("\\Others");
	close();
	list = getList("window.titles"); 
     for (j=0; j<list.length; j++){ 
     winame = list[j]; 
     	selectWindow(winame); 
     run("Close"); 
     } 
}

input = getDirectory("Choose an input Directory ");
output = getDirectory("Choose an output Directory ");

list = getFileList(input);
nmax = list.length;

n0 = getNumber("start at file #", 1);
ne = getNumber("stop at file #", nmax);

for (i = n0-1; i < ne; i++)
        getOrientation(input, output, substring(list[i],0,lengthOf(list[i])-4));
setBatchMode(false);



