package com.teamhoe

import java.net.{DatagramSocket,InetSocketAddress}
import java.nio.ByteBuffer

/**
 * Created by Eric on 10/18/2015.
 */
class UDPDatagram(
                       private val payload:ByteBuffer,
                       private val destination:InetSocketAddress,
                       private val source:InetSocketAddress)
{
    def getPayload = payload
    def getDestinationAddress = destination
    def getSourceAddress = source
    def send():Unit =
    {
        val socket:DatagramSocket = new DatagramSocket()
    }
}
