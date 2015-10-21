package com.teamhoe

import java.net._
import java.nio.ByteBuffer

/**
 * Created by Eric on 10/18/2015.
 */
abstract class UDPServer(val port:Int)
{
    private val socket:DatagramSocket = new DatagramSocket(port)
    new AcceptThread().start()

    def onDatagram(datagram:UDPDatagram):Unit

    def stop():Unit =
    {
        // unbind previous datagram socket from port
        if(!socket.isClosed) socket.close()
    }

    def send(datagram:DatagramPacket):Unit =
    {
        socket.send(datagram)
    }

    private class AcceptThread extends Thread
    {
        override def run():Unit =
        {
            val data = new Array[Byte](128)
            while(!socket.isClosed)
            {
                val packet:DatagramPacket = new DatagramPacket(data,data.length)
                socket.receive(packet)
                onDatagram(new UDPDatagram(
                    ByteBuffer.wrap(packet.getData,packet.getOffset,packet.getLength),
                    packet.getSocketAddress.asInstanceOf[InetSocketAddress],
                    socket.getPort))
            }
        }
    }
}
