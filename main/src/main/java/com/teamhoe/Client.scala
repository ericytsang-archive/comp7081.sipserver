package com.teamhoe

import java.net.InetSocketAddress
import java.nio.ByteBuffer

class Client(
        val serverAddress:InetSocketAddress,
        val protocolPort:Int)
{
    val server:UDPServer = new MyUdpServer()

    def registerWithServer():Unit =
    {
        server.send(new UDPDatagram(
            ByteBuffer.allocate(4).putChar(Protocol.TYPE_REGISTER_CLIENT),
            serverAddress,protocolPort).getDatagramPacket)
    }
    def unregisterFromServer():Unit =
    {
        server.send(new UDPDatagram(
            ByteBuffer.allocate(4).putChar(Protocol.TYPE_UNREGISTER_CLIENT),
            serverAddress,protocolPort).getDatagramPacket)
    }
    def queryServerForClient(clientIp:InetSocketAddress):Unit =
    {
        val hostname:Array[Byte] = clientIp.getHostString.getBytes("UTF-8")
        server.send(new UDPDatagram(
            ByteBuffer.allocate(hostname.length+100)
                .putChar(Protocol.TYPE_IS_CLIENT_REGISTERED)    // packet type
                .putInt(hostname.length).put(hostname)          // hostname
                .putInt(clientIp.getPort),                      // port number
            serverAddress,
            protocolPort)
            .getDatagramPacket)
    }

    private class MyUdpServer extends UDPServer(protocolPort)
    {
        override def onDatagram(datagram:UDPDatagram):Unit =
        {
            val payload = datagram.getPayload
            val strlen = payload.getInt
            val string = new String(payload.get(new Array[Byte](strlen)).array())
            println(string)
        }
    }
}
