/****
libreTSPSWP - a toolkit for sensing people in space with phones.
With libreTSPSWP you will be able to harness the computing power of your phone to sense people in space and broadcast the result to a Processing sketch.. 
You can download the app here: https://itunes.apple.com/us/app/libretspswp/id1276040950?mt=8
made by Pierluigi Dalla Rosa
***/

import websockets.*;

import libreTSPSWP.*;



libreTSPSWP hello;
LBlobsContainer container;

void setup(){
  size(320,576);
  hello = new libreTSPSWP(this, "192.168.1.13");
  hello.isVerbose = true;
}
void draw(){
  background(255);
  if(container!=null)
  {
    for(int i=0;i<container.nBlobs;i++)
    {
      container.blobs[i].draw();
    }
  }
}
void onBlobsReceived(LBlobsContainer c, int nBlobs, String ip){
  container = c;
}