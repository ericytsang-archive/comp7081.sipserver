package com.teamhoe

import java.net._
import java.nio.ByteBuffer

/**
 * Created by Eric on 10/18/2015.
 */
abstract class UDPServer
{
    private var socket:DatagramSocket = null

    private class AcceptThread extends Thread
    {
        override def run():Unit =
        {
            val data = new Array[Byte](4096)
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

    def onDatagram(datagram:UDPDatagram):Unit
    def startListening(port:Int):Unit =
    {
        // stop previous socket, and bind new datagram socket to port
        stopListening()
        socket = new DatagramSocket(port)

        // start an accept thread for the datagram socket
        new AcceptThread().start()
    }
    def stopListening():Unit =
    {
        // unbind previous datagram socket from port
        if(socket != null && !socket.isClosed)
        {
            socket.close()
        }
    }
}
