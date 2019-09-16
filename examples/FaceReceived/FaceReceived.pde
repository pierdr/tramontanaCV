/**** //<>//
tramontanaCV - a toolkit for sensing people in space with phones.
With tramontanaCV you will be able to harness the computing power of your phone to sense people in space and broadcast the result to a Processing sketch.. 
You can download the app here: https://itunes.apple.com/us/app/libreTSPSWP/id1276040950?mt=8
made by Pierluigi Dalla Rosa
***/

import websockets.*;

import tramontanaCV.*;

tramontanaCV hello;
TBBoxContainer faces;
int nFaces = 0;

void setup(){
  size(320,480);
  
  //OPEN THE COMMUNICATION BETWEEN PROCESSING AND YOUR PHONE
  //in the application you can find your IP Address in the communication tab
  //Both your computer and your phone should be on the same WiFi network
  //tramontanaCV uses port 9088, be sure the port is open
  hello = new tramontanaCV(this, "10.0.1.4");
 
}
void draw(){
  //draw background
  background(255);
  for(int i=0;i<nFaces;i++)
  {
    //DRAW BOUNDING BOX
    stroke(0,255,255);
    noFill();
    rect(faces.bboxes[i].x*width,faces.bboxes[i].y*height,faces.bboxes[i].w*width,faces.bboxes[i].h*height);
  }
}

/**To receive faces from your device:
1. ON THE APP: In the VISION bar, under the first tab, select DETECT FACES
2. Use the following method 'onFacesReceived' to get the result of face tracking. 
3. The values for the position in TBBoxContainer of the faces are normalized.
**/
void onFacesReceived(TBBoxContainer faces, int nFaces, String ip){
  this.nFaces = nFaces;
  this.faces  = faces;
}
