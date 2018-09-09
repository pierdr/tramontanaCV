/**** //<>//
tramontanaCV - a toolkit for sensing people in space with phones.
With tramontanaCV you will be able to harness the computing power of your phone to sense people in space and broadcast the result to a Processing sketch.. 
You can download the app here: https://itunes.apple.com/us/app/libreTSPSWP/id1276040950?mt=8
made by Pierluigi Dalla Rosa
***/

import websockets.*;

import tramontanaCV.*;

tramontanaCV hello;
int nBlobs = 0;

void setup(){
  size(320,320);
  
  //OPEN THE COMMUNICATION BETWEEN PROCESSING AND YOUR PHONE
  //in the application you can find your IP Address in the communication tab
  //Both your computer and your phone should be on the same WiFi network
  //tramontanaCV uses port 9088, be sure the port is open
  hello = new tramontanaCV(this, "192.168.1.13");
}
void draw(){
  background(255);  
  fill(0);
  text("tramontanaCV - Hello World!",20,20);
  text("number of detected blobs:"+nBlobs,20,40);
  fill(246, 171, 154, 80 );
  ellipse(160,200,map(nBlobs,0,15,0,100),map(nBlobs,0,15,0,100));
}
void onBlobsReceived(LBlobsContainer c, int nBlobs, String ip){
  this.nBlobs = nBlobs;
}
