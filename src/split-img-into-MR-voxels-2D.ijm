len = getNumber("Side length of voxels in microns:", 100); 
dir = getDirectory("Choose an output Directory ");
id = getImageID(); 
title = getTitle(); 
getLocationAndSize(locX, locY, sizeW, sizeH); 
width = getWidth(); 
height = getHeight(); 

getPixelSize(unit, pw, ph, pd);

tileWidth = round(len / pw); 
tileHeight = round(len / ph);

nx = floor(width / tileWidth);
ny = floor(height / tileHeight);


print(tileHeight+" "+tileWidth);
print (ny + " " + nx);

for (y = 0; y < ny+1; y++) { 
	offsetY = y * tileHeight; 
	for (x = 0; x < nx+1; x++) { 
		print("Processing tile x:"+x+" y:"+y+"\n");
		offsetX = x * tileWidth; 
		selectImage(id); 
 		call("ij.gui.ImageWindow.setNextLocation", locX + offsetX, locY + offsetY); 
		tileTitle = title + " [" + x + "," + y + "]"; 
 		run("Duplicate...", "title=" + tileTitle); 
		makeRectangle(offsetX, offsetY, minOf(tileWidth,width-offsetX), minOf(tileHeight,height-offsetY)); 
 		run("Crop");
 		saveAs("Tiff", dir+title + "_" + x + "_" + y);
 		selectImage(getImageID());
 		close();  
		} 
	} 

	
selectImage(id); 
close(); 


