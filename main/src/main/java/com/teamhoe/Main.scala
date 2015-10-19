package com.example

import java.net.InetSocketAddress
import java.util.Scanner

import com.teamhoe.Client

class Main
{
    def main(args:Array[String]): Unit =
    {
        val client:Client = new Client()
        val scan:Scanner = new Scanner(System.in)
        while(true)
        {
            val input:Array[String] = scan.nextLine().split("\\s+")
            try
            {
                input.head match
                {
                case "setremote" => client.setServerAddress(new InetSocketAddress(input(1),Integer.parseInt(input(2))))
                case "reg" => client.registerWithServer()
                case "ureg" => client.unregisterFromServer()
                case "check" => client.queryServerForClient(new InetSocketAddress(input(1),Integer.parseInt(input(2))))
                }
            }
            catch
                {
                    case e:Exception =>
                    {
                        println(e.getMessage)
                    }
                }
        }
    }
}