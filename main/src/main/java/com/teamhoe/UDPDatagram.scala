package com.teamhoe

import java.net.{DatagramPacket,DatagramSocket,InetSocketAddress}
import java.nio.ByteBuffer

/**
 * Created by Eric on 10/18/2015.
 */
class UDPDatagram(
                       val payload:ByteBuffer,
                       val remoteAddress:InetSocketAddress,
                       val localPort:Int)
{
    def getPayload = payload
    def getRemoteAddress = remoteAddress
    def getLocalPort = localPort
    def getDatagramPacket:DatagramPacket =
    {
        val data = payload.array()
        new DatagramPacket(data,data.length,remoteAddress)
    }
}
