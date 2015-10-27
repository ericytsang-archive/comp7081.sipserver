import java.net.InetSocketAddress
import java.util.Scanner

object Main
{
    def makeServer(port:Option[Int]):Option[Server] =
    {
        if(port.isDefined)
            Some(new Server(port.get))
        else
            None
    }

    def makeClient(serverAddress:Option[InetSocketAddress],protocolPort:Option[Int]) : Option[Client] =
    {
        if(serverAddress.isDefined && protocolPort.isDefined)
            Some(new Client(serverAddress.get,protocolPort.get))
        else
            None
    }

    def main(args:Array[String]):Unit =
    {
        val scan:Scanner = new Scanner(System.in)
        var done:Boolean = false
        while(true)
        {
            val input:Array[String] = scan.nextLine().split("\\s+")
            input.head match
            {
            case "clnt" =>
                clientLoop()
                done = true
            case "svr" =>
                serverLoop()
                done = true
            case _ => println("enter 'svr' to start the server program, or 'clnt' to start the client program")
            }
        }
    }

    def clientLoop():Unit =
    {
        var serverAddress:Option[InetSocketAddress] = None
        var protocolPort:Option[Int] = None
        var client:Option[Client] = None
        val scan:Scanner = new Scanner(System.in)
        while(true)
        {
            val input:Array[String] = scan.nextLine().split("\\s+")
            input.head match
            {

            case "setport" =>
                try
                {
                    protocolPort = Option(Integer.parseInt(input(1)))
                    println("protocol port set to "+protocolPort.get)
                    client = makeClient(serverAddress,protocolPort)
                }
                catch
                    {
                        case e:Exception => println("usage: setport [port]")
                    }

            case "setremote" =>
                try
                {
                    serverAddress = Option(new InetSocketAddress(input(1),Integer.parseInt(input(2))))
                    println("remote address set to "+serverAddress.get)
                    client = makeClient(serverAddress,protocolPort)
                }
                catch
                    {
                        case e:Exception => println("usage: setremote [server address] [port]")
                    }

            case "reg" =>
                if(client.isDefined) client.get.registerWithServer()
                println(
                    if(client.isEmpty) "insufficient connection parameters"
                    else "register request sent")

            case "ureg" =>
                if(client.isDefined) client.get.unregisterFromServer()
                println(
                    if(client.isEmpty) "insufficient connection parameters"
                    else "unregister request sent")

            case "check" =>
                try
                {
                    if(client.isDefined) client.get.queryServerForClient(new InetSocketAddress(input(1),Integer.parseInt(input(2))))
                    println(
                        if(client.isEmpty) "insufficient connection parameters"
                        else "query request sent")
                }
                catch
                    {
                        case e:Exception => println("usage: check [client address] [port]")
                    }
            case _ =>
                println("usage:")
                println("setremote [server address] [port] - sets the address of the remote server")
                println("setport [port] - sets the port used to send UDP packets on the client")
                println("reg - registers the client with the server")
                println("ureg - unregisters the client from the server")
                println("check - registers the client with the server")
            }
        }
    }

    def serverLoop():Unit =
    {
        var port:Option[Int] = None
        var server:Option[Server] = None
        val scan:Scanner = new Scanner(System.in)
        while(true)
        {
            val input:Array[String] = scan.nextLine().split("\\s+")
            input.head match
            {

            case "setport" =>
                try
                {
                    port = Option(Integer.parseInt(input(1)))
                    println("protocol port set to " + port.get)
                    server = makeServer(port)
                }
                catch
                    {
                        case e:Exception => println("usage: setport [port]")
                    }
            }
        }
    }
}
