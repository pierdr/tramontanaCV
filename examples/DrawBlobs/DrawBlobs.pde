/**** //<>//
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
  hello = new libreTSPSWP(this, "192.168.86.94");
  
}
void draw(){
  background(255);
  if(container!=null)
  {
    fill(0);
    text(container.nBlobs,100,100);
    for(int i=0;i<container.nBlobs;i++)
    {
       PShape s; 
       s = createShape();
       s.beginShape();
       s.fill(0, 0, 255);
       s.noStroke();
        fill(255,0,0);
      for(int j=0;j<container.blobs[i].nPts;j++)
      {
          s.vertex(container.blobs[i].pts[j].x, container.blobs[i].pts[j].y);
      }
      s.endShape(CLOSE);
      shape(s, 0, 0);
    }
  }
}
void onBlobsReceived(LBlobsContainer c,int nBlobs, String ip){
  container = c;
}