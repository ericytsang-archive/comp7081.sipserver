package com.teamhoe

import java.net.{SocketAddress,DatagramPacket,DatagramSocket,InetSocketAddress}
import java.nio.ByteBuffer

/**
 * Created by Eric on 10/18/2015.
 */
class UDPDatagram(
                       private val payload:ByteBuffer,
                       private val remoteAddress:InetSocketAddress,
                       private val localPort:Int)
{
    def getPayload = payload
    def getRemoteAddress = remoteAddress
    def getLocalPort = localPort
    def send():Unit =
    {
        val socket:DatagramSocket = new DatagramSocket()
        socket.bind(new InetSocketAddress(localPort))
        val data = payload.array()
        socket.send(new DatagramPacket(data,data.length,remoteAddress))
        socket.close()
    }
}
