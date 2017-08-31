/****
libreTSPSWP - a toolkit for sensing people in space with phones.
With libreTSPSWP you will be able to harness the computing power of your phone to sense people in space and broadcast the result to a Processing sketch.. 
You can download the app here: https://itunes.apple.com/us/app/libretspswp/id1276040950?mt=8
made by Pierluigi Dalla Rosa
***/

import websockets.*;
import libreTSPSWP.*;

libreTSPSWP hello;
LBBoxContainer container;


void setup(){
  size(320,576);
  hello = new libreTSPSWP(this, "192.168.86.94");
  
}
void draw(){
  background(255);
  if(container!=null)
  {
    
    noFill();
    text(container.nBBoxes,100,100);
    for(int i=0;i<container.nBBoxes;i++)
    {
       rect(container.bboxes[i].x,container.bboxes[i].y,container.bboxes[i].w,container.bboxes[i].h);
    }
  }
}
void onBoundingBoxReceived(LBBoxContainer c, int nBlobs, String ip){
  container = c;
}