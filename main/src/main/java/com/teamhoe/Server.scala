package com.teamhoe

import java.net.InetSocketAddress
import java.nio.ByteBuffer

import java.util

class Server(val port:Int)
{
    private val registeredClients:util.LinkedHashSet[InetSocketAddress] = new util.LinkedHashSet[InetSocketAddress]()
    private val server:MyUdpServer = new MyUdpServer()

    def register(address:InetSocketAddress):Unit =
    {
        registeredClients.add(address)
    }

    def unregister(address:InetSocketAddress):Unit =
    {
        registeredClients.remove(address)
    }

    def isRegistered(address:InetSocketAddress):Boolean =
    {
        registeredClients.add(address)
    }

    def sendString(address:InetSocketAddress,string:String):Unit =
    {
        val hostname:Array[Byte] = string.getBytes("UTF-8")
        server.send(new UDPDatagram(
            ByteBuffer.allocate(hostname.length+100)
                .putInt(hostname.length)
                .put(hostname),
            address,port)
            .getDatagramPacket)
    }

    private class MyUdpServer extends UDPServer(port)
    {
        override def onDatagram(datagram:UDPDatagram):Unit =
        {
            val payload = datagram.getPayload
            payload.getChar match
            {
            case Protocol.TYPE_REGISTER_CLIENT =>
                register(datagram.getRemoteAddress)
                sendString(datagram.getRemoteAddress,"registered")
            case Protocol.TYPE_UNREGISTER_CLIENT =>
                unregister(datagram.getRemoteAddress)
                sendString(datagram.getRemoteAddress,"unregistered")
            case Protocol.TYPE_IS_CLIENT_REGISTERED =>
                val strlen = payload.getInt
                val hostname = new String(payload.get(new Array[Byte](strlen)).array())
                val port = payload.getInt
                if(isRegistered(new InetSocketAddress(hostname,port)))
                {
                    sendString(datagram.getRemoteAddress,"ACK")
                }
                else
                {
                    sendString(datagram.getRemoteAddress,"NAK")
                }
            }
        }
    }
}
