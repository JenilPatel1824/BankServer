package server;

import service.BankService;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankServer
{
    private static final int PORT = 9999;

    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args)
    {
        BankService bankService = new BankService();

        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        try (ServerSocket serverSocket = new ServerSocket(PORT))
        {
            System.out.println("Bank Server running on port " + PORT);

            while (true)
            {
                Socket clientSocket = serverSocket.accept();

                executor.execute(new ClientHandler(clientSocket, bankService));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            executor.shutdown();
        }
    }
}
