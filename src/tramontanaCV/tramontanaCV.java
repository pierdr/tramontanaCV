package tramontanaCV;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import tramontanaCV.LBlob;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.util.ArrayList;
import tramontanaCV.BVector;
/**
 * 
 * 
 * 
 * 
 */


@WebSocket
public class tramontanaCV {
	//WEBSOCKET UTILS
	private Session session;
	CountDownLatch latch = new CountDownLatch(1);
	public boolean isOpen;
	private WebSocketClient client;
	private String ipAddress;
	private JSONObject workingJson;
	private int port;
	
	
	//SKETCH REFRENCE
	PApplet sketch;
	
	
	//RETRIEVING MESSAGE
	private int index;
	private Method onBoundingBoxReceived;
	private Method onBlobsReceived;
	private Method onFrameReceived;
	
	//UTILS
	public boolean isVerbose = false;
	
	
	public tramontanaCV(PApplet parent,String IP){
		sketch = parent;
		ipAddress = IP;
		port = 9088;
		
		//LOOK FOR METHODS
		try {
			Class<?> params[] = new Class[3];
			params[0] = LBBoxContainer.class;
			params[1] = int.class;
			params[2] = String.class;
			
			onBoundingBoxReceived = parent.getClass().getMethod("onBoundingBoxReceived", params);
        } catch (Exception e) {
        		
        		printLog("Method onBoundingBoxReceived not found.");
        }
		try {
			Class<?> params[] = new Class[3];
			params[0] = LBlobsContainer.class;
			params[1] = int.class;
			params[2] = String.class;
			
			onBlobsReceived = parent.getClass().getMethod("onBlobsReceived", params);
        } catch (Exception e) {
        		
        		printLog("Method onBlobsReceived not found.");
        }
		//ON FRAME RECEIVED
		try {
			Class<?> params[] = new Class[2];
			params[0] = processing.core.PImage.class;
			params[1] = String.class;
			
			onFrameReceived = parent.getClass().getMethod("onFrameReceived", params);
        } catch (Exception e) {
        		
        		printLog("Method onFrameReceived not found.");
        }
		
		/* WEBSOCKET START */
		connectToSocket("ws://"+IP+":"+port);
		
	}
//	public tramontanaCV(PApplet parent,String IP,int Port)
//	{
//		this(parent,IP);
//		this.port = Port;
//		
//	}
	private void connectToSocket(String endpointURI) {
		client = new WebSocketClient();
		try {
			client.start();
			URI echoUri = new URI(endpointURI);
			ClientUpgradeRequest request = new ClientUpgradeRequest();
			client.connect(this, echoUri, request);
			this.getLatch().await();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * Sending incoming messages to the Processing sketch's websocket event function 
	 * 
	 * @param session The connection between server and client
	 * @param message The received message
	 * @throws IOException If no event fonction is registered in the Processing sketch then an exception is thrown, but it will be ignored
	 */
	@OnWebSocketMessage
	public void onText(Session session, String message) throws IOException {
		try {
			workingJson = sketch.parseJSONObject(message);
		}catch(Exception e)
		{
			printLog("incorrect JSON: "+e);
			return;
		}
		String event = (String) workingJson.get("m");
		if(event.contains("x")) 
		{
			//RECEIVED BOUNDING BOX
			JSONArray a = new JSONArray();
			
			try {
				a = workingJson.getJSONArray("a");
			}catch(Exception e){
				printLog("Error in parsing Bounding boxes; \n "+e);
			}
			
			try {
				
				LBBoxContainer container = new LBBoxContainer();
				container.bboxes = new LBBox[a.size()];
				container.nBBoxes = a.size();
				
					for(index=0;index<a.size();index+=1)
					{
						
						JSONArray b = a.getJSONArray(index);
						
						container.bboxes[index] = new LBBox(b.getInt(0),b.getInt(1),b.getInt(2),b.getInt(3));
					}
				
				try {
					onBoundingBoxReceived.invoke(sketch,container,(int)a.size(),ipAddress);
				}
				catch(Exception e)
				{
					printLog("invoke onBoundingBoxReceived error: "+e);
				}
			}
			catch(Exception e)
			{
				printLog("processing bounding box resulted in an error "+e);
			}
		}
		else if(event.contains("b")) 
		{
			//RECEIVED BLOBS
			JSONArray a = new JSONArray();
			
			try {
				a = workingJson.getJSONArray("a");
			}catch(Exception e){
				printLog("parse error "+e);
			}
			
			try {
				
				LBlobsContainer container = new LBlobsContainer();
				container.blobs = new LBlob[a.size()];
				container.nBlobs = a.size();
				
					for(index=0;index<a.size();index+=1)
					{
						container.blobs[index] = new LBlob(sketch);
						JSONArray b = a.getJSONArray(index);
						container.blobs[index].pts = new BVector[(int)b.size()/2];
						container.blobs[index].nPts = b.size()/2;
						for(int i=0;i<b.size()/2;i++)
						{
							container.blobs[index].pts[i] = new BVector(b.getInt((int)i*2),b.getInt((int)(i*2)+1));
						}
					}
				
				try {
					onBlobsReceived.invoke(sketch, container, container.nBlobs ,ipAddress);
				}
				catch(Exception e)
				{
					printLog("invoke error: blobs should have been received and parsed but "+e);
				}
			}
			catch(Exception e)
			{
				printLog("processing blobs resulted in an error: "+e);
			}
			
		}
	}
	
	private void printLog(String s)
	{
		if(isVerbose) {
			System.out.println(s);
		}
	}
	
	
	/**
	 * 
	 * Handling establishment of the connection
	 * 
	 * @param session The connection between server and client
	 */
	@OnWebSocketConnect
	public void onConnect(Session session) {
		this.session = session;
		latch.countDown();
		isOpen = true;
		printLog("connection open");
	}

	/**
	 * 
	 * Sends message to the websocket server
	 * 
	 * @param str The message to send to the server
	 */
	private void sendMessage(String str) {
		try {
			session.getRemote().sendString(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Handles errors occurring and writing them to the console 
	 * 
	 * @param cause The cause of an error
	 */
	@OnWebSocketError
	public void onError(Throwable cause) {
		System.out.printf("onError(%s: %s)%n",cause.getClass().getSimpleName(), cause.getMessage());
		cause.printStackTrace(System.out);
		isOpen = false;
	}

	private CountDownLatch getLatch() {
		return latch;
	}
}
