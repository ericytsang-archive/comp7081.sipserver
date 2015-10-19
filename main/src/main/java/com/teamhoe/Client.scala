package com.teamhoe

import java.net.InetSocketAddress
import java.nio.ByteBuffer

class Client
{
    private var serverAddress:InetSocketAddress = null
    private var protocolPort:Int = null
    def setServerAddress(newAddress:InetSocketAddress):Unit =
    {
        serverAddress = newAddress
    }
    def setLocalPort(port:Int):Unit =
    {
        protocolPort = port
    }
    def registerWithServer():Unit =
    {
        new UDPDatagram(
            ByteBuffer.allocate(1).putChar(Protocol.TYPE_REGISTER_CLIENT),
            serverAddress,protocolPort).send()
    }
    def unregisterFromServer():Unit =
    {
        new UDPDatagram(
            ByteBuffer.allocate(1).putChar(Protocol.TYPE_UNREGISTER_CLIENT),
            serverAddress,protocolPort).send()
    }
    def queryServerForClient(clientIp:InetSocketAddress):Unit =
    {
        val hostname:Array[Byte] = clientIp.getHostString.getBytes("UTF-8")
        new UDPDatagram(
            ByteBuffer.allocate(hostname.length+9)
                .putChar(Protocol.TYPE_IS_CLIENT_REGISTERED)    // packet type
                .putInt(hostname.length).put(hostname)          // hostname
                .putInt(clientIp.getPort),                      // port number
            serverAddress,
            protocolPort)
            .send()
    }
}
