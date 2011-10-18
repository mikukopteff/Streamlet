package streamlet

import com.wowza.wms.client.IClient
import com.wowza.wms.request.RequestFunction
import com.wowza.wms.amf.AMFDataList
import com.wowza.wms.application.IApplicationInstance
import com.wowza.wms.module._
import com.wowza.wms.stream.IMediaStream



class StreamletForWowza extends ModuleBase with  IModuleOnCall with IModuleOnConnect with IModuleOnStream with Streamlet {




  def onCall(handlerName: String, client: IClient,function: RequestFunction, params: AMFDataList) =
    doServerSideFunction("function", handlerName)

  def onConnect(client: IClient, function: RequestFunction, params: AMFDataList) {
    doServerSideFunction("connect", "")
  }

  def onDisconnect(client: IClient ) {
    doServerSideFunction("disconnect", "")
  }

  def onConnectAccept(client: IClient ) {
    println("onConnectAccept")
  }

  def onConnectReject(client: IClient ) {
    println("onConnectReject")
  }

  def onStreamCreate(stream: IMediaStream ) {
    doServerSideFunction("streamCreate", "")
  }
  
  def onStreamDestroy(stream: IMediaStream ) {
    doServerSideFunction("streamDestroy", "")
  }

  def play(client: IClient, function: RequestFunction , params: AMFDataList) {
    doServerSideFunction("play", "")
		ModuleCore.play(client, function, params)
	}

}