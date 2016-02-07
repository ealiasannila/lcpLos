"""
comparision script:

variables needed:
-polygons
-friciton field
-maxdist
-cellsize
-startpoint
-targetpoints

"""
import sys
import os
import numpy as np

from qgis.core import *
from PyQt4.QtGui import *
from PyQt4.QtCore import QVariant





app = QApplication([])
QgsApplication.setPrefixPath("/usr", True)
QgsApplication.initQgis()

# Prepare processing framework 

sys.path.append('/usr/share/qgis/python/plugins')
from processing.core.Processing import Processing

Processing.initialize()
from processing.tools import *

########DON*T DISTRIBUTE REMOVES EVERTTHING tmp*!!##########
os.system("rm -r tmp*")

def compare(path, verticenumber, friciton, poly, lcpLos, cellsize, maxdist):
	print friciton
	print poly
	if os.path.isdir(path):
		"path already used"
		return
	os.system("mkdir "+path)
	
	# Generate Random start point:
	general.runalg("qgis:randompointsinlayerbounds",poly,1,10,path +"random_start.shp")
	print "random startpoint"
	# Generate Random target points:
	general.runalg("qgis:randompointsinlayerbounds",poly,1000,10,path +"random_targets.shp")
	print "random targets"


	# Convert shp to geojson:
	cmd = 'ogr2ogr -f "GeoJSON" '+path+'random_start.geojson "'+path+'random_start.shp" -t_srs EPSG:3067'
	os.system(cmd)
	print "start conversion done"

	cmd = 'ogr2ogr -f "GeoJSON" '+path+'random_targets.geojson "'+path+'random_targets.shp" -t_srs EPSG:3067'
	os.system(cmd)
	print "targets conversion done"

	cmd = 'ogr2ogr -f "GeoJSON" '+path+'polygons.geojson '+poly+' -t_srs EPSG:3067'
	os.system(cmd)
	print "polygons conversion done"

	# Solve least cost path using funnel method: (find a way to solve the vertice number...)

	cmd = "java -jar "+lcpLos+" "+path+"polygons.geojson "+str(verticenumber)+" -d "+path+"random_start.geojson "+path+"random_targets.geojson "+str(maxdist)+" "+friciton+" "+path+"results.geojson "+path+"path.geojson"
	os.system(cmd)
	print "funnel method done"

	# Convert geojson results to shp:
	cmd = 'ogr2ogr -f "ESRI Shapefile" '+path+'results.shp "'+path+'results.geojson" -t_srs EPSG:3067'
	os.system(cmd)
	cmd = 'ogr2ogr -f "ESRI Shapefile" '+path+'path.shp "'+path+'path.geojson" -t_srs EPSG:3067'
	os.system(cmd)
	print "conversion done"


	# Rasterize cost surface
	general.runalg("gdalogr:rasterize",poly,friciton,1,cellsize,cellsize,False,5,"-9999",4,75,6,1,False,0,path+"cost_raster.tif")
	print "rasterized"

	# Calculate cumalitve cost raster
	costLyr = QgsRasterLayer(path+"cost_raster.tif", "Cost")
	ext = costLyr.extent()
	xmin = ext.xMinimum()
	ymin = ext.yMinimum()
	xmax = ext.xMaximum()
	ymax = ext.yMaximum()
	box = "%s,%s,%s,%s" % (xmin,xmax,ymin,ymax)

	
	general.runalg("grass:r.cost.full",path +"cost_raster.tif",path +"random_start.shp",False,False,box,0,-1,0.0001,path+"cumul_cost.tif")
	print "cost distance"

	# Multiply by cellsize
	general.runalg("gdalogr:rastercalculator",path+"cumul_cost.tif","1",None,"1",None,"1",None,"1",None,"1",None,"1","A*"+str(cellsize),"-9999",5,"",path+"corrected.tif")
	print "correction by cellsize"


	# Add cost values to target points:
	general.runalg("saga:addgridvaluestopoints",path+"results.shp",path+"corrected.tif",0,path+"added.shp")
	print "add values to points"

	# reproject
	cmd = 'ogr2ogr -f "ESRI Shapefile" '+path+'final_p.shp "'+path+'added.shp" -t_srs EPSG:3067'
	os.system(cmd)

	# Compute difference in path lengths
	layer =  QgsVectorLayer(path+'final_p.shp', 'final_p' , "ogr")

	layer.dataProvider().addAttributes([QgsField("dif", QVariant.Double)])
	layer.dataProvider().addAttributes([QgsField("frac", QVariant.Double)])
	layer.updateFields()

	it = layer.getFeatures()
	layer.startEditing()
	for feat in it:
	  dif = None
	  frac = None
	  if feat.attributes()[1]!=None and feat.attributes()[2]!=None:
		dif = feat.attributes()[2]-feat.attributes()[1]
		if feat.attributes()[1]!=0:
		  frac = feat.attributes()[2] / feat.attributes()[1]
	  layer.changeAttributeValue(feat.id(), 3, dif)
	  layer.changeAttributeValue(feat.id(), 4, frac)

	layer.commitChanges()
	print "difference"

	# generate report
	difs = [
		feat.attributes()[3] for feat in layer.getFeatures()
		if feat.attributes()[3] != None
	]
	fracs = [
		feat.attributes()[4] for feat in layer.getFeatures()
		if feat.attributes()[4] != None
	]

	meand = np.mean(difs)
	mediand = np.median(difs)
	mind = np.min(difs)
	maxd = np.max(difs)
	meanf = np.mean(fracs)
	medianf = np.median(fracs)
	minf = np.min(fracs)
	maxf =  np.max(fracs)


	print "Difference:"
	print "  mean: ", meand
	print "  median: ", mediand
	print "  min: ", mind
	print "  max: ", maxd
	print "Fraction:"
	print "  mean: ", meanf
	print "  median: ", medianf
	print "  min: ", minf
	print "  max: ", maxf

	print "done"
	return [[meand, mediand, mind, maxd, meanf, medianf,minf, maxf],difs, fracs]


data = [["hki2.shp", "ENKLAAVI"],["Hki_peruspiirit.shp", "KUNTA"],["large_sp.shp", "Level3"]] #add maxdist and vnumber

lcpLos ="/home/elias/Documents/kandi/lcpLos/lcpLos/dist/lcpLos.jar"
path = "tmp"
verticenumber = 130177

def compareAll(data):
	results = []
	allDifs = []
	allFracs = []
	for i in xrange(0, len(data)):
		results.append([])
		print results
		d = data[i]
		orig = d[0]
		friciton = d[1]
		pp = compare(path+str(i)+"/", verticenumber, friciton, orig, lcpLos, 100, 1000)
		
		results[i].append(orig)
		for j in xrange(0,8):
		  results[i].append(pp[0][j])
	return results
differenceMatrix = compareAll(data)
print "all done"
f = open('result_matrix.txt', 'w')
for l in differenceMatrix:
  f.write(str(l)[1:-1]+"\n")
f.close()
print "writing done"
# Exit applications
QgsApplication.exitQgis()
app.exit()

